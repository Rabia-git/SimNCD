package v30.noncommdiseases;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import v30.noncommdiseases.agent.Activity;
import v30.noncommdiseases.agent.Child;
import v30.noncommdiseases.agent.CollegeSchool;
import v30.noncommdiseases.agent.CulturalClub;
import v30.noncommdiseases.agent.Home;
import v30.noncommdiseases.agent.Park;
import v30.noncommdiseases.agent.PrimarySchool;
import v30.noncommdiseases.agent.SecondarySchool;
import v30.noncommdiseases.agent.SportClub;
import v30.noncommdiseases.cognitive.Characteristic;
import v30.noncommdiseases.cognitive.Criterion;
import v30.noncommdiseases.cognitive.Preference;
import v30.noncommdiseases.cognitive.SubCriterion;
import v30.noncommdiseases.factors.Age;
import v30.noncommdiseases.util.IntervalConstraint;
import v30.noncommdiseases.util.Output;
import fr.lifl.jedi.controllersCore.AbstractControlGUIController;
import fr.lifl.jedi.controllersCore.simulationRun.SimulationCore;
import fr.lifl.jedi.model.Agent;
import fr.lifl.jedi.model.Environment;
import fr.lifl.jedi.util.SimulationProperties;

/**
 * This class defines how the simulation is initialized, and when the simulation
 * is considered as finished.
 * 
 * @author Rabia AZIZA
 */
public class ObesitySimulationCore extends SimulationCore {

	public static final boolean OUTPUT_CONSOLE_DETAILS = false;
	public static final boolean OUTPUT_DB_GENERAL = true;
	public static String OUTPUT_TableUpdates = "";
	public static String OUTPUT_TablePreferences = "";
	public static String OUTPUT_TableSatisfactions = "";
	public static Connection connection = null;
	public static final boolean COGNITIVE_REASONING = false;

	public static final int SCHOOL_DAYS_PER_YEAR = 45; // 180;

	/**
	 * Initial parameters of agents in the simulation.
	 */
	public static int GENDER = 2;
	public static final int NB_CHILDREN = 474;//2000;  ANGELOPOULOS 2009
	private final int NB_PRIMARY_SCHOOLS = 1;//40;
	private final int NB_COLLEGE_SCHOOLS = 1;//40;
	private final int NB_SECONDARY_SCHOOLS = 1;//40;
	private final int NB_HOMES = NB_CHILDREN;//500;
	private final int NB_PARKS = 1;//40;
	private final int NB_CULTURALCLUBS = 0;
	private final int NB_SPORTCLUBS = 1;//40;

	private final int NB_CHILDREN_AGE_FROM_6_TO_10 = NB_CHILDREN;//2000;
	private final int NB_CHILDREN_AGE_FROM_11_TO_14 = 0;
	private final int NB_CHILDREN_AGE_FROM_15_TO_17 = 0;

	private final int NB_UNDERWEIGHT = 0;
	private final int NB_NORMAL = NB_CHILDREN;
	private final int NB_OVERWEIGHT = 0;//2000;
	private final int NB_OBESE = 0;

	private int NB_FAMILIES_1CHILD = NB_CHILDREN;
	private int NB_FAMILIES_2CHILD = 0;
	private int NB_FAMILIES_3CHILD = 0;
	private int NB_FAMILIES_4CHILD = 0;

	// MVPA in [200%*basicMVPA ; 20%*basicMVPA]
	private int E_MOTIV_PA_UW = 1;
	private int E_MOTIV_PA_N = 1;
	private int E_MOTIV_PA_OW = 1;
	private int E_MOTIV_PA_O = 1;

	public static final double CONTRIB_MVPA_PS = 1;
	public static final double CONTRIB_MVPA_CS = 1;
	public static final double CONTRIB_MVPA_SS = 1;

	public static final double CONTRIB_MVPA_SC = 1;
	public static final double CONTRIB_MVPA_P = 0.6;
	public static final double CONTRIB_MVPA_CC = 0;

	public static final double CONTRIB_MVPA_Home1 = 1;
	public static final double CONTRIB_MVPA_Home2 = CONTRIB_MVPA_Home1;

	private ArrayList<InitializationScenario> childrenParameters = new ArrayList<InitializationScenario>();

	private ArrayList<Activity> allActivities = new ArrayList<Activity>();
	public static final int MAX_HOURS_PER_DAY = 4; //6;
	public static final int SIMULATION_STEPS_PER_HOUR = 4;
	public static int year = 0, day = 0, hour = 0;

	// Cognitive
	public static ArrayList<Criterion> criteria = new ArrayList<Criterion>();
	public static ArrayList<SubCriterion> subCriteria = new ArrayList<SubCriterion>();

	public ObesitySimulationCore(AbstractControlGUIController<?> guiControl,
			Environment e) {
		super(guiControl, e);
	}

	/**
	 * This method defines the ending criterion of the simulation. Returning
	 * <code>false</code> means that the simulation never ends.
	 */
	public boolean hasToStop() {
		return false;
	}

	@Override
	public void beforeStep() {
		year = this.getYear();
		day = this.getDay();
		hour = this.getHour();
		if (ObesitySimulationCore.OUTPUT_CONSOLE_DETAILS)
			System.out.println(this.getYear() + "y/" + this.getDay() + "d/"
					+ this.getHour() + "h");
	}

	public int getYear() {
		if (ObesitySimulationCore.year != ((int) getCurrentSimulationStep() / (MAX_HOURS_PER_DAY
				* SIMULATION_STEPS_PER_HOUR * ObesitySimulationCore.SCHOOL_DAYS_PER_YEAR)))
			System.out
					.println("YEAR="
							+ (int) getCurrentSimulationStep()
							/ (MAX_HOURS_PER_DAY * SIMULATION_STEPS_PER_HOUR * ObesitySimulationCore.SCHOOL_DAYS_PER_YEAR));
		return (int) getCurrentSimulationStep()
				/ (MAX_HOURS_PER_DAY * SIMULATION_STEPS_PER_HOUR * ObesitySimulationCore.SCHOOL_DAYS_PER_YEAR);
	}

	public int getDay() {
		return (int) (this.getCurrentSimulationStep() - this.getYear()
				* MAX_HOURS_PER_DAY * SIMULATION_STEPS_PER_HOUR
				* ObesitySimulationCore.SCHOOL_DAYS_PER_YEAR)
				/ (MAX_HOURS_PER_DAY * SIMULATION_STEPS_PER_HOUR);
	}

	public int getHour() {
		return (int) (this.getCurrentSimulationStep() % (MAX_HOURS_PER_DAY * SIMULATION_STEPS_PER_HOUR))
				/ SIMULATION_STEPS_PER_HOUR;
	}

	/**
	 * This method initializes the simulation. It instantiates all agents, and
	 * puts them into the environment.
	 */
	@SuppressWarnings("rawtypes")
	public void initialize() {

		this.prepareOutputs();

		this.prepareCriteria();

		// Primary School agents creation.
		this.createRandomXYPS();

		// College School agents creation.
		this.createRandomXYCS();

		// Secondary School agents creation.
		this.createRandomXYSS();

		// Home agents creation.
		this.createRandomXYHouses();

		// Park agents creation.
		this.createRandomXYParks();

		// CulturalClub agents creation.
		this.createRandomXYCulturalClubs();

		// SportClub agents creation.
		this.createRandomXYSportClubs();

		this.allActivities = this.getAllActivitiesInEnv();

		// Preparing parameter scenarios for child agents
		this.prepareChildrenParameters();

		// Child agents creation.

		for (int ichild = 0; ichild < ObesitySimulationCore.NB_CHILDREN; ichild++) {

			ArrayList<Activity> percActs = new ArrayList<Activity>();

			for (int i = 0; i < this.childrenParameters.get(ichild).percActivities
					.size(); i++)
				percActs.add(this.childrenParameters.get(ichild).percActivities
						.get(i));

			ArrayList<Class> plan = new ArrayList<Class>();

			// Plan
			if (this.childrenParameters.get(ichild).age < 11)
				plan.add(PrimarySchool.class);
			else if (this.childrenParameters.get(ichild).age < 15)
				plan.add(CollegeSchool.class);
			else
				plan.add(SecondarySchool.class);
			//plan.add(null);// *****Empty day segment
			plan.add(Home.class);
			ArrayList<Preference> preferences = this
					.getRandomSetOfPreferences();

			Child c = new Child(this.childrenParameters.get(ichild).age,
					this.childrenParameters.get(ichild).gender,
					this.childrenParameters.get(ichild).e_motivPA,
					this.childrenParameters.get(ichild).statusBMI, percActs,
					plan, preferences);
			if (ObesitySimulationCore.OUTPUT_CONSOLE_DETAILS)
				System.out.println(c + "-"
						+ this.childrenParameters.get(ichild).age + "-"
						+ this.childrenParameters.get(ichild).gender + "-motiv"
						+ this.childrenParameters.get(ichild).e_motivPA + "-"
						+ this.childrenParameters.get(ichild).statusBMI
						+ "-day"
						+ ((Age) c.factors.get("age")).dayAtInitialization
						+ "-" + percActs + preferences);
			c.setSensitiveToObstacles(false);
			environment.putAgent(c, 10, 10);
		}
	}

	private void prepareCriteria() {
		// Adding criteria
		ObesitySimulationCore.criteria.add(new Criterion("domaine"));
		ObesitySimulationCore.criteria.add(new Criterion("type"));
		ObesitySimulationCore.criteria.add(new Criterion("compagnie sociale"));
		ObesitySimulationCore.criteria.add(new Criterion("endroit"));

		// Adding subCriteria
		ObesitySimulationCore.subCriteria.add(new SubCriterion(
				ObesitySimulationCore.criteria.get(0), "formel"));
		ObesitySimulationCore.subCriteria.add(new SubCriterion(
				ObesitySimulationCore.criteria.get(0), "informel"));

		ObesitySimulationCore.subCriteria.add(new SubCriterion(
				ObesitySimulationCore.criteria.get(1), "loisir"));
		ObesitySimulationCore.subCriteria.add(new SubCriterion(
				ObesitySimulationCore.criteria.get(1), "physiquement active"));
		ObesitySimulationCore.subCriteria.add(new SubCriterion(
				ObesitySimulationCore.criteria.get(1), "sociale"));
		ObesitySimulationCore.subCriteria.add(new SubCriterion(
				ObesitySimulationCore.criteria.get(1), "competences"));
		ObesitySimulationCore.subCriteria.add(new SubCriterion(
				ObesitySimulationCore.criteria.get(1), "auto-amelioration"));

		ObesitySimulationCore.subCriteria.add(new SubCriterion(
				ObesitySimulationCore.criteria.get(2), "seul"));
		ObesitySimulationCore.subCriteria.add(new SubCriterion(
				ObesitySimulationCore.criteria.get(2), "famille"));
		ObesitySimulationCore.subCriteria.add(new SubCriterion(
				ObesitySimulationCore.criteria.get(2), "autre parents"));
		ObesitySimulationCore.subCriteria.add(new SubCriterion(
				ObesitySimulationCore.criteria.get(2), "amis"));
		ObesitySimulationCore.subCriteria.add(new SubCriterion(
				ObesitySimulationCore.criteria.get(2), "plusieurs types"));

		ObesitySimulationCore.subCriteria.add(new SubCriterion(
				ObesitySimulationCore.criteria.get(3), "maison"));
		ObesitySimulationCore.subCriteria.add(new SubCriterion(
				ObesitySimulationCore.criteria.get(3), "famille"));
		ObesitySimulationCore.subCriteria.add(new SubCriterion(
				ObesitySimulationCore.criteria.get(3), "voisinage"));
		ObesitySimulationCore.subCriteria.add(new SubCriterion(
				ObesitySimulationCore.criteria.get(3), "ecole"));
		ObesitySimulationCore.subCriteria.add(new SubCriterion(
				ObesitySimulationCore.criteria.get(3), "communaute"));
		ObesitySimulationCore.subCriteria.add(new SubCriterion(
				ObesitySimulationCore.criteria.get(3),
				"au-dela de la communite"));
	}

	public ArrayList<Preference> getRandomSetOfPreferences() {
		ArrayList<Preference> prefs = new ArrayList<Preference>();
		for (int i = 0; i < ObesitySimulationCore.subCriteria.size(); i++)
			prefs.add(new Preference(ObesitySimulationCore.subCriteria.get(i),
					(new Random()).nextInt(3) + 1));
		return prefs;
	}

	public ArrayList<Activity> getAllActivitiesInEnv() {
		ArrayList<Activity> list = new ArrayList<Activity>();
		Iterator<Agent> i = environment.getAllAgents().iterator();
		while (i.hasNext()) {
			Agent a = (Agent) i.next();
			if (a instanceof Activity)
				list.add((Activity) a);
		}
		return list;
	}

	public Activity getActivityInEnvByCategoryAndIndex(
			@SuppressWarnings("rawtypes") Class c, int index) {
		ArrayList<Activity> list = new ArrayList<Activity>();
		Iterator<Activity> i = this.allActivities.iterator();
		while (i.hasNext()) {
			Activity a = i.next();
			if (a.getClass() == c)
				list.add((Activity) a);
		}
		if (list.size() != 0)
			return list.get(Math.min(index, list.size() - 1));
		return null;
	}

	public ArrayList<Activity> getAllActivitiesInEnvByCategory(
			@SuppressWarnings("rawtypes") Class c) {
		ArrayList<Activity> list = getAllActivitiesInEnv();
		ArrayList<Activity> list2 = new ArrayList<Activity>();
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).getClass() == c)
				list2.add(list.get(i));
		return list2;
	}

	private void prepareOutputs() {
		String sysMS = "" + System.currentTimeMillis();

		if (ObesitySimulationCore.OUTPUT_DB_GENERAL) {
			Output.prepareDB(sysMS);
		}
	}

	private ArrayList<Characteristic> defaultCharacteristics() {
		ArrayList<Characteristic> characteristics = new ArrayList<Characteristic>();
		for (int i = 0; i < ObesitySimulationCore.subCriteria.size(); i++)
			characteristics.add(new Characteristic(
					ObesitySimulationCore.subCriteria.get(i), false));
		return characteristics;
	}

	private void createRandomXYPS() {
		ArrayList<IntervalConstraint> yearIntervals = new ArrayList<IntervalConstraint>();
		ArrayList<IntervalConstraint> hourIntervals = new ArrayList<IntervalConstraint>();
		hourIntervals.add(new IntervalConstraint(0, 1));
		ArrayList<Integer> dayConstrains = new ArrayList<Integer>();
		dayConstrains.add(1);
		dayConstrains.add(2);
		dayConstrains.add(3);
		dayConstrains.add(4);
		dayConstrains.add(5);
		ArrayList<Characteristic> characteristics = this
				.defaultCharacteristics();

		for (int i = 0; i < this.NB_PRIMARY_SCHOOLS; i++) {
			PrimarySchool s = new PrimarySchool(yearIntervals, dayConstrains,
					hourIntervals, characteristics);
			double x = SimulationProperties.random()
					* (environment.getWidth() - 1);
			double y = SimulationProperties.random()
					* (environment.getHeight() - 1);
			x = Math.floor(x) + 0.5;
			y = Math.floor(y) + 0.5;

			s.setSensitiveToObstacles(false);
			if (environment.canBePutAt(s, x, y)) {
				environment.putAgent(s, x, y);
			} else {
				throw new RuntimeException("An unexpected error occured");
			}
		}
	}

	private void createRandomXYCS() {
		for (int i = 0; i < this.NB_COLLEGE_SCHOOLS; i++) {
			ArrayList<IntervalConstraint> yearIntervals = new ArrayList<IntervalConstraint>();
			ArrayList<IntervalConstraint> hourIntervals = new ArrayList<IntervalConstraint>();
			hourIntervals.add(new IntervalConstraint(0, 1));
			ArrayList<Integer> dayConstrains = new ArrayList<Integer>();
			dayConstrains.add(1);
			dayConstrains.add(2);
			dayConstrains.add(3);
			dayConstrains.add(4);
			dayConstrains.add(5);
			ArrayList<Characteristic> characteristics = this
					.defaultCharacteristics();
			CollegeSchool s = new CollegeSchool(yearIntervals, dayConstrains,
					hourIntervals, characteristics);
			double x = SimulationProperties.random()
					* (environment.getWidth() - 1);
			double y = SimulationProperties.random()
					* (environment.getHeight() - 1);
			// 0.5 because the agent is put at the center
			x = Math.floor(x) + 0.5;
			y = Math.floor(y) + 0.5;

			s.setSensitiveToObstacles(false);
			if (environment.canBePutAt(s, x, y)) {
				environment.putAgent(s, x, y);
			} else {
				throw new RuntimeException("An unexpected error occured");
			}
		}
	}

	private void createRandomXYSS() {
		for (int i = 0; i < this.NB_SECONDARY_SCHOOLS; i++) {
			ArrayList<IntervalConstraint> yearIntervals = new ArrayList<IntervalConstraint>();
			ArrayList<IntervalConstraint> hourIntervals = new ArrayList<IntervalConstraint>();
			hourIntervals.add(new IntervalConstraint(0, 1));
			ArrayList<Integer> dayConstrains = new ArrayList<Integer>();
			dayConstrains.add(1);
			dayConstrains.add(2);
			dayConstrains.add(3);
			dayConstrains.add(4);
			dayConstrains.add(5);
			ArrayList<Characteristic> characteristics = this
					.defaultCharacteristics();
			SecondarySchool s = new SecondarySchool(yearIntervals,
					dayConstrains, hourIntervals, characteristics);
			double x = SimulationProperties.random()
					* (environment.getWidth() - 1);
			double y = SimulationProperties.random()
					* (environment.getHeight() - 1);
			// 0.5 because the agent is put at the center
			x = Math.floor(x) + 0.5;
			y = Math.floor(y) + 0.5;

			s.setSensitiveToObstacles(false);
			if (environment.canBePutAt(s, x, y)) {
				environment.putAgent(s, x, y);
			} else {
				throw new RuntimeException("An unexpected error occured");
			}
		}
	}

	private void createRandomXYHouses() {
		ArrayList<IntervalConstraint> hourIntervals = new ArrayList<IntervalConstraint>();
		hourIntervals.add(new IntervalConstraint(2, 3));
		//hourIntervals.add(new IntervalConstraint(4, 5));
		ArrayList<Integer> dayConstrains = new ArrayList<Integer>();
		dayConstrains.add(1);
		dayConstrains.add(2);
		dayConstrains.add(3);
		dayConstrains.add(4);
		dayConstrains.add(5);
		ArrayList<Characteristic> characteristics = this
				.defaultCharacteristics();
		characteristics.get(1).c = true;
		characteristics.get(2).c = true;
		characteristics.get(7).c = true;
		characteristics.get(8).c = true;
		characteristics.get(12).c = true;

		for (int i = 0; i < this.NB_HOMES; i++) {
			Home h = new Home(null, dayConstrains, hourIntervals,
					characteristics);
			double x = SimulationProperties.random()
					* (environment.getWidth() - 1);
			double y = SimulationProperties.random()
					* (environment.getHeight() - 1);
			// 0.5 because the agent is put at the center
			x = Math.floor(x) + 0.5;
			y = Math.floor(y) + 0.5;

			h.setSensitiveToObstacles(false);
			if (environment.canBePutAt(h, x, y)) {
				environment.putAgent(h, x, y);
			} else {
				throw new RuntimeException("An unexpected error occured");
			}
		}
	}

	private void createRandomXYParks() {
		ArrayList<IntervalConstraint> hourIntervals = new ArrayList<IntervalConstraint>();
		hourIntervals.add(new IntervalConstraint(2, 3));
		ArrayList<Integer> dayConstrains = new ArrayList<Integer>();
		dayConstrains.add(2);
		dayConstrains.add(3);
		dayConstrains.add(5);
		ArrayList<Characteristic> characteristics = this
				.defaultCharacteristics();
		characteristics.get(1).c = true;
		characteristics.get(3).c = true;
		characteristics.get(7).c = true;
		characteristics.get(8).c = true;
		characteristics.get(10).c = true;
		characteristics.get(14).c = true;

		for (int i = 0; i < this.NB_PARKS; i++) {
			Park p = new Park(null, dayConstrains, hourIntervals,
					characteristics);
			double x = SimulationProperties.random()
					* (environment.getWidth() - 1);
			double y = SimulationProperties.random()
					* (environment.getHeight() - 1);
			// 0.5 because the agent is put at the center
			x = Math.floor(x) + 0.5;
			y = Math.floor(y) + 0.5;

			p.setSensitiveToObstacles(false);
			if (environment.canBePutAt(p, x, y)) {
				environment.putAgent(p, x, y);
			} else {
				throw new RuntimeException("An unexpected error occured");
			}
		}
	}

	private void createRandomXYCulturalClubs() {
		ArrayList<IntervalConstraint> hourIntervals = new ArrayList<IntervalConstraint>();
		hourIntervals.add(new IntervalConstraint(2, 3));
		ArrayList<Integer> dayConstrains = new ArrayList<Integer>();
		dayConstrains.add(3);
		dayConstrains.add(5);
		ArrayList<Characteristic> characteristics = this
				.defaultCharacteristics();
		characteristics.get(0).c = true;
		characteristics.get(4).c = true;
		characteristics.get(10).c = true;
		characteristics.get(11).c = true;
		characteristics.get(14).c = true;
		characteristics.get(16).c = true;

		for (int i = 0; i < this.NB_CULTURALCLUBS; i++) {
			CulturalClub cc = new CulturalClub(null, dayConstrains,
					hourIntervals, characteristics);
			double x = SimulationProperties.random()
					* (environment.getWidth() - 1);
			double y = SimulationProperties.random()
					* (environment.getHeight() - 1);
			// 0.5 because the agent is put at the center
			x = Math.floor(x) + 0.5;
			y = Math.floor(y) + 0.5;

			cc.setSensitiveToObstacles(false);
			if (environment.canBePutAt(cc, x, y)) {
				environment.putAgent(cc, x, y);
			} else {
				throw new RuntimeException("An unexpected error occured");
			}
		}
	}

	private void createRandomXYSportClubs() {
		ArrayList<IntervalConstraint> hourIntervals = new ArrayList<IntervalConstraint>();
		hourIntervals.add(new IntervalConstraint(2, 3));
		ArrayList<Integer> dayConstrains = new ArrayList<Integer>();
		dayConstrains.add(1);
		dayConstrains.add(3);
		dayConstrains.add(5);
		ArrayList<Characteristic> characteristics = this
				.defaultCharacteristics();
		characteristics.get(0).c = true;
		characteristics.get(5).c = true;
		characteristics.get(10).c = true;
		characteristics.get(11).c = true;
		characteristics.get(14).c = true;
		characteristics.get(16).c = true;

		for (int i = 0; i < this.NB_SPORTCLUBS; i++) {
			SportClub sc = new SportClub(null, dayConstrains, hourIntervals,
					characteristics);
			double x = SimulationProperties.random()
					* (environment.getWidth() - 1);
			double y = SimulationProperties.random()
					* (environment.getHeight() - 1);
			// 0.5 because the agent is put at the center
			x = Math.floor(x) + 0.5;
			y = Math.floor(y) + 0.5;

			sc.setSensitiveToObstacles(false);
			if (environment.canBePutAt(sc, x, y)) {
				environment.putAgent(sc, x, y);
			} else {
				throw new RuntimeException("An unexpected error occured");
			}
		}
	}

	private class InitializationScenario {
		int age = 0;
		int gender = -1;
		int e_motivPA = 0;
		String statusBMI = "";
		ArrayList<Activity> percActivities = new ArrayList<Activity>();
	}

	private class Family {
		int nbChildren = 0;
		Home home = null;

		public Family(Home home, int nbChildren) {
			super();
			this.home = home;
			this.nbChildren = nbChildren;
		}
	}

	public void prepareChildrenParameters() {
		for (int i = 0; i < ObesitySimulationCore.NB_CHILDREN; i++)
			this.childrenParameters.add(new InitializationScenario());
		// Initializing Age :
		for (int i = 0; i < this.NB_CHILDREN_AGE_FROM_6_TO_10; i++)
			this.childrenParameters.get(i).age = 9 + (new Random()).nextInt(5);
		// (new Random()).nextInt(5) + 6;// ***************
		for (int i = this.NB_CHILDREN_AGE_FROM_6_TO_10; i < this.NB_CHILDREN_AGE_FROM_6_TO_10
				+ this.NB_CHILDREN_AGE_FROM_11_TO_14; i++)
			this.childrenParameters.get(i).age = (new Random()).nextInt(2) + 12;//******
		for (int i = this.NB_CHILDREN_AGE_FROM_6_TO_10
				+ this.NB_CHILDREN_AGE_FROM_11_TO_14; i < this.NB_CHILDREN_AGE_FROM_6_TO_10
				+ this.NB_CHILDREN_AGE_FROM_11_TO_14
				+ this.NB_CHILDREN_AGE_FROM_15_TO_17; i++)
			this.childrenParameters.get(i).age = (new Random()).nextInt(3) + 15;

		// Initializing Gender :
		for (int i = 0; i < ObesitySimulationCore.NB_CHILDREN; i++)
			this.childrenParameters.get(i).gender = this.GENDER;
		// (new Random()).nextInt(2) + 1;// *************

		// Initializing BMIstatus AND dMVPA :
		ArrayList<String> bmiStatuses = new ArrayList<String>();
		for (int i = 0; i < this.NB_UNDERWEIGHT; i++)
			bmiStatuses.add("underweight");
		for (int i = this.NB_UNDERWEIGHT; i < this.NB_UNDERWEIGHT
				+ this.NB_NORMAL; i++)
			bmiStatuses.add("normal");
		for (int i = this.NB_UNDERWEIGHT + this.NB_NORMAL; i < this.NB_UNDERWEIGHT
				+ this.NB_NORMAL + this.NB_OVERWEIGHT; i++)
			bmiStatuses.add("overweight");
		for (int i = this.NB_UNDERWEIGHT + this.NB_NORMAL + this.NB_OVERWEIGHT; i < this.NB_UNDERWEIGHT
				+ this.NB_NORMAL + this.NB_OVERWEIGHT + this.NB_OBESE; i++)
			bmiStatuses.add("obese");

		for (int i = 0; i < ObesitySimulationCore.NB_CHILDREN; i++) {
			String bmiStatus = null;
			while (bmiStatus == null) {
				int j = (new Random()).nextInt(NB_CHILDREN);
				bmiStatus = bmiStatuses.get(j);
				bmiStatuses.set(j, null);
			}
			this.childrenParameters.get(i).statusBMI = bmiStatus;
			if (bmiStatus == "underweight")
				this.childrenParameters.get(i).e_motivPA = this.E_MOTIV_PA_UW;
			else if (bmiStatus == "normal")
				this.childrenParameters.get(i).e_motivPA = this.E_MOTIV_PA_N;
			else if (bmiStatus == "overweight")
				this.childrenParameters.get(i).e_motivPA = this.E_MOTIV_PA_OW;
			else if (bmiStatus == "obese")
				this.childrenParameters.get(i).e_motivPA = this.E_MOTIV_PA_O;
		}

		// Preparing families
		ArrayList<Family> families = new ArrayList<Family>();
		ArrayList<Activity> homes = this
				.getAllActivitiesInEnvByCategory(Home.class);

		for (int i = 0; i < this.NB_FAMILIES_1CHILD; i++)
			families.add(new Family((Home) homes.get(i), 1));
		for (int i = this.NB_FAMILIES_1CHILD; i < this.NB_FAMILIES_1CHILD
				+ this.NB_FAMILIES_2CHILD; i++)
			families.add(new Family((Home) homes.get(i), 2));
		for (int i = this.NB_FAMILIES_1CHILD + this.NB_FAMILIES_2CHILD; i < this.NB_FAMILIES_1CHILD
				+ this.NB_FAMILIES_2CHILD + this.NB_FAMILIES_3CHILD; i++)
			families.add(new Family((Home) homes.get(i), 3));
		for (int i = this.NB_FAMILIES_1CHILD + this.NB_FAMILIES_2CHILD
				+ this.NB_FAMILIES_3CHILD; i < this.NB_FAMILIES_1CHILD
				+ this.NB_FAMILIES_2CHILD + this.NB_FAMILIES_3CHILD
				+ this.NB_FAMILIES_4CHILD; i++)
			families.add(new Family((Home) homes.get(i), 4));

		// Initializing homes
		for (int i = 0; i < ObesitySimulationCore.NB_CHILDREN; i++) {
			Home home = null;
			while (home == null) {
				int j = (new Random()).nextInt(NB_HOMES);
				if (families.get(j).nbChildren > 0) {
					home = families.get(j).home;
					families.get(j).nbChildren--;
				}
			}
			this.childrenParameters.get(i).percActivities.add(home);
		}

		// Preparing Schools
		ArrayList<Activity> primarySchools = this
				.getAllActivitiesInEnvByCategory(PrimarySchool.class);
		ArrayList<Activity> collegeSchools = this
				.getAllActivitiesInEnvByCategory(CollegeSchool.class);
		ArrayList<Activity> secondarySchools = this
				.getAllActivitiesInEnvByCategory(SecondarySchool.class);

		// Initializing schools
		for (int i = 0; i < ObesitySimulationCore.NB_CHILDREN; i++) {
			this.childrenParameters.get(i).percActivities.add(primarySchools
					.get((new Random()).nextInt(primarySchools.size())));
			this.childrenParameters.get(i).percActivities.add(collegeSchools
					.get((new Random()).nextInt(collegeSchools.size())));
			this.childrenParameters.get(i).percActivities.add(secondarySchools
					.get((new Random()).nextInt(secondarySchools.size())));
		}

		// Preparing and Initializing Parks
		// Children have 1/2 probability of perceiving Parks
		ArrayList<Activity> parks = this
				.getAllActivitiesInEnvByCategory(Park.class);
		if (parks.size() > 0)
			for (int i = 0; i < ObesitySimulationCore.NB_CHILDREN; i++)
				if ((new Random()).nextInt(2) == 0)
					this.childrenParameters.get(i).percActivities.add(parks
							.get((new Random()).nextInt(parks.size())));

		// Preparing and Initializing Clubs
		// Children have 1/2 probability of perceiving a Club
//		ArrayList<Activity> culturalClubs = this
//				.getAllActivitiesInEnvByCategory(CulturalClub.class);
		ArrayList<Activity> sportClubs = this
				.getAllActivitiesInEnvByCategory(SportClub.class);
		for (int i = 0; i < ObesitySimulationCore.NB_CHILDREN; i++)
			if ((new Random()).nextInt(2) == 0) {
//				int t = ((new Random()).nextInt(2));
//				if (t == 0)
//					this.childrenParameters.get(i).percActivities
//							.add(culturalClubs.get((new Random())
//									.nextInt(culturalClubs.size())));
//				else if (t == 1)
					this.childrenParameters.get(i).percActivities
							.add(sportClubs.get((new Random())
									.nextInt(sportClubs.size())));

			}
	}
}
