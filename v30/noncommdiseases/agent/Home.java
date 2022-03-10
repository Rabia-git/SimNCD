package v30.noncommdiseases.agent;

import java.util.ArrayList;

import v30.noncommdiseases.ObesitySimulationCore;
import v30.noncommdiseases.cognitive.Characteristic;
import v30.noncommdiseases.util.IntervalConstraint;

public class Home extends Activity {

	public Home(ArrayList<IntervalConstraint> yearPeriods,
			ArrayList<Integer> dayConstrains,
			ArrayList<IntervalConstraint> hourPeriods,
			ArrayList<Characteristic> characteristics) {
		super(yearPeriods, dayConstrains, hourPeriods, characteristics);
	}

	@Override
	public double getContribMVPA() {
		//if (ObesitySimulationCore.hour <= 3)
			return ObesitySimulationCore.CONTRIB_MVPA_Home1;
		//else
		//	return ObesitySimulationCore.CONTRIB_MVPA_Home2;
	}
}
