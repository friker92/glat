package mtanalysis.fixpoint;

import glat.program.Node;
import mtanalysis.domains.AbstractState;
import mtanalysis.stores.Store;

public interface Fixpoint {
	public void start();
	public Store<Node,AbstractState> getResult();
}
