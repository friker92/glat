package mtanalysis.domains.constprop;

import mtanalysis.domains.nonrel.AbstractValue;

public class ConsPropBOT extends ConsPropAbstValue {

	
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

	@Override
	public AbstractValue widen(AbstractValue a) {
		return this.lub(a);
	}

}
