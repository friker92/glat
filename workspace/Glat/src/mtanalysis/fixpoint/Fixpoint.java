package mtanalysis.fixpoint;

import mtanalysis.stores.Store;

public interface Fixpoint {
	public void start();

	public Store getResult();
}
