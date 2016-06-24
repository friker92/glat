package mtanalysis.interferences;

import mtanalysis.domains.AbstractState;

public interface Interference {

	/**
	 * {@code null} if it can not incorporate or an State result of incorporate
	 * this interference to st
	 * 
	 * @param st
	 * @return
	 */
	public AbstractState incorporate(AbstractState st);

}
