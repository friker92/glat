package glat.domains.constprop;

import glat.domains.nonrel.AbstractValue;

public class ConsPropAbstValue implements AbstractValue {

	private double value;
	
	ConsPropAbstValue(double value) {
		this.value = value; 
	}
	
	@Override
	public String getDesc() {
		return "Constrant Prop Abstract Value";
	}

	@Override
	public AbstractValue lub(AbstractValue a) {
		
		if ( a instanceof ConsPropTOP ) {
			return a;
		} else if ( a instanceof ConsPropBOT ) {
			return this;
		} 
		
		
		if ( ((ConsPropAbstValue) a).value == this.value ) {
			return this;
		}
		
		return new ConsPropTOP();
	}

	@Override
	public boolean lte(AbstractValue a) {
		return (a instanceof ConsPropTOP) || (!(a instanceof ConsPropBOT) && ((ConsPropAbstValue) a).value == this.value);
	}

	public double getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return ""+value;
	}

	@Override
	public AbstractValue widen(AbstractValue a) {
		return this.lub(a);
	}
}
