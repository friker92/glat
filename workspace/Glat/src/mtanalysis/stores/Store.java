package mtanalysis.stores;

import glat.program.Node;
import mtanalysis.domains.AbstractState;

public interface Store {

	public void set(Node key, AbstractState value);
	public boolean modify(Node key, AbstractState value);
	public AbstractState get(Node key);
}
