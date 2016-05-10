package glat.domains.sign;

import glat.domains.nonrel.AbstractValue;

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
	
	@Override
	public AbstractValue lub(AbstractValue a) {
		SignAbstValue b = (SignAbstValue) a;
		
		if (this.equals(SignAbstValue.TOP) || b.equals(SignAbstValue.TOP)) {
			return SignAbstValue.TOP;
		}

		if (this.equals(SignAbstValue.BOT)) {
			return b;
		}

		if (this.equals(SignAbstValue.BOT)) {
			return this;
		}

		if (this.equals(b)) {
			return this;
		}

		return SignAbstValue.TOP;
	}

	@Override
	public String getDesc() {
		return "Sign abstract value";
	}

	@Override
	public AbstractValue widen(AbstractValue a) {
		return this.lub(a);
	}
}
