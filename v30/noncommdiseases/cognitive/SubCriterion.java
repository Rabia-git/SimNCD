package v30.noncommdiseases.cognitive;

public class SubCriterion {
	public Criterion criterion;
	public String label="";
	
	public SubCriterion(Criterion criterion, String label) {
		this.criterion = criterion;
		this.label = label;
	}
	
	@Override
	public String toString() {
		return criterion.label+"."+this.label;
	}
}
