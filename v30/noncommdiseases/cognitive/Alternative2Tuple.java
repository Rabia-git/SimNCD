package v30.noncommdiseases.cognitive;

import java.util.ArrayList;

import v30.noncommdiseases.agent.Activity;

public class Alternative2Tuple {
	public Activity activity;
	public ArrayList<Double> evaluations=new ArrayList<Double>();
	
	public Alternative2Tuple(Activity activity, ArrayList<Double> evaluations) {
		this.activity = activity;
		this.evaluations = evaluations;
	}	
	
	public Alternative2Tuple(Activity activity) {
		this.activity = activity;
	}	
}
