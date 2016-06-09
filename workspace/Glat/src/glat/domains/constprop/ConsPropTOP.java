package glat.domains.constprop;

import glat.domains.nonrel.AbstractValue;

public class ConsPropTOP extends ConsPropAbstValue {

	
	ConsPropTOP() {
	}
	
	@Override
	public String getDesc() {
		return "Constrant Prop Abstract Value (TOP)";
	}

	@Override
	public AbstractValue lub(AbstractValue a) {
		return this;
	}

	@Override
	public boolean lte(AbstractValue a) {
		return a instanceof ConsPropTOP;
	}
	
	@Override
	public String toString() {
		return "TOP";
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ConsPropTOP;
	}

	@Override
	public AbstractValue widen(AbstractValue a) {
		return this.lub(a);
	}
	
}
