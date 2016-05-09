package glat.domains;

import glat.program.Node;

public interface Store {
	public void set(Node key, AbstractState value);

	public void modify(Node key, AbstractState value);

	public AbstractState get(Node key);
}
