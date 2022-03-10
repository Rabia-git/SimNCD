package v30.noncommdiseases.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import v30.noncommdiseases.ObesitySimulationCore;
import v30.noncommdiseases.cognitive.Alternative;
import v30.noncommdiseases.cognitive.Alternative2Tuple;
import v30.noncommdiseases.cognitive.AlternativeDistances;
import v30.noncommdiseases.cognitive.EvaluationSet;
import v30.noncommdiseases.cognitive.Preference;
import v30.noncommdiseases.factors.Age;
import v30.noncommdiseases.factors.BMI;
import v30.noncommdiseases.factors.Factor;
import v30.noncommdiseases.factors.Gender;
import v30.noncommdiseases.factors.MVPA;
import v30.noncommdiseases.interaction.Engage;
import v30.noncommdiseases.interaction.Move;
import v30.noncommdiseases.interaction.Plan;
import v30.noncommdiseases.interaction.Quit;
import v30.noncommdiseases.interaction.ShareData;
import v30.noncommdiseases.interaction.UpdateEPhy;
import v30.noncommdiseases.interaction.UpdateESoc;
import v30.noncommdiseases.interaction.UpdateI;
import v30.noncommdiseases.interaction.functions.EngageActivitySourceFunction;
import v30.noncommdiseases.interaction.functions.QuitActivitySourceFunction;
import v30.noncommdiseases.interaction.functions.ShareDataSourceFunction;
import v30.noncommdiseases.interaction.functions.ShareDataTargetFunction;
import v30.noncommdiseases.util.DataSharedBetweenAgents;
import v30.noncommdiseases.util.Output;
import fr.lifl.jedi.model.Agent;
import fr.lifl.jedi.model.halo.HaloBuilder;
import fr.lifl.jedi.model.interactionDeclaration.InteractionMatrixLine;

/**
 * The java class that represents the child agent family in the simulation of
 * childhood obesity.
 * 
 * @author Rabia AZIZA
 */
public class Child extends Agent implements EngageActivitySourceFunction,
		ShareDataSourceFunction, ShareDataTargetFunction,
		QuitActivitySourceFunction {

	public static final InteractionMatrixLine CHILD_MATRIX_LINE = createChildMatrixLine();
	public ArrayList<Activity> perceivedActivities;
	@SuppressWarnings("rawtypes")
	public ArrayList<Class> staticPlan;

	// Cognitive
	public ArrayList<Preference> preferences;
	// Yearly coefficient of satisfaction of 'AP de Loisir'
	// in [0 ; 2]
	public double yearlySatisfactionAPL = 0;
	public double currentSatisfactionAPL = -1;

	private int indexCurrentActivityInPlan = -1;
	private Activity plannedNextActivity = null;
	public int nb_sc = 0;
	public int nb_p = 0;
	public int nb_cc = 0;
	public int nb_h = 0;
	// Yearly daily opportunityToPA=%contribution
	public double yearlyContribMVPA = 0;

	public static int toAdulthood = 0;

	public Activity getPlannedNextActivity() {
		return plannedNextActivity;
	}

	public String status = "goalless";

	public ArrayList<Child> participants = new ArrayList<Child>();
	private ArrayList<DataSharedBetweenAgents> participantsData = new ArrayList<DataSharedBetweenAgents>();

	public Map<String, Factor> factors = new HashMap<String, Factor>();

	public String fileName = "";

	/**
	 * This method creates the interaction matrix line of all children
	 */
	private static InteractionMatrixLine createChildMatrixLine() {
		InteractionMatrixLine childrenLine = new InteractionMatrixLine();
		childrenLine.add(new Plan(), 0);
		childrenLine.add(new Move(), 1);
		childrenLine.add(new Engage(), Activity.class, 2, 2);
		childrenLine.add(new UpdateEPhy(), 3);
		childrenLine.add(new ShareData(), Child.class, 0, 4);
		childrenLine.add(new UpdateESoc(), 5);
		childrenLine.add(new UpdateI(), 6);
		childrenLine.add(new Quit(), Activity.class, 0, 7);

		return childrenLine;
	}

	/**
	 * Create the factors
	 **/
	private void setFactors(int ageYears, int gender, int e_motivPA,
			String statusBMI) {
		// factors.put("age", new Age(ageYears));

		// v29 : fixed day of birth
		factors.put("age", new Age(ageYears, 0));
		// Gender : 1=Boy / 2=Girl
		factors.put("gender", new Gender(gender));
		// Conversion of e_motivPA in percentage
		factors.put("mvpa", new MVPA((Gender) factors.get("gender"),
				(Age) factors.get("age"), ((double) e_motivPA) / 100));
		factors.put("bmi", new BMI((Gender) this.factors.get("gender"),
				(Age) factors.get("age")));
				//, statusBMI));
	}

	@SuppressWarnings({ "rawtypes" })
	public Child(int ageYears, int gender, int e_motivPA, String statusBMI,
			ArrayList<Activity> perceivedActivities, ArrayList<Class> plan,
			ArrayList<Preference> preferences) {
		super(HaloBuilder.INSTANCE.createRectangleShapedArea(2, 2),
				CHILD_MATRIX_LINE);
		this.perceivedActivities = perceivedActivities;
		this.staticPlan = plan;
		this.preferences = preferences;
		Activity myHome = this.getPerceivedActivity(Home.class);

		if (myHome != null) {
			this.surface.x = myHome.getXPosition();
			this.surface.y = myHome.getYPosition();
		}

		this.setFactors(ageYears, gender, e_motivPA, statusBMI);

		if (ObesitySimulationCore.OUTPUT_DB_GENERAL) {
			Output.insertToDB_ChildData(this);
		}
	}

	/**
	 * Updates the agent at the beginning of every time step.
	 */
	public void update() {
		if (((Age) this.factors.get("age")).year <= 18) {
			Iterator<Factor> it = this.factors.values().iterator();
			while (it.hasNext()) {
				Factor f = it.next();
				f.updateN(this);
			}
			if (ObesitySimulationCore.OUTPUT_CONSOLE_DETAILS)
				System.out.println(this + "-" + status);
		}
	}

	public Activity plan() {
		this.plannedNextActivity = this.getNextPrePlannedActivity();

		if (this.plannedNextActivity == null)
			if (!ObesitySimulationCore.COGNITIVE_REASONING) {
				// Dynamic RANDOM choice of goalActivity
			} else {
				// Dynamic COGNITIVE choice of goalActivity
				ArrayList<Alternative> DM = new ArrayList<Alternative>();
				this.makeDecisionMatrix(DM);

				// Agregate results using arithmetic mean on 2-tuple ling
				ArrayList<Alternative2Tuple> DM2Tuple = this
						.make2TupleDecisionMatrix(DM);

				// Create distance DM
				ArrayList<AlternativeDistances> DM2TupleDistances = this
						.make2TupleDistancesDecisionMatrix(DM2Tuple);

				this.plannedNextActivity = this
						.getBestActivity(DM2TupleDistances);
			}
		if (this.plannedNextActivity != null) {
			if (this.plannedNextActivity.getClass().equals(SportClub.class))
				this.nb_sc++;
			else if (this.plannedNextActivity.getClass().equals(Park.class))
				this.nb_p++;
			else if (this.plannedNextActivity.getClass().equals(
					CulturalClub.class))
				this.nb_cc++;
			else if (this.plannedNextActivity.getClass().equals(Home.class))
				this.nb_h++;
			this.updateYearlyContributionMVPA();
		}
		// System.out.println(this+" -------- After Planning = "+plannedNextActivity);
		return this.plannedNextActivity;
	}

	private Activity getBestActivity(
			ArrayList<AlternativeDistances> dM2TupleDistances) {
		int index = -1;
		Double max = 0.0;
		for (int i = 0; i < dM2TupleDistances.size(); i++)
			if (dM2TupleDistances.get(i).dRelativeProxmity > max) {
				index = i;
				max = dM2TupleDistances.get(i).dRelativeProxmity;
			}
		if (index > -1) {
			// this.updateYearlySatisfaction(dM2TupleDistances.get(index).dRelativeProxmity);
			this.currentSatisfactionAPL = dM2TupleDistances.get(index).dRelativeProxmity * 2;
			return dM2TupleDistances.get(index).activity;
		}
		return null;
	}

	private void updateYearlySatisfaction() {
		if (((Age) this.factors.get("age")).year == ((Age) this.factors
				.get("age")).yearAtInitialization) {
			this.yearlySatisfactionAPL += currentSatisfactionAPL
					/ (ObesitySimulationCore.SCHOOL_DAYS_PER_YEAR
							- ((Age) this.factors.get("age")).dayAtInitialization + 1);
		} else {
			this.yearlySatisfactionAPL += currentSatisfactionAPL
					/ ObesitySimulationCore.SCHOOL_DAYS_PER_YEAR;
		}
	}

	private void updateYearlyContributionMVPA() {
		if (((Age) this.factors.get("age")).year == ((Age) this.factors
				.get("age")).yearAtInitialization) {
			this.yearlyContribMVPA += this.plannedNextActivity.getContribMVPA()
					/ (ObesitySimulationCore.SCHOOL_DAYS_PER_YEAR
							- ((Age) this.factors.get("age")).dayAtInitialization + 1);
		} else {
			this.yearlyContribMVPA += this.plannedNextActivity.getContribMVPA()
					/ ObesitySimulationCore.SCHOOL_DAYS_PER_YEAR;
		}
	}

	public void resetYearlyData() {
		this.nb_cc = 0;
		this.nb_h = 0;
		this.nb_p = 0;
		this.nb_sc = 0;
		this.yearlySatisfactionAPL = 0;
		this.yearlyContribMVPA = 0;
	}

	public void makeDecisionMatrix(ArrayList<Alternative> DM) {
		// prepare the set of perceived activities
		for (int i = 0; i < this.perceivedActivities.size(); i++)
			if (!this.perceivedActivities.get(i).getClass()
					.equals(PrimarySchool.class)
					&& !this.perceivedActivities.get(i).getClass()
							.equals(CollegeSchool.class)
					&& !this.perceivedActivities.get(i).getClass()
							.equals(SecondarySchool.class))
				if (this.perceivedActivities.get(i).isCurrentlyActive())
					DM.add(new Alternative(this.perceivedActivities.get(i)));

		// each alternative contains an EvaluationSet per criteria
		for (int i = 0; i < DM.size(); i++)
			for (int j = 0; j < ObesitySimulationCore.criteria.size(); j++)
				DM.get(i).evaluationSets.add(new EvaluationSet(
						ObesitySimulationCore.criteria.get(j),
						new ArrayList<Integer>()));
		// the decision matrix is DM: it contains an alternative for each
		// activity, and each alternative contains evaluationSets=pairs
		// <criterion, likert evaluations> --> we still need to fill in the
		// evaluations :
		for (int i = 0; i < DM.size(); i++)
			for (int j = 0; j < DM.get(i).activity.characteristics.size(); j++)
				if (DM.get(i).activity.characteristics.get(j).c)
					for (int k = 0; k < this.preferences.size(); k++)
						if (this.preferences.get(k).subCriterion
								.equals(DM.get(i).activity.characteristics
										.get(j).subCriterion))
							for (int l = 0; l < DM.get(i).evaluationSets.size(); l++)
								if (DM.get(i).evaluationSets.get(l).criterion
										.equals(this.preferences.get(k).subCriterion.criterion))
									DM.get(i).evaluationSets.get(l).lingTerms
											.add(this.preferences.get(k).lingTerm);
	}

	public String DMtoString(ArrayList<Alternative> DM) {
		String s = "";
		for (int i = 0; i < DM.size(); i++) {
			s += DM.get(i).activity + "\t";
			for (int j = 0; j < DM.get(i).evaluationSets.size(); j++) {
				for (int k = 0; k < DM.get(i).evaluationSets.get(j).lingTerms
						.size(); k++)
					s += "~" + DM.get(i).evaluationSets.get(j).lingTerms.get(k);
				s += "\t";
			}
			s += "\n";
		}
		return s;
	}

	public ArrayList<Alternative2Tuple> make2TupleDecisionMatrix(
			ArrayList<Alternative> DM) {
		ArrayList<Alternative2Tuple> DM2Tuple = new ArrayList<Alternative2Tuple>();
		for (int i = 0; i < DM.size(); i++) {
			DM2Tuple.add(new Alternative2Tuple(DM.get(i).activity,
					new ArrayList<Double>()));
			Double sum = 0.0;
			for (int j = 0; j < DM.get(i).evaluationSets.size(); j++) {
				for (int k = 0; k < DM.get(i).evaluationSets.get(j).lingTerms
						.size(); k++)
					sum += DM.get(i).evaluationSets.get(j).lingTerms.get(k);
				DM2Tuple.get(i).evaluations.add(sum
						/ DM.get(i).evaluationSets.get(j).lingTerms.size());
				sum = 0.0;
			}
		}
		return DM2Tuple;
	}

	public String DM2TupletoString(ArrayList<Alternative2Tuple> DM2Tuple) {
		String s = "";
		for (int i = 0; i < DM2Tuple.size(); i++) {
			s += DM2Tuple.get(i).activity + "\t";
			for (int j = 0; j < DM2Tuple.get(i).evaluations.size(); j++) {
				s += "~" + DM2Tuple.get(i).evaluations.get(j);
				s += "\t";
			}
			s += "\n";
		}
		return s;
	}

	public ArrayList<AlternativeDistances> make2TupleDistancesDecisionMatrix(
			ArrayList<Alternative2Tuple> DM2Tuple) {
		ArrayList<AlternativeDistances> DM2TupleDistances = new ArrayList<AlternativeDistances>();
		for (int i = 0; i < DM2Tuple.size(); i++) {
			Double dPositive = 0.0;
			Double dNegative = 0.0;
			for (int j = 0; j < DM2Tuple.get(i).evaluations.size(); j++) {
				dPositive += 3 - DM2Tuple.get(i).evaluations.get(j);
				dNegative += DM2Tuple.get(i).evaluations.get(j) - 1;
			}
			DM2TupleDistances.add(new AlternativeDistances(
					DM2Tuple.get(i).activity, dPositive
							/ DM2Tuple.get(i).evaluations.size(), dNegative
							/ DM2Tuple.get(i).evaluations.size(), dNegative
							/ (dPositive + dNegative)));
		}
		return DM2TupleDistances;
	}

	public String DM2TupleDistancestoString(
			ArrayList<AlternativeDistances> DM2TupleDistances) {
		String s = "";
		for (int i = 0; i < DM2TupleDistances.size(); i++) {
			s += DM2TupleDistances.get(i).activity + "\t"
					+ DM2TupleDistances.get(i).dPositive + "\t"
					+ DM2TupleDistances.get(i).dNegative + "\t"
					+ DM2TupleDistances.get(i).dRelativeProxmity + "\n";
		}
		return s;
	}

	// V29: FOR OUTPUT ONLY:
	public double getRelativeProximity(Class c) {
		ArrayList<Alternative> DM = new ArrayList<Alternative>();
		this.makeDMforAllPercActsEitherActiveOrNot(DM);
		ArrayList<Alternative2Tuple> DM2Tuple = this
				.make2TupleDecisionMatrix(DM);
		ArrayList<AlternativeDistances> DM2TupleDistances = this
				.make2TupleDistancesDecisionMatrix(DM2Tuple);
		for (int i = 0; i < DM2TupleDistances.size(); i++)
			if (DM2TupleDistances.get(i).activity.getClass().equals(c))
				return DM2TupleDistances.get(i).dRelativeProxmity;
		return -1;
	}

	// V29: FOR OUTPUT ONLY:
	public void makeDMforAllPercActsEitherActiveOrNot(ArrayList<Alternative> DM) {
		// prepare the set of perceived activities
		for (int i = 0; i < this.perceivedActivities.size(); i++)
			if (!this.perceivedActivities.get(i).getClass()
					.equals(PrimarySchool.class)
					&& !this.perceivedActivities.get(i).getClass()
							.equals(CollegeSchool.class)
					&& !this.perceivedActivities.get(i).getClass()
							.equals(SecondarySchool.class))
				DM.add(new Alternative(this.perceivedActivities.get(i)));
		// each alternative contains an EvaluationSet per criteria
		for (int i = 0; i < DM.size(); i++)
			for (int j = 0; j < ObesitySimulationCore.criteria.size(); j++)
				DM.get(i).evaluationSets.add(new EvaluationSet(
						ObesitySimulationCore.criteria.get(j),
						new ArrayList<Integer>()));
		// Filling in the evaluations :
		for (int i = 0; i < DM.size(); i++)
			for (int j = 0; j < DM.get(i).activity.characteristics.size(); j++)
				if (DM.get(i).activity.characteristics.get(j).c)
					for (int k = 0; k < this.preferences.size(); k++)
						if (this.preferences.get(k).subCriterion
								.equals(DM.get(i).activity.characteristics
										.get(j).subCriterion))
							for (int l = 0; l < DM.get(i).evaluationSets.size(); l++)
								if (DM.get(i).evaluationSets.get(l).criterion
										.equals(this.preferences.get(k).subCriterion.criterion))
									DM.get(i).evaluationSets.get(l).lingTerms
											.add(this.preferences.get(k).lingTerm);
	}

	public Activity getNextPrePlannedActivity() {
		if (this.indexCurrentActivityInPlan == this.staticPlan.size() - 1)
			return this.getPerceivedActivity(this.staticPlan.get(0));
		return this.getPerceivedActivity(this.staticPlan
				.get(indexCurrentActivityInPlan + 1));
	}

	@SuppressWarnings("unused")
	private Activity getRandomlyChosenActivity() {
		ArrayList<Activity> accessibleActivities = getAllPerceivedActivitiesByHourInterval(
				2, 3);
		return accessibleActivities.get((new Random())
				.nextInt(accessibleActivities.size()));
	}

	private ArrayList<Activity> getAllPerceivedActivitiesByHourInterval(
			int debHour, int endHour) {
		ArrayList<Activity> accessibleActivities = new ArrayList<Activity>();
		for (int i = 0; i < this.perceivedActivities.size(); i++)
			if (this.perceivedActivities.get(i).isActiveAtHour(debHour))
				accessibleActivities.add(this.perceivedActivities.get(i));
		return accessibleActivities;
	}

	@Override
	public void setStatus(String status) {
		this.status = status;
	}

	public boolean hasAGoalActivity() {
		return this.status == "hasGoal";
	}

	@Override
	public boolean isOnGoal() {
		return this.status == "onGoal";
	}

	// The activity the child is currently practicing.
	// Null means no activity is being practiced.
	private Activity currentActivity = null;

	@Override
	public void engageActivity(Activity a) {
		this.currentActivity = a;
		this.IncrementIndexCurrentActivityInPlan();
		if (this.currentSatisfactionAPL != -1)
			this.updateYearlySatisfaction();
	}

	public boolean isAchievingGoal() {
		return this.status == "achievingGoal";
	}

	public void updateEPhy() {
		((MVPA) this.factors.get("mvpa")).updateEPhy(this.currentActivity);
		if (ObesitySimulationCore.OUTPUT_CONSOLE_DETAILS)
			System.out.println(this + "-Ephy:"
					+ ((MVPA) this.factors.get("mvpa")).mvpa + " / curr:"
					+ ((MVPA) this.factors.get("mvpa")).mvpaActivity);
	}

	@Override
	public boolean isUpdatedEPhy() {
		return this.status == "updatedEPhy";
	}

	@Override
	public DataSharedBetweenAgents dataToShare() {
		return new DataSharedBetweenAgents(this,
				((MVPA) this.factors.get("mvpa")).mvpa);// e_motivPA);// dMVPA);
	}

	@Override
	public void addSharedData(DataSharedBetweenAgents d) {
		this.participantsData.add(d);
	}

	public boolean isDataShared() {
		return this.status == "dataShared";
	}

	public void updateESoc() {
		((MVPA) this.factors.get("mvpa")).updateESoc(this.participantsData);
		if (ObesitySimulationCore.OUTPUT_CONSOLE_DETAILS)
			System.out.println(this + "-Esoc:"
					+ ((MVPA) this.factors.get("mvpa")).mvpa);
		this.participants.clear();
		this.participantsData.clear();
	}

	public boolean isUpdatedESoc() {
		return this.status == "updatedESoc";
	}

	public void updateI() {
		// v28 :
		((MVPA) this.factors.get("mvpa")).updateI(this);
		if (ObesitySimulationCore.OUTPUT_CONSOLE_DETAILS)
			System.out.println(this + "-updateI:"
					+ ((MVPA) this.factors.get("mvpa")).mvpa + " / satisf:"
					+ this.currentSatisfactionAPL);
	}

	@Override
	public boolean isUpdatedI() {
		return this.status == "updatedI";
	}

	@Override
	public void quitActivity() {

		if (this.indexCurrentActivityInPlan == this.staticPlan.size() - 1) {
			((MVPA) this.factors.get("mvpa")).addToAnnualMVPA(this);
		}
		this.currentActivity = null;
		this.currentSatisfactionAPL = -1;
	}

	public Activity getPerceivedActivity(@SuppressWarnings("rawtypes") Class c) {
		for (int i = 0; i < this.perceivedActivities.size(); i++)
			if (this.perceivedActivities.get(i).getClass() == c)
				return this.perceivedActivities.get(i);
		return null;
	}

	public int ifActivityIsPerceived(@SuppressWarnings("rawtypes") Class c) {
		if (this.getPerceivedActivity(c) != null)
			return 1;
		return 0;
	}

	public Activity getCurrentPlannedActivity() {
		if (this.indexCurrentActivityInPlan >= 0)
			return this.getPerceivedActivity(this.staticPlan
					.get(indexCurrentActivityInPlan));
		else
			return null;
	}

	@Override
	public void IncrementIndexCurrentActivityInPlan() {
		if (this.indexCurrentActivityInPlan == this.staticPlan.size() - 1)
			this.indexCurrentActivityInPlan = 0;
		else
			this.indexCurrentActivityInPlan = indexCurrentActivityInPlan + 1;
	}
}
