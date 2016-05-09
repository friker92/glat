package glat.domain.nonrel;

public interface AbstractValue {

	/**
	 * 
	 * @return
	 */
	public String getDesc();

	/**
	 * 
	 * @param a
	 * @return
	 */
	public AbstractValue lub(AbstractValue a);

	/**
	 * 
	 * @param a
	 * @return
	 */
	public boolean lte(AbstractValue a);
}
