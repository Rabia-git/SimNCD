package v30.noncommdiseases.factors;

import java.util.ArrayList;
import java.util.Random;

import v30.noncommdiseases.ObesitySimulationCore;
import v30.noncommdiseases.agent.Activity;
import v30.noncommdiseases.agent.Child;
import v30.noncommdiseases.util.DataSharedBetweenAgents;
import v30.noncommdiseases.util.Output;

public class BMI extends Factor {

	public double basicBMI = 0;
	public double dBMI = 0;
	public String term = "";

	@Override
	public void updateN(Child child) {
		if (((Age) child.factors.get("age")).mustUpdateBMI) {
			// System.out.println("updating BMI");
			// Calculate new additionalBMI based on the average 
			// addiionalMVPA in the last year
			this.dBMI += MVPA.getEffectMVPAonBMI(
					((Gender) child.factors.get("gender")),
					((Age) child.factors.get("age")),
					((BMI) child.factors.get("bmi")).term)
					* ((((MVPA) child.factors.get("mvpa"))
							.getAverageAnnualMVPA() - ((MVPA) child.factors
							.get("mvpa")).basicMVPA));

			// calculate basicBMI of the new age based on the same weight status
			// of the last year
			this.basicBMI = getBasicBMIByGenderAndAge(
					(Gender) child.factors.get("gender"),
					(Age) child.factors.get("age"), this.term);

			// Re adjusting BMI values :
			double totalBMI = this.getBMI();

			this.basicBMI = getBasicBMIByGenderAndAge(
					(Gender) child.factors.get("gender"),
					(Age) child.factors.get("age"), totalBMI);

			this.dBMI = totalBMI - this.basicBMI;

			if (ObesitySimulationCore.OUTPUT_DB_GENERAL)
				Output.insertToDB_ChildData_Annual(child);

			// Reset Averages
			((MVPA) child.factors.get("mvpa")).resetAverageAnnualMVPA(child);
			child.resetYearlyData();
			((Age) child.factors.get("age")).mustUpdateBMI = false;
		}
	}

	@Override
	public void updateI(Child child) {
	}

	@Override
	public void updateESoc(ArrayList<DataSharedBetweenAgents> participantsData) {
	}

	@Override
	public void updateEPhy(Activity activity) {
	}

	/**
	 * Initializes the BMI with a random value between [12, 22]
	 */
	public BMI(Gender gender, Age age) {
		// v30 
		double bmi = ((new Random()).nextInt(86)*1.0 + 149)/10; // double in 
											//21.1;
		this.basicBMI = this.getBasicBMIByGenderAndAge(gender, age, bmi);
		this.dBMI = bmi - this.basicBMI;
		this.setTerm(gender, age);
	}

	public BMI(Gender gender, Age age, String term) {
		//double totalBMI = BMI.getRandomBMIByTerm(gender, age, term);
		this.basicBMI = BMI.getBasicBMIByGenderAndAge(gender, age, term);
		this.term = term;
		if (this.term.equals("underweight"))
			this.dBMI = -1;
		else if (this.term.equals("normal"))
			this.dBMI = 0.8135;//0;
		else
			this.dBMI = 0.736;
	}

	private void setTerm(Gender gender, Age age) {
		double totalBMI = this.getBMI();
		this.term = BMI.getTermByBMI(gender, age, totalBMI);

		// updating the basic and add BMI in case the child changed his category
		this.basicBMI = this.getBasicBMIByGenderAndAge(gender, age, totalBMI);
		this.dBMI = totalBMI - this.basicBMI;
	}

	public static double getBasicBMIByGenderAndAge(Gender gender, Age age,
			String term) {
		// Values are correct in age [4-18] based on [WHO, 2007]

		if (term.equals("normal")) {
			// For normal, we consider the 50th centile of BMI from
			// [WHO, 2007]
			if (gender.value == 1) {
				// boy
				switch (age.year) {
				case 1:
					return 15.14;
				case 2:
					return 15.18;
				case 3:
					return 15.2;
				case 4:
					return 15.264;
				case 5:
					return 15.306;
				case 6:
					return 15.483;
				case 7:
					return 15.737;
				case 8:
					return 16.049;
				case 9:
					return 16.443;
				case 10:
					return 16.939;
				case 11:
					return 17.533;
				case 12:
					return 18.233;
				case 13:
					return 19.005;
				case 14:
					return 19.774;
				case 15:
					return 20.495;
				case 16:
					return 21.142;
				case 17:
					return 21.708;
				case 18:
					return 22.188;
				default:// >18ans
					return 24;
				}
			} else {
				// Girl
				switch (age.year) {
				case 1:
					return 15.15;
				case 2:
					return 15.18;
				case 3:
					return 15.21;
				case 4:
					return 15.244;
				case 5:
					return 15.27;
				case 6:
					return 15.404;
				case 7:
					return 15.681;
				case 8:
					return 16.096;
				case 9:
					return 16.613;
				case 10:
					return 17.246;
				case 11:
					return 17.997;
				case 12:
					return 18.801;
				case 13:
					return 19.565;
				case 14:
					return 20.212;
				case 15:
					return 20.701;
				case 16:
					return 21.037;
				case 17:
					return 21.26;
				case 18:
					return 21.427;
				default:// >18ans
					return 24;
				}
			}
		}

		else if (term.equals("underweight")) {
			// For underweight, we consider the 15th centile of BMI from
			// [WHO, 2007]
			if (gender.value == 1) {
				// boy
				switch (age.year) {
				case 1:
				case 2:
				case 3:
				case 4:
					return 14.03;
				case 5:
					return 14.042;
				case 6:
					return 14.166;
				case 7:
					return 14.35;
				case 8:
					return 14.579;
				case 9:
					return 14.875;
				case 10:
					return 15.26;
				case 11:
					return 15.734;
				case 12:
					return 16.303;
				case 13:
					return 16.941;
				case 14:
					return 17.58;
				case 15:
					return 18.176;
				case 16:
					return 18.707;
				case 17:
					return 19.163;
				case 18:
					return 19.539;
				default:// >18ans
					return 22;
				}
			} else {
				// Girl
				switch (age.year) {
				case 1:
				case 2:
				case 3:
				case 4:
					return 13.846;
				case 5:
					return 13.816;
				case 6:
					return 13.879;
				case 7:
					return 14.07;
				case 8:
					return 14.382;
				case 9:
					return 14.782;
				case 10:
					return 15.283;
				case 11:
					return 15.889;
				case 12:
					return 16.544;
				case 13:
					return 17.164;
				case 14:
					return 17.687;
				case 15:
					return 18.072;
				case 16:
					return 18.327;
				case 17:
					return 18.485;
				case 18:
					return 18.593;
				default:// >18ans
					return 21;
				}
			}
		}

		else if (term.equals("overweight")) {
			// For overweight, we consider the 85th centile of BMI from
			// [WHO, 2007]
			if (gender.value == 1) {
				// boy
				switch (age.year) {
				case 1:
				case 2:
				case 3:
				case 4:
					return 16.7;
				case 5:
					return 16.819;
				case 6:
					return 17.111;
				case 7:
					return 17.508;
				case 8:
					return 17.987;
				case 9:
					return 18.568;
				case 10:
					return 19.26;
				case 11:
					return 20.052;
				case 12:
					return 20.943;
				case 13:
					return 21.891;
				case 14:
					return 22.812;
				case 15:
					return 23.656;
				case 16:
					return 24.402;
				case 17:
					return 25.046;
				case 18:
					return 25.584;
				default:// >18ans
					return 27;
				}
			} else {
				// Girl
				switch (age.year) {
				case 1:
				case 2:
				case 3:
				case 4:
					return 16.936;
				case 5:
					return 17.083;
				case 6:
					return 17.367;
				case 7:
					return 17.817;
				case 8:
					return 18.422;
				case 9:
					return 19.137;
				case 10:
					return 19.973;
				case 11:
					return 20.929;
				case 12:
					return 21.931;
				case 13:
					return 22.868;
				case 14:
					return 23.656;
				case 15:
					return 24.247;
				case 16:
					return 24.651;
				case 17:
					return 24.917;
				case 18:
					return 25.113;
				default:// >18ans
					return 27;
				}
			}
		}

		else if (term.equals("obese")) {
			// For obese, we consider the 97th centile of BMI from
			// [WHO, 2007]
			if (gender.value == 1) {
				// boy
				switch (age.year) {
				case 1:
				case 2:
				case 3:
				case 4:
					return 18.052;
				case 5:
					return 18.291;
				case 6:
					return 18.756;
				case 7:
					return 19.371;
				case 8:
					return 20.112;
				case 9:
					return 20.985;
				case 10:
					return 21.977;
				case 11:
					return 23.05;
				case 12:
					return 24.182;
				case 13:
					return 25.315;
				case 14:
					return 26.352;
				case 15:
					return 27.258;
				case 16:
					return 28.021;
				case 17:
					return 28.648;
				case 18:
					return 29.14;
				default:// >18ans
					return 31;
				}
			} else {
				// Girl
				switch (age.year) {
				case 1:
				case 2:
				case 3:
				case 4:
					return 18.598;
				case 5:
					return 18.929;
				case 6:
					return 19.448;
				case 7:
					return 20.167;
				case 8:
					return 21.062;
				case 9:
					return 22.063;
				case 10:
					return 23.167;
				case 11:
					return 24.366;
				case 12:
					return 25.574;
				case 13:
					return 26.667;
				case 14:
					return 27.559;
				case 15:
					return 28.205;
				case 16:
					return 28.62;
				case 17:
					return 28.868;
				case 18:
					return 29.03;
				default:// >18ans
					return 31;
				}
			}
		}
		return 0;
	}

	public double getBasicBMIByGenderAndAge(Gender gender, Age age, Double bmi) {
		// Values are correct in age [4-18] based on [WHO, 2007]
		this.term = getTermByBMI(gender, age, bmi);
		return BMI.getBasicBMIByGenderAndAge(gender, age, term);
	}

	public static String getTermByBMI(Gender gender, Age age, Double basicBMI) {
		// From [WHO, 2007]: We consider the 15th, 50th, 85th and 97th
		// centiles as limits to the terms {underweight, normal, overweight,
		// obese} - The following values are only correct starting age 4
		if (gender.value == 1) {
			// boy
			switch (age.year) {
			case 1:
			case 2:
			case 3:
			case 4:
				if (basicBMI <= 14.03)
					return "underweight";
				// 16.7 is the min cut-off for overweight, so normal should be
				// < 16.7
				else if (basicBMI < 16.7)
					return "normal";
				else if (basicBMI < 18.052)
					return "overweight";
				else
					return "obese";
			case 5:
				if (basicBMI <= 14.042)
					return "underweight";
				else if (basicBMI < 16.819)
					return "normal";
				else if (basicBMI < 18.291)
					return "overweight";
				else
					return "obese";
			case 6:
				if (basicBMI <= 14.166)
					return "underweight";
				else if (basicBMI < 17.111)
					return "normal";
				else if (basicBMI < 18.756)
					return "overweight";
				else
					return "obese";
			case 7:
				if (basicBMI <= 14.35)
					return "underweight";
				else if (basicBMI < 17.508)
					return "normal";
				else if (basicBMI < 19.371)
					return "overweight";
				else
					return "obese";
			case 8:
				if (basicBMI <= 14.579)
					return "underweight";
				else if (basicBMI < 17.987)
					return "normal";
				else if (basicBMI < 20.112)
					return "overweight";
				else
					return "obese";
			case 9:
				if (basicBMI <= 14.875)
					return "underweight";
				else if (basicBMI < 18.568)
					return "normal";
				else if (basicBMI < 20.985)
					return "overweight";
				else
					return "obese";
			case 10:
				if (basicBMI <= 15.26)
					return "underweight";
				else if (basicBMI < 19.26)
					return "normal";
				else if (basicBMI < 21.977)
					return "overweight";
				else
					return "obese";
			case 11:
				if (basicBMI <= 15.734)
					return "underweight";
				else if (basicBMI < 20.052)
					return "normal";
				else if (basicBMI < 23.05)
					return "overweight";
				else
					return "obese";
			case 12:
				if (basicBMI <= 16.303)
					return "underweight";
				else if (basicBMI < 20.943)
					return "normal";
				else if (basicBMI < 24.182)
					return "overweight";
				else
					return "obese";
			case 13:
				if (basicBMI <= 16.941)
					return "underweight";
				else if (basicBMI < 21.891)
					return "normal";
				else if (basicBMI < 25.315)
					return "overweight";
				else
					return "obese";
			case 14:
				if (basicBMI <= 17.58)
					return "underweight";
				else if (basicBMI < 22.812)
					return "normal";
				else if (basicBMI < 26.352)
					return "overweight";
				else
					return "obese";
			case 15:
				if (basicBMI <= 18.176)
					return "underweight";
				else if (basicBMI < 23.656)
					return "normal";
				else if (basicBMI < 27.258)
					return "overweight";
				else
					return "obese";
			case 16:
				if (basicBMI <= 18.707)
					return "underweight";
				else if (basicBMI < 24.402)
					return "normal";
				else if (basicBMI < 28.021)
					return "overweight";
				else
					return "obese";
			case 17:
				if (basicBMI <= 19.163)
					return "underweight";
				else if (basicBMI < 25.046)
					return "normal";
				else if (basicBMI < 28.648)
					return "overweight";
				else
					return "obese";
			case 18:
				if (basicBMI <= 19.539)
					return "underweight";
				else if (basicBMI < 25.584)
					return "normal";
				else if (basicBMI < 29.14)
					return "overweight";
				else
					return "obese";
			}
		} else {
			// Girl
			switch (age.year) {
			case 1:
			case 2:
			case 3:
			case 4:
				if (basicBMI <= 13.846)
					return "underweight";
				else if (basicBMI < 16.936)
					return "normal";
				else if (basicBMI < 18.598)
					return "overweight";
				else
					return "obese";
			case 5:
				if (basicBMI <= 13.816)
					return "underweight";
				else if (basicBMI < 17.083)
					return "normal";
				else if (basicBMI < 18.929)
					return "overweight";
				else
					return "obese";
			case 6:
				if (basicBMI <= 13.879)
					return "underweight";
				else if (basicBMI < 17.367)
					return "normal";
				else if (basicBMI < 19.448)
					return "overweight";
				else
					return "obese";
			case 7:
				if (basicBMI <= 14.07)
					return "underweight";
				else if (basicBMI < 17.817)
					return "normal";
				else if (basicBMI < 20.167)
					return "overweight";
				else
					return "obese";
			case 8:
				if (basicBMI <= 14.382)
					return "underweight";
				else if (basicBMI < 18.422)
					return "normal";
				else if (basicBMI < 21.062)
					return "overweight";
				else
					return "obese";
			case 9:
				if (basicBMI <= 14.782)
					return "underweight";
				else if (basicBMI < 19.137)
					return "normal";
				else if (basicBMI < 22.063)
					return "overweight";
				else
					return "obese";
			case 10:
				if (basicBMI <= 15.283)
					return "underweight";
				else if (basicBMI < 19.973)
					return "normal";
				else if (basicBMI < 23.167)
					return "overweight";
				else
					return "obese";
			case 11:
				if (basicBMI <= 15.889)
					return "underweight";
				else if (basicBMI < 20.929)
					return "normal";
				else if (basicBMI < 24.366)
					return "overweight";
				else
					return "obese";
			case 12:
				if (basicBMI <= 16.544)
					return "underweight";
				else if (basicBMI < 21.931)
					return "normal";
				else if (basicBMI < 25.574)
					return "overweight";
				else
					return "obese";
			case 13:
				if (basicBMI <= 17.164)
					return "underweight";
				else if (basicBMI < 22.868)
					return "normal";
				else if (basicBMI < 26.667)
					return "overweight";
				else
					return "obese";
			case 14:
				if (basicBMI <= 17.687)
					return "underweight";
				else if (basicBMI < 23.656)
					return "normal";
				else if (basicBMI < 27.559)
					return "overweight";
				else
					return "obese";
			case 15:
				if (basicBMI <= 18.072)
					return "underweight";
				else if (basicBMI < 24.247)
					return "normal";
				else if (basicBMI < 28.205)
					return "overweight";
				else
					return "obese";
			case 16:
				if (basicBMI <= 18.327)
					return "underweight";
				else if (basicBMI < 24.651)
					return "normal";
				else if (basicBMI < 28.62)
					return "overweight";
				else
					return "obese";
			case 17:
				if (basicBMI <= 18.485)
					return "underweight";
				else if (basicBMI < 24.917)
					return "normal";
				else if (basicBMI < 28.868)
					return "overweight";
				else
					return "obese";
			case 18:
				if (basicBMI <= 18.593)
					return "underweight";
				else if (basicBMI < 25.113)
					return "normal";
				else if (basicBMI < 29.03)
					return "overweight";
				else
					return "obese";
			}
		}
		return "";
	}

	private static double getRandomBMIByTerm(Gender gender, Age age, String term) {
		// From [WHO, 2007]: We consider the 15th, 50th, 85th and 97th
		// centiles as limits to the terms {underweight, normal, overweight,
		// obese} - The following values are only correct starting age 4
		double d = 0;
		if (gender.value == 1) {
			// boy
			switch (age.year) {
			case 1:
			case 2:
			case 3:
			case 4:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(40) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(167 - 140) + 140);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(180 - 167) + 167);
				else
					d = ((new Random()).nextInt(250 - 180) + 180);
				break;
			case 5:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(40) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(168 - 140) + 140);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(182 - 168) + 168);
				else
					d = ((new Random()).nextInt(250 - 182) + 182);
				break;
			case 6:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(41) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(171 - 141) + 141);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(187 - 171) + 171);
				else
					d = ((new Random()).nextInt(250 - 187) + 187);
				break;
			case 7:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(43) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(175 - 143) + 143);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(193 - 175) + 175);
				else
					d = ((new Random()).nextInt(250 - 193) + 193);
				break;
			case 8:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(45) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(179 - 145) + 145);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(201 - 179) + 179);
				else
					d = ((new Random()).nextInt(250 - 201) + 201);
				break;
			case 9:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(48) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(185 - 148) + 148);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(209 - 185) + 185);
				else
					d = ((new Random()).nextInt(250 - 209) + 209);
				break;
			case 10:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(52) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(192 - 152) + 152);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(219 - 192) + 192);
				else
					d = ((new Random()).nextInt(250 - 219) + 219);
				break;
			case 11:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(57) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(200 - 157) + 157);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(230 - 200) + 200);
				else
					d = ((new Random()).nextInt(300 - 230) + 230);
				break;
			case 12:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(63) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(209 - 163) + 163);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(241 - 209) + 209);
				else
					d = ((new Random()).nextInt(300 - 241) + 241);
				break;
			case 13:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(69) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(218 - 169) + 169);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(253 - 218) + 218);
				else
					d = ((new Random()).nextInt(300 - 253) + 253);
				break;
			case 14:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(75) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(228 - 175) + 175);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(263 - 228) + 228);
				else
					d = ((new Random()).nextInt(300 - 263) + 263);
				break;
			case 15:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(81) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(236 - 181) + 181);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(272 - 236) + 236);
				else
					d = ((new Random()).nextInt(300 - 272) + 272);
				break;
			case 16:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(87) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(244 - 187) + 187);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(280 - 244) + 244);
				else
					d = ((new Random()).nextInt(300 - 280) + 280);
				break;
			case 17:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(91) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(250 - 191) + 191);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(286 - 250) + 250);
				else
					d = ((new Random()).nextInt(300 - 286) + 286);
				break;
			case 18:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(95) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(255 - 195) + 195);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(291 - 255) + 255);
				else
					d = ((new Random()).nextInt(300 - 291) + 291);
				break;
			}
		} else {
			// Girl
			switch (age.year) {
			case 1:
			case 2:
			case 3:
			case 4:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(38) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(169 - 138) + 138);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(185 - 169) + 169);
				else
					d = ((new Random()).nextInt(250 - 185) + 185);
				break;
			case 5:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(38) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(170 - 138) + 138);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(189 - 170) + 170);
				else
					d = ((new Random()).nextInt(250 - 189) + 189);
				break;
			case 6:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(38) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(173 - 138) + 138);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(194 - 173) + 173);
				else
					d = ((new Random()).nextInt(250 - 194) + 194);
				break;
			case 7:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(40) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(178 - 140) + 140);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(201 - 178) + 178);
				else
					d = ((new Random()).nextInt(250 - 201) + 201);
				break;
			case 8:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(43) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(184 - 143) + 143);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(210 - 184) + 184);
				else
					d = ((new Random()).nextInt(250 - 210) + 210);
				break;
			case 9:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(47) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(191 - 147) + 147);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(220 - 191) + 191);
				else
					d = ((new Random()).nextInt(250 - 220) + 220);
				break;
			case 10:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(52) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(199 - 152) + 152);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(231 - 199) + 199);
				else
					d = ((new Random()).nextInt(300 - 231) + 231);
				break;
			case 11:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(58) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(209 - 158) + 158);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(243 - 209) + 209);
				else
					d = ((new Random()).nextInt(300 - 243) + 243);
				break;
			case 12:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(65) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(219 - 165) + 165);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(255 - 219) + 219);
				else
					d = ((new Random()).nextInt(300 - 255) + 255);
				break;
			case 13:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(71) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(228 - 171) + 171);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(266 - 228) + 228);
				else
					d = ((new Random()).nextInt(300 - 266) + 266);
				break;
			case 14:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(76) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(236 - 176) + 176);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(275 - 236) + 236);
				else
					d = ((new Random()).nextInt(300 - 275) + 275);
				break;
			case 15:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(80) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(242 - 180) + 180);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(282 - 242) + 242);
				else
					d = ((new Random()).nextInt(300 - 282) + 282);
				break;
			case 16:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(83) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(246 - 183) + 183);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(286 - 246) + 246);
				else
					d = ((new Random()).nextInt(300 - 286) + 286);
				break;
			case 17:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(84) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(249 - 184) + 184);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(288 - 249) + 249);
				else
					d = ((new Random()).nextInt(300 - 288) + 288);
				break;
			case 18:
				if (term.equals("underweight"))
					d = ((new Random()).nextInt(85) + 100);
				else if (term.equals("normal"))
					d = ((new Random()).nextInt(251 - 185) + 185);
				else if (term.equals("overweight"))
					d = ((new Random()).nextInt(290 - 252) + 251);
				else
					d = ((new Random()).nextInt(300 - 290) + 290);
				break;
			}
		}
		return d / 10;
	}

	/**
	 * Independent of the evolution of MVPA
	 */
	public void updateYearlyBMIRandomly(Gender gender, Age age) {
		this.basicBMI = getBasicBMIByGenderAndAge(gender, age, this.getBMI());
		boolean t = false;
		do {
			this.dBMI = ((new Random()).nextInt((int) (basicBMI * 0.2) + 1))
					- (basicBMI * 0.2 + 1) / 2;
			if (((basicBMI + dBMI) >= basicBMI * 0.8)
					|| ((basicBMI + dBMI) <= (basicBMI * 1.2)))
				t = true;
		} while (!t);
		this.setTerm(gender, age);
	}

	public void updateYearlyBMIbyMVPAPercentage(Gender gender, Age age,
			double basicMVPA, double dMVPA) {
		this.basicBMI = getBasicBMIByGenderAndAge(gender, age, this.getBMI());
		this.dBMI = this.basicBMI * dMVPA / basicMVPA * (-1);
		this.setTerm(gender, age);
	}

	public double getBMI() {
		return this.basicBMI + this.dBMI;
	}

	public String toString() {
		return this.getBMI() + "";
	}
}
