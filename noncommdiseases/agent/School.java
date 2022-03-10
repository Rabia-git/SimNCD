package v30.noncommdiseases.agent;

import java.util.ArrayList;

import v30.noncommdiseases.cognitive.Characteristic;
import v30.noncommdiseases.util.IntervalConstraint;

public abstract class School extends Activity {

	public School(ArrayList<IntervalConstraint> yearPeriods,
			ArrayList<Integer> dayConstrains,
			ArrayList<IntervalConstraint> hourPeriods,
			ArrayList<Characteristic> characteristics) {
		super( yearPeriods, dayConstrains, hourPeriods, characteristics);
	}

}
