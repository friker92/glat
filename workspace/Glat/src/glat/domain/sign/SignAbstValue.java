package glat.domain.sign;

import glat.domain.AbstractValue;

public enum SignAbstValue implements AbstractValue {
	TOP("T"), BOT("B"), POS("P"), NEG("N"), ZERO("Z");
	
	private String desc;
	
	SignAbstValue(String desc) {
		this.desc = desc;
	}
	
	@Override
	public String toString() {
		return desc;
	}
	
	@Override
	public boolean lte(AbstractValue a) {
		if ( ! (a instanceof SignAbstValue) ) {
			return false;
		}
		
		SignAbstValue value = (SignAbstValue) a;
		
		if ( value.equals(TOP) ) {
			return true;
		}

		if ( this.equals(BOT) ) {
			return true;
		}

		if ( this.equals(value) ) {
			return true;
		}
		
		return false;
	}
}
