package mtanalysis.stores;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import glat.program.Node;
import mtanalysis.domains.AbstractState;

public class NodeAbstStateStore implements Store<Node,AbstractState> {

	private Map<Node, AbsStateWithCount> table;

	private class AbsStateWithCount {
		public AbstractState state;
		public int count;

		public AbsStateWithCount(AbstractState st) {
			this.state = st;
			this.count = 0;
		}

		@Override
		public String toString() {
			return state.toString();
		}
	}

	public NodeAbstStateStore() {
		this.table = new HashMap<Node, AbsStateWithCount>();
	}

	@Override
	public int setValue(Node key, AbstractState value) {
		AbsStateWithCount extValue = table.get(key);
		if ( extValue == null ) {
			extValue = new AbsStateWithCount(value);
			table.put(key, extValue);
			return extValue.count;
		} else {
			extValue.count++;
			extValue.state = value;
		}
		return extValue.count;
	}

	@Override
	public AbstractState getValue(Node key) {
		AbsStateWithCount extValue = table.get(key);
		if ( extValue != null ) {
			return extValue.state;
		} else {
			return null;
		}
	}


	@Override
	public int getCount(Node key) {
		AbsStateWithCount extValue = table.get(key);
		if ( extValue != null ) {
			return extValue.count;
		} else {
			return -1;
		}
	}

	@Override
	public int setCount(Node key, int count) {
		AbsStateWithCount extValue = table.get(key);
		int currCount = -1;
		if ( extValue != null ) {
			currCount = extValue.count;
			extValue.count = count;
		}
		return currCount;
	}

	@Override
	public Set<Node> getKeys() {
		return table.keySet();
	}
	

	@Override
	public String toString() {
		String str = "{";
		for (Node n : table.keySet()) {
			str += "\n\t" + n + ": " + table.get(n);
		}
		str += "\n}\n";
		return str;
	}
}
