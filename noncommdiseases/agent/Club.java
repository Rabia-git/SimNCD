package v30.noncommdiseases.agent;

import java.util.ArrayList;

import fr.lifl.jedi.model.Agent;

import v30.noncommdiseases.cognitive.Characteristic;
import v30.noncommdiseases.util.IntervalConstraint;

public abstract class Club extends Activity {

	// ArrayList<Agent> subsribedAgents = new ArrayList<Agent>();

	public Club( ArrayList<IntervalConstraint> yearPeriods,
			ArrayList<Integer> dayConstrains,
			ArrayList<IntervalConstraint> hourPeriods,
			ArrayList<Characteristic> characteristics) {
		super(yearPeriods, dayConstrains, hourPeriods, characteristics);
	}

	@Override
	public boolean checkEligibility(Agent a) {
		return super.checkEligibility(a) && this.isSubscribed(a);
	}

	private boolean isSubscribed(Agent a) {
		return true;
	}
}
