package mtanalysis.domains.nonrel;

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
	public AbstractValue glb(AbstractValue a);

	/**
	 * assuming this is smaller than or euqal a
	 * 
	 * 
	 * @param a
	 * @return
	 */
	public AbstractValue widen(AbstractValue a);

	/**
	 * 
	 * @param a
	 * @return
	 */
	public boolean lte(AbstractValue a);
}
