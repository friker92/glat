package glat.domain.intervals;

import glat.domain.AbstractValue;

public class IntervalsAbstValue implements AbstractValue {
	private Integer leftLimit;
	private Integer rightLimit;

	public IntervalsAbstValue() {
	}

	public IntervalsAbstValue(Integer leftLimit, Integer rightLimit) {
		this.leftLimit = leftLimit;
		this.rightLimit = rightLimit;
	}

	public boolean lte(AbstractValue a) {
		
		IntervalsAbstValue b = (IntervalsAbstValue) a;
		
		return ( ( b.leftLimit == null || (this.leftLimit != null && b.leftLimit <= this.leftLimit ) ) &&
				( b.rightLimit == null || (this.rightLimit != null && b.rightLimit >= this.rightLimit ) ));
	}

}
