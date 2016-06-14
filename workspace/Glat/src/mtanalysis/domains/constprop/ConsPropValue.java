package mtanalysis.domains.constprop;

import mtanalysis.domains.nonrel.AbstractValue;

public class ConsPropValue extends ConsPropAbstValue {

	private double value;
	
	ConsPropValue(double value) {
		this.value = value; 
	}
	
	@Override
	public String getDesc() {
		return "Constrant Prop Value";
	}

	@Override
	public AbstractValue lub(AbstractValue a) {
		
		if ( a instanceof ConsPropTOP ) {
			return a;
		} else if ( a instanceof ConsPropBOT ) {
			return this;
		} 
		
		
		if ( ((ConsPropValue) a).value == this.value ) {
			return this;
		}
		
		return new ConsPropTOP();
	}

	@Override
	public boolean lte(AbstractValue a) {
		return (a instanceof ConsPropTOP) || (!(a instanceof ConsPropBOT) && ((ConsPropValue) a).value == this.value);
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
