package v30.noncommdiseases.util;

import v30.noncommdiseases.agent.Child;

public class DataSharedBetweenAgents {

	public Child child;
	public double mvpa = 0;// e_motivPA=0;//dMVPA = 0;

	public DataSharedBetweenAgents(Child child, double mvpa) {
		this.child = child;
		// this.dMVPA = dMVPA;
		// this.e_motivPA=e_motivPA;
		this.mvpa = mvpa;
	}
}
