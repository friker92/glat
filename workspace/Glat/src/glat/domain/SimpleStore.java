package glat.domain;

import java.util.HashMap;
import java.util.Map;

import glat.program.Node;

public class SimpleStore implements Store {
	
	private Map<Node, ExtAbsSt> table;
	private AbstractDomain domain;

	private class ExtAbsSt {
		public AbstractState st;
		public int count;
		public ExtAbsSt(AbstractState st) {
			this.st = st;
			this.count = 0;
		}
	}
	
	public SimpleStore(AbstractDomain domain) {
		this.table = new HashMap<Node, ExtAbsSt>();
		this.domain = domain;
	}

	@Override
	public void set(Node key, AbstractState value) {
		table.put(key, new ExtAbsSt(value));
	}

	@Override
	public void modify(Node key, AbstractState value) {
		ExtAbsSt currValue = table.get(key);
		
		currValue.st = domain.lub(currValue.st,value);


	}

	@Override
	public AbstractState get(Node key) {
		// TODO Auto-generated method stub
		return null;
	}

}
