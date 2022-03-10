package v30.noncommdiseases.agent;

import java.util.ArrayList;

import v30.noncommdiseases.ObesitySimulationCore;
import v30.noncommdiseases.cognitive.Characteristic;
import v30.noncommdiseases.util.IntervalConstraint;

public class SecondarySchool extends School {

	public SecondarySchool(ArrayList<IntervalConstraint> yearPeriods,
			ArrayList<Integer> dayConstrains,
			ArrayList<IntervalConstraint> hourPeriods,
			ArrayList<Characteristic> characteristics) {
		super( yearPeriods, dayConstrains, hourPeriods, characteristics);
	}

	@Override
	public double getContribMVPA() {
		return ObesitySimulationCore.CONTRIB_MVPA_SS;
	}

}
