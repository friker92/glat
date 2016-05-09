package glat.domains.constprop;

import glat.domains.nonrel.AbstractValue;

public class ConsPropBOT implements AbstractValue {

	
	ConsPropBOT() {
	}
	
	@Override
	public String getDesc() {
		return "Constrant Prop Abstract Value (Bottom)";
	}

	@Override
	public AbstractValue lub(AbstractValue a) {
		return a;
	}

	@Override
	public boolean lte(AbstractValue a) {
		return true;
	}
	
	@Override
	public String toString() {
		return "BOT";
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ConsPropBOT;
	}
}
