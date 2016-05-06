package glat.domain;

public interface AbstractValue {

	/**
	 * 
	 * @return
	 */
	default public String getDesc() {
		return getClass().getName();
	}

	
	/**
	 * 
	 * @param a
	 * @return
	 */
	default public AbstractValue lub(AbstractValue a) {
		throw new UnsupportedOperationException(getDesc() + " does not support lub");
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	default public boolean lte(AbstractValue a) {
		throw new UnsupportedOperationException(getDesc() + " does not support lte");
	}
}
