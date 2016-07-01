package mtanalysis.fixpoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import glat.program.GlatProgram;
import glat.program.Node;
import glat.program.Transition;
import mtanalysis.domains.AbstractDomain;
import mtanalysis.domains.AbstractState;
import mtanalysis.stores.Store;
import mtanalysis.strategies.IterationStrategy;
import mtanalysis.strategies.StrategyNode;

public class SeqFixpoint implements Fixpoint {

	private AbstractDomain domain;
	private Store<Node, AbstractState> store;
	private GlatProgram program;
	private IterationStrategy iterStrategy;
	private int widenDelay = 3;

	public SeqFixpoint(GlatProgram program, Store<Node, AbstractState> store, AbstractDomain d,
			IterationStrategy iterStrategy) {
		this.program = program;
		this.store = store;
		domain = d;
		this.iterStrategy = iterStrategy;
	}

	private boolean start_fixpoint(StrategyNode st) {
		boolean notStable;
		boolean changed;
		int widenPointsCount = 0;
		do {
			notStable = false;
			Iterator<StrategyNode> l = st.getComponents().iterator();
			widenPointsCount = st.getWidenPoints().size();
			while (l.hasNext() && (widenPointsCount > 0 || notStable)) {
				StrategyNode n = l.next();
				if (n.isLeaf()) {
					changed = analyze_node(n);
					if (n.isWidenNode()) {
						widenPointsCount--;
						notStable = notStable || changed;
					}
				} else {
					start_fixpoint(n);
				}
			}
		} while (notStable);
		return false;
	}

	private boolean analyze_node(StrategyNode stn) {
		Node n = stn.getCFGNode();
		AbstractState currState = store.getValue(n);

		List<AbstractState> lst = new ArrayList<AbstractState>();

		AbstractState st = currState;
		for (Transition t : stn.getInTransitions()) {
			st = domain.exec(t, store.getValue(t.getSrcNode()));
			lst.add(st);
		}

		lst.add(currState);

		st = domain.lub(lst);
		boolean changed = false;
		
		if (!domain.lte(st, currState)) {
			if (store.getCount(n) > widenDelay ) {
				store.setValue(n, domain.widen(currState, st));
				store.setCount(n, 0);
			} else {
				store.setValue(n, st);
			}
			changed= true;
		}
		return changed;
	}

	@Override
	public void start() {
		start_fixpoint(iterStrategy.getStrategy());
	}

	@Override
	public Store<Node, AbstractState> getResult() {
		return store;
	}

}
