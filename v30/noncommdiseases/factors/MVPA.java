package v30.noncommdiseases.factors;

import java.util.ArrayList;
import java.util.Random;

import v30.noncommdiseases.ObesitySimulationCore;
import v30.noncommdiseases.agent.Activity;
import v30.noncommdiseases.agent.Child;
import v30.noncommdiseases.factors.MVPA;
import v30.noncommdiseases.util.DataSharedBetweenAgents;

public class MVPA extends Factor {
	public double mvpa = 0;// v26
	public double mvpaActivity = 0; // v28
	public double basicMVPA = 0;

	// Percentage of possible margin around basicMVPA
	public static final double BETA_MAX = 2;
	public static final double BETA_MIN = 0.2;
	// Percentage of random dMVPA in updateI
	// public static final double PERC_I = 0;
	// Percentage of the effect of all activity-participants in updateESoc
	public static final double PERC_ESOC = 0.1;
	// Number of participants perceived a child during an activity
	public static final int N_SOC = 5;

	public double annualMVPA = 0;

	// v29 deleted: public double annualMVPAold = 0;
	// v29 deleted: public double e_motivPA = 0;
	//v30 :
	public double e_motivPA = 0;

	public MVPA(Gender gender, Age age, double e_motivPA) {
		this.updateBasicMVPA(gender, age);
		// v29 deleted: this.e_motivPA = e_motivPA;// v26
		//v30:  motivation 
		this.e_motivPA = 1+60/this.basicMVPA;
						//((new Random()).nextInt(201) - 100)/100; 
		
		// dMVPA=mvpa-this.basicMVPA;
		// v29 deleted: this.annualMVPAold = this.e_motivPA * this.basicMVPA;
	}

	@Override
	public void updateN(Child child) {
		this.updateBasicMVPA((Gender) child.factors.get("gender"),
				(Age) child.factors.get("age"));
	}

	@Override
	public void updateEPhy(Activity activity) {
		// v26 :
		// this.mvpa += activity.getContribMVPA() * this.basicMVPA
		// * this.e_motivPA;

		// v28 :
		// this.mvpaActivity = activity.getContribMVPA() * this.basicMVPA
		// * this.e_motivPA;

		// v29 (version2) :
		this.mvpaActivity = activity.getContribMVPA() * this.basicMVPA;
		if (!ObesitySimulationCore.COGNITIVE_REASONING) {
			this.mvpa = this.mvpaActivity;
		}
	}

	@Override
	public void updateESoc(ArrayList<DataSharedBetweenAgents> participantsData) {
		double sum = 0;
		if (participantsData.size() > 0) {
			for (int i = 0; i < Math.min(participantsData.size(), MVPA.N_SOC); i++)
				// v 26 :
				sum += participantsData.get((new Random())
						.nextInt(participantsData.size())).mvpa;
			this.mvpa = (1 - MVPA.PERC_ESOC) * this.mvpa + MVPA.PERC_ESOC * sum
					/ Math.min(participantsData.size(), MVPA.N_SOC);
		}
	}

	@Override
	public void updateI(Child child) {
		if (ObesitySimulationCore.COGNITIVE_REASONING) {
			// v28
			if (child.currentSatisfactionAPL != -1)
				this.mvpa += this.mvpaActivity * child.currentSatisfactionAPL;
			else
				this.mvpa += this.mvpaActivity;
			this.mvpaActivity = 0;

			if (this.mvpa < this.basicMVPA * MVPA.BETA_MIN)
				this.mvpa = this.basicMVPA * MVPA.BETA_MIN;
			else if (this.mvpa > this.basicMVPA * MVPA.BETA_MAX)
				this.mvpa = this.basicMVPA * MVPA.BETA_MAX;
		}
	}

	public void updateBasicMVPA(Gender gender, Age age) {
		basicMVPA = MVPA.getBasicMeanMVPAByAgeAndGender(gender, age);
	}

	public void addToAnnualMVPA(Child child) {
		if (((Age) child.factors.get("age")).year == ((Age) child.factors
				.get("age")).yearAtInitialization) {
			this.annualMVPA += this.mvpa
					/ (ObesitySimulationCore.SCHOOL_DAYS_PER_YEAR
							- ((Age) child.factors.get("age")).dayAtInitialization + 1);
		} else {
			this.annualMVPA += this.mvpa
					/ ObesitySimulationCore.SCHOOL_DAYS_PER_YEAR;
		}
		this.mvpa = 0;
	}

	public double getAverageAnnualMVPA() {
		return this.annualMVPA;// old;
	}

	public void resetAverageAnnualMVPA(Child child) {
		// v29 deleted: this.annualMVPAold = this.annualMVPA;
		this.annualMVPA = 0;
		// v29 deleted: this.resetE_motivationPA();
	}

	// v29 deleted:
	// public void resetE_motivationPA() {
	// this.e_motivPA = this.annualMVPAold / this.basicMVPA;
	// }

	public static double getBasicMeanMVPAByAgeAndGender(Gender gender, Age age) {
		// Values are correct in age [2-18] from the book [Guinhouya, 2013]
		if (gender.value == 1) {
			// boy
			switch (age.year) {
			case 2:
				return 70;
			case 3:
				return 78;
			case 4:
				return 88;
			case 5:
				return 84;
			case 6:
				return 82;
			case 7:
				return 78;
			case 8:
				return 75;
			case 9:
				return 72;
			case 10:
				return 69;
			case 11:
				return 65;
			case 12:
				return 62;
			case 13:
				return 55;
			case 14:
				return 49;
			case 15:
				return 41;
			case 16:
				return 36;
			case 17:
				return 31;
			case 18:
				return 27;
			}
		} else {
			// Girl
			switch (age.year) {
			case 2:
				return 64;
			case 3:
				return 75;
			case 4:
				return 84;
			case 5:
				return 80;
			case 6:
				return 78;
			case 7:
				return 75;
			case 8:
				return 72;
			case 9:
				return 60;
			case 10:
				return 50;
			case 11:
				return 48;
			case 12:
				return 44;
			case 13:
				return 41;
			case 14:
				return 39;
			case 15:
				return 38;
			case 16:
				return 36;
			case 17:
				return 30;
			case 18:
				return 25;
			}
		}
		return 21;// >18ans
	}

	public static double getEffectMVPAonBMI(Gender gender, Age age,
			String BMIterm) {
		// Weighted average (from 'Inputs V1.xlsx':BOYS - Moyenne ponderee
		if (gender.value == 1) {
			// boy
			if (BMIterm == "underweight")
				switch (age.year) {
				case 2:
					return -0.0505;
				case 3:
					return -0.0505;
				case 4:
					return -0.0505;
				case 5:
					return -0.0229;
				case 6:
					return -0.0282;
				case 7:
					return -0.0140;
				case 8:
					return -0.0073;
				case 9:
					return -0.0105;
				case 10:
					return -0.0113;
				case 11:
					return -0.0154;
				case 12:
					return -0.0044;
				case 13:
					return -0.0147;
				case 14:
					return 0.0005;
				case 15:
					return 0.0006;
				case 16:
					return 0.0407;
				case 17:
					return -0.0183;
				case 18:
					return 0.0127;
				}
			else if (BMIterm == "normal")
				switch (age.year) {
				case 2:
					return -0.0505;
				case 3:
					return -0.0505;
				case 4:
					return -0.0505;
				case 5:
					return -0.0229;
				case 6:
					return -0.0282;
				case 7:
					return -0.0121;
				case 8:
					return -0.0073;
				case 9:
					return -0.0063;
				case 10:
					return -0.0164;
				case 11:
					return -0.0139;
				case 12:
					return -0.0112;
				case 13:
					return 0.0006;
				case 14:
					return 0.0094;
				case 15:
					return 0.0151;
				case 16:
					return 0.0220;
				case 17:
					return 0.0070;
				case 18:
					return -0.0052;
				}
			else if (BMIterm == "overweight")
				switch (age.year) {
				case 2:
					return -0.0505;
				case 3:
					return -0.0505;
				case 4:
					return -0.0505;
				case 5:
					return -0.0229;
				case 6:
					return -0.0282;
				case 7:
					return -0.0112;
				case 8:
					return -0.0090;
				case 9:
					return -0.0135;
				case 10:
					return -0.0114;
				case 11:
					return -0.0154;
				case 12:
					return -0.0108;
				case 13:
					return -0.0033;
				case 14:
					return -0.0031;
				case 15:
					return 0.0215;
				case 16:
					return 0.0195;
				case 17:
					return -0.0161;
				case 18:
					return -0.0099;
				}
			else if (BMIterm == "obese")
				switch (age.year) {
				case 2:
					return -0.0505;
				case 3:
					return -0.0505;
				case 4:
					return -0.0505;
				case 5:
					return -0.0229;
				case 6:
					return -0.0282;
				case 7:
					return -0.0110;
				case 8:
					return -0.0095;
				case 9:
					return -0.0085;
				case 10:
					return -0.0125;
				case 11:
					return -0.0174;
				case 12:
					return -0.0135;
				case 13:
					return -0.0017;
				case 14:
					return 0.0065;
				case 15:
					return 0.0096;
				case 16:
					return 0.0078;
				case 17:
					return -0.0101;
				case 18:
					return -0.0081;
				}
		}
		// Girls
		else if (BMIterm == "underweight")
			switch (age.year) {
			case 2:
				return -0.0505;
			case 3:
				return -0.0505;
			case 4:
				return -0.0505;
			case 5:
				return -0.0229;
			case 6:
				return -0.0282;
			case 7:
				return -0.0078;
			case 8:
				return -0.0068;
			case 9:
				return -0.0126;
			case 10:
				return -0.0098;
			case 11:
				return -0.0037;
			case 12:
				return -0.0020;
			case 13:
				return -0.0006;
			case 14:
				return 0.0013;
			case 15:
				return 0.0003;
			case 16:
				return -0.0017;
			case 17:
				return -0.0029;
			case 18:
				return -0.0067;
			}
		else if (BMIterm == "normal")
			switch (age.year) {
			case 2:
				return -0.0505;
			case 3:
				return -0.0505;
			case 4:
				return -0.0505;
			case 5:
				return -0.0229;
			case 6:
				return -0.0282;
			case 7:
				return -0.0078;
			case 8:
				return -0.0086;
			case 9:
				return -0.0136;
			case 10:
				return -0.0093;
			case 11:
				return -0.0060;
			case 12:
				return -0.0031;
			case 13:
				return -0.0043;
			case 14:
				return 0.0043;
			case 15:
				return -0.0070;
			case 16:
				return -0.0002;
			case 17:
				return -0.0074;
			case 18:
				return -0.0083;
			}
		else if (BMIterm == "overweight")
			switch (age.year) {
			case 2:
				return -0.0505;
			case 3:
				return -0.0505;
			case 4:
				return -0.0505;
			case 5:
				return -0.0229;
			case 6:
				return -0.0282;
			case 7:
				return -0.0722;
			case 8:
				return -0.0115;
			case 9:
				return -0.0097;
			case 10:
				return -0.0111;
			case 11:
				return -0.0090;
			case 12:
				return -0.0066;
			case 13:
				return 0.0129;
			case 14:
				return -0.0038;
			case 15:
				return -0.0248;
			case 16:
				return 0.0003;
			case 17:
				return -0.0098;
			case 18:
				return -0.0091;
			}
		else if (BMIterm == "obese")
			switch (age.year) {
			case 2:
				return -0.0505;
			case 3:
				return -0.0505;
			case 4:
				return -0.0505;
			case 5:
				return -0.0229;
			case 6:
				return -0.0282;
			case 7:
				return -0.0066;
			case 8:
				return -0.0118;
			case 9:
				return -0.0088;
			case 10:
				return -0.0115;
			case 11:
				return -0.0094;
			case 12:
				return -0.0097;
			case 13:
				return -0.0043;
			case 14:
				return -0.0038;
			case 15:
				return -0.0008;
			case 16:
				return -0.0059;
			case 17:
				return -0.0041;
			case 18:
				return -0.0098;
			}
		return -0.005; // >18ans
	}
}
