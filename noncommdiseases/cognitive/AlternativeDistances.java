package v30.noncommdiseases.cognitive;

import v30.noncommdiseases.agent.Activity;

public class AlternativeDistances {
	public Activity activity;
	public Double dPositive=0.0;
	public Double dNegative=0.0;
	public Double dRelativeProxmity=0.0;
	public AlternativeDistances(Activity activity, Double dPositive,
			Double dNegative, Double dRelativeProxmity) {
		this.activity = activity;
		this.dPositive = dPositive;
		this.dNegative = dNegative;
		this.dRelativeProxmity = dRelativeProxmity;
	}	
}
