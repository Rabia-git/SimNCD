package v30.noncommdiseases.factors;

import java.util.ArrayList;
import java.util.Random;

import v30.noncommdiseases.agent.Activity;
import v30.noncommdiseases.agent.Child;
import v30.noncommdiseases.util.DataSharedBetweenAgents;

public class Gender extends Factor {

	// values : 1=Boy / 2=Girl
	public int value = 1;

	public Gender() {
		randomInitialize();
	}

	public Gender(int value) {
		this.value = value;
	}

	private void randomInitialize() {
		value = (new Random()).nextInt(2) + 1;
	}

	public String toString() {
		if (this.value == 1)
			return "Boy";
		else if (this.value == 2)
			return "Girl";
		return "Not Set";
	} 

	@Override
	public void updateN(Child child) {
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
