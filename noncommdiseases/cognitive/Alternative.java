package v30.noncommdiseases.cognitive;

import java.util.ArrayList;

import v30.noncommdiseases.agent.Activity;

public class Alternative {
	public Activity activity;
	public ArrayList<EvaluationSet> evaluationSets=new ArrayList<EvaluationSet>();
	
	public Alternative(Activity activity, ArrayList<EvaluationSet> evaluationSets) {
		this.activity = activity;
		this.evaluationSets = evaluationSets;
	}	
	
	public Alternative(Activity activity) {
		this.activity = activity;
	}	
}
