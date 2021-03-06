package mtanalysis.domains.intervals;

import mtanalysis.domains.nonrel.AbstractValue;

public class IntervalsAbstValue implements AbstractValue {

	public static double inf = Double.MAX_VALUE;

	private double leftLimit;
	private double rightLimit;

	public IntervalsAbstValue() {
		leftLimit = 1.0;
		rightLimit = -1.0;
	}

	public boolean isBottom() {
		if (leftLimit > rightLimit)
			return true;
		return false;
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

	@Override
	public IntervalsAbstValue glb(AbstractValue a) {
		// TODO : check!
		IntervalsAbstValue b = (IntervalsAbstValue) a;

		if (this.leftLimit > this.rightLimit)
			return this;
		else if (b.leftLimit > b.rightLimit)
			return b;
		else {
			double l = Math.max(this.getLeftLimit(), b.getLeftLimit());
			double r = Math.min(this.getRightLimit(), b.getRightLimit());
			return new IntervalsAbstValue(l, r);
		}

	}

	public double getLeftLimit() {
		return leftLimit;
	}

	public double getRightLimit() {
		return rightLimit;
	}

	@Override
	public String toString() {
		String str = "[";
		if (leftLimit <= -inf + 1e9)
			str += "-INF";
		else
			str += leftLimit;
		str += ",";
		if (rightLimit >= inf - 1e9)
			str += "INF";
		else
			str += rightLimit;
		str += "]";
		return str;
	}

	@Override
	public String getDesc() {
		return "Intervals abstract value";
	}

	@Override
	public AbstractValue widen(AbstractValue a) {
		IntervalsAbstValue interVal_a = (IntervalsAbstValue) a;
		double r = interVal_a.rightLimit > this.rightLimit ? inf : this.rightLimit;
		double l = interVal_a.leftLimit < this.leftLimit ? -inf : this.leftLimit;

		return new IntervalsAbstValue(l, r);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IntervalsAbstValue){
			IntervalsAbstValue iobj = (IntervalsAbstValue)obj;
			return this.leftLimit == iobj.leftLimit && this.rightLimit == iobj.rightLimit;
		}
		return super.equals(obj);
	}
}
