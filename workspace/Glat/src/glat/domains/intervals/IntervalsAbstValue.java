package glat.domains.intervals;

import glat.domains.nonrel.AbstractValue;

public class IntervalsAbstValue implements AbstractValue {

	private double leftLimit;
	private double rightLimit;

	public IntervalsAbstValue() {
		leftLimit = 1.0;
		rightLimit = -1.0;
	}

	public IntervalsAbstValue(double leftLimit, double rightLimit) {
		this.leftLimit = leftLimit;
		this.rightLimit = rightLimit;
	}

	public boolean lte(AbstractValue a) {

		IntervalsAbstValue b = (IntervalsAbstValue) a;

		return (b.leftLimit <= this.leftLimit && b.rightLimit >= this.rightLimit) || this.leftLimit > this.rightLimit;
	}

	@Override
	public IntervalsAbstValue lub(AbstractValue a) {
		IntervalsAbstValue b = (IntervalsAbstValue) a;

		if (this.leftLimit > this.rightLimit)
			return b;
		else if (b.leftLimit > b.rightLimit)
			return this;
		else
			return new IntervalsAbstValue(Math.min(this.getLeftLimit(), b.getLeftLimit()),
					Math.max(this.getRightLimit(), b.getRightLimit()));
	}

	public double getLeftLimit() {
		return leftLimit;
	}

	public double getRightLimit() {
		return rightLimit;
	}

	@Override
	public String toString() {
		return "[" + leftLimit + "," + rightLimit + "]";
	}

	@Override
	public String getDesc() {
		return "Intervals abstract value";
	}
}
