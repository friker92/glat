package glat.example;

import glat.domains.AbstractState;
import glat.program.Node;

public interface Store {

	public void set(Node key, AbstractState value);
	public boolean modify(Node key, AbstractState value);
	public AbstractState get(Node key);
}
