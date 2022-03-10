package v30.noncommdiseases.factors;

import java.util.ArrayList;

import v30.noncommdiseases.agent.Activity;
import v30.noncommdiseases.agent.Child;
import v30.noncommdiseases.util.DataSharedBetweenAgents;

public abstract class Factor {
	public abstract void updateN(Child child);
	public abstract void updateI(Child child);
	public abstract void updateESoc(ArrayList<DataSharedBetweenAgents> participantsData);
	public abstract void updateEPhy(Activity activity);
}
