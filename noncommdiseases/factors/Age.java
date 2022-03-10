package v30.noncommdiseases.factors;

import java.util.ArrayList;
import java.util.Random;

import v30.noncommdiseases.ObesitySimulationCore;
import v30.noncommdiseases.agent.Activity;
import v30.noncommdiseases.agent.Child;
import v30.noncommdiseases.agent.CollegeSchool;
import v30.noncommdiseases.agent.PrimarySchool;
import v30.noncommdiseases.agent.SecondarySchool;
import v30.noncommdiseases.util.DataSharedBetweenAgents;

public class Age extends Factor {

	private final int MAXIMUM_RANDOM_AGE = 14;
	public int year = 0, day = 0;
	private int tempSysDay = -1;
	public boolean mustUpdateBMI = false;
	public int yearAtInitialization = 0;
	public int dayAtInitialization = 0;

	public Age() {
		this.randomYear();
		this.yearAtInitialization = this.year;
		this.randomDay();
		this.dayAtInitialization = this.day;
		this.tempSysDay = ObesitySimulationCore.day;
	}

	public Age(int year) {
		this.year = year;
		this.yearAtInitialization = this.year;
		this.randomDay();
		this.dayAtInitialization = this.day;
		this.tempSysDay = ObesitySimulationCore.day;
	}

	public Age(int year, int day) {
		this.year = year;
		this.yearAtInitialization = this.year;
		this.day = day;
		this.dayAtInitialization = this.day;
		this.tempSysDay = ObesitySimulationCore.day;
	}

	public void randomYear() {
		// Generate a random year of birth (age will be between 2 to 14yrs)
		this.year = (new Random()).nextInt(MAXIMUM_RANDOM_AGE - 2) + 2;
	}

	public void randomDay() {
		this.day = (new Random())
				.nextInt(ObesitySimulationCore.SCHOOL_DAYS_PER_YEAR) + 1;
	}

	public String toString() {
		return this.year + "Y" + this.day + "D";
	}

	public void updateN() {

	}

	@Override
	public void updateN(Child child) {
		if (this.tempSysDay != ObesitySimulationCore.day) {
			this.day++;
			this.tempSysDay = ObesitySimulationCore.day;
		}
		if (this.day > ObesitySimulationCore.SCHOOL_DAYS_PER_YEAR) {
			this.year++;
			this.day = 1;
			this.mustUpdateBMI = true;

			// when turning 11, the primary school becomes a college
			if (this.year == 11)
				for (int i = 0; i < child.staticPlan.size(); i++)
					if (child.staticPlan.get(i) != null
							&& child.staticPlan.get(i).equals(
									PrimarySchool.class))
						child.staticPlan.set(i, CollegeSchool.class);

			// when turning 15, the primary school becomes a college
			if (this.year == 15)
				for (int i = 0; i < child.staticPlan.size(); i++)
					if (child.staticPlan.get(i) != null
							&& child.staticPlan.get(i).equals(
									CollegeSchool.class))
						child.staticPlan.set(i, SecondarySchool.class);

			System.out.println(child + "- AGE:" + this.year);
			if (year == 19)
				Child.toAdulthood++;
			if (Child.toAdulthood == ObesitySimulationCore.NB_CHILDREN)
				System.out.println(Child.toAdulthood
						+ " children grew into adulthood");

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
}
