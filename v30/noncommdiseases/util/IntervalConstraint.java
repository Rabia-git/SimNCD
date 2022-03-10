package v30.noncommdiseases.util;

public class IntervalConstraint {
	public double start = -1;
	public double ending = -1;

	public IntervalConstraint(double start, double ending) {
		this.start = start;
		this.ending = ending;
	}

	public double duration() {
		return this.ending - this.start;
	}
}
