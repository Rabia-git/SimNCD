package v30.noncommdiseases.cognitive;

import java.util.ArrayList;

public class EvaluationSet {
	public Criterion criterion;
	public ArrayList<Integer> lingTerms = new ArrayList<Integer>();
	
	public EvaluationSet(Criterion criterion, ArrayList<Integer> lingTerms) {
		this.criterion = criterion;
		this.lingTerms = lingTerms;
	}
	
	public EvaluationSet(Criterion criterion) {
		super();
		this.criterion = criterion;
	}
}
