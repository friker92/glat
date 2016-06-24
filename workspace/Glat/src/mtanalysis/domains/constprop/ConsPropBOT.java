package mtanalysis.domains.constprop;

import mtanalysis.domains.nonrel.AbstractValue;
import mtanalysis.domains.nonrel.BottomAbstractValue;

public class ConsPropBOT extends ConsPropAbstValue {

	public static ConsPropBOT instance = null;
	
	private ConsPropBOT() {
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

	public static ConsPropBOT getInstance() {
		if (instance == null) {
			instance = new ConsPropBOT();
		}
		return instance;
	}

}
