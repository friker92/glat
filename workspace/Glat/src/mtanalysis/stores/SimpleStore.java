package mtanalysis.stores;

import java.util.HashMap;
import java.util.Map;

import glat.program.Node;
import mtanalysis.domains.AbstractDomain;
import mtanalysis.domains.AbstractState;

public class SimpleStore implements Store {

	private Map<Node, ExtAbsSt> table;
	private AbstractDomain domain;

	private class ExtAbsSt {
		public AbstractState st;
		public int count;

		public ExtAbsSt(AbstractState st) {
			this.st = st;
			this.count = -1;
		}

		@Override
		public String toString() {
			return st.toString();
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
	public boolean modify(Node key, AbstractState value) {

		ExtAbsSt e = table.get(key);
		// e.st = domain.lub(e.st, value);
		e.count++;
		if (e.count != 0 && domain.lte(value, e.st)) {
			return false;
		} else if (e.count > 3) {
			e.count = 0;
			e.st = domain.widen(e.st, value);
			System.out.println("> " + e.st);
		} else {
			e.st = value;
		}
		return true;

		/*
		 * if (!domain.lte(value, destCurrState)) { if (e.count > 3) { e.count =
		 * 0; e.st = domain.widen(destCurrState, e.st); } return true; } return
		 * false;
		 */
	}

	@Override
	public AbstractState get(Node key) {
		return table.get(key).st;
	}

	@Override
	public String toString() {
		return table.toString();
	}
}
