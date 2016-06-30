package mtanalysis.fixpoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import glat.program.GlatProgram;
import glat.program.GlatTransition;
import glat.program.Method;
import glat.program.Node;
import glat.program.Transition;
import glat.program.instructions.Call;
import glat.program.instructions.expressions.terminals.Variable;
import mtanalysis.domains.AbstractDomain;
import mtanalysis.domains.AbstractState;
import mtanalysis.interferences.FlowInsensitiveInterference;
import mtanalysis.interferences.Interference;
import mtanalysis.stores.Store;
import mtanalysis.strategies.IterationStrategy;
import mtanalysis.strategies.StrategyNode;

public class SeqFixpoint implements Fixpoint {

	private AbstractDomain domain;
	private Store table;
	private GlatProgram program;
	private Call call;
	private IterationStrategy itStrategy;
	private Vector<Interference> interferences;
	private Vector<Interference> return_interferences;

	public SeqFixpoint(GlatProgram p, Call c, Store s, AbstractDomain d, IterationStrategy its, Vector<Interference> interf, Vector<Interference> ret_interf) {
		program = p;
		call = c;
		table = s;
		domain = d;
		itStrategy = its;
		interferences = interf;
		return_interferences = ret_interf;
	}

	private boolean analyze(IterationStrategy strategy) {
		return analyze(strategy.getProperties(), strategy.getStrategy());
	}

	private boolean analyze(Properties strategyProp, StrategyNode st) {
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
					changed = analyze_node(strategyProp, n);
					if (n.isWidenNode()) {
						widenPointsCount--;
						notStable = notStable || changed;
					}
				} else {
					analyze(strategyProp, n);
				}
			}
		} while (notStable);
		System.err.println(return_interferences);
		return false;
	}

	private boolean analyze_node(Properties strategyProp, StrategyNode stn) {
		Node n = stn.getCFGNode();
		AbstractState currState = table.get(n);

		List<AbstractState> lst = new ArrayList<AbstractState>();

		AbstractState st = currState;
		AbstractState st_prime;
		for (Transition t : stn.getInTransitions()) {
			st_prime = table.get(t.getSrcNode());
			st = domain.exec(t, st_prime);
				return_interferences.add(new FlowInsensitiveInterference(domain, st_prime, st));
			lst.add(st);
		}
		lst.add(currState);

		st = domain.lub(lst);
		
		if (table.modify(n, st)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isWriteGlobal(Transition t) {
		return (boolean) t.getPropValue("isWriteGlobal");
	}

	@Override
	public void start() {
		
		analyze(itStrategy);
	}

	@Override
	public Store getResult() {
		return table;
	}

}
