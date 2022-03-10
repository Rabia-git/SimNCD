package v30.noncommdiseases.cognitive;

public class Preference {
	public SubCriterion subCriterion;
	public Integer lingTerm;
	
	public Preference(SubCriterion subCriterion, Integer lingTerm) {
		this.subCriterion = subCriterion;
		this.lingTerm = lingTerm;
	}

	@Override
	public String toString() {
		return "[" + subCriterion + ", "+ lingTerm + "]";
	}
}
