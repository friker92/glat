package mtanalysis.fixpoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import glat.program.GlatProgram;
import glat.program.Method;
import glat.program.Node;
import glat.program.Transition;
import glat.program.instructions.Call;
import glat.program.instructions.expressions.terminals.Variable;
import mtanalysis.domains.AbstractDomain;
import mtanalysis.domains.AbstractState;
import mtanalysis.stores.Store;
import mtanalysis.strategies.IterationStrategy;
import mtanalysis.strategies.StrategyNode;

public class SeqFixpoint implements Fixpoint {

	private AbstractDomain domain;
	private Store table;
	private GlatProgram program;
	private Call call;
	private AbstractState default_state;
	private IterationStrategy itStrategy;

	public SeqFixpoint(GlatProgram p, Call c, Store s, AbstractState def_st, AbstractDomain d, IterationStrategy its) {
		program = p;
		call = c;
		table = s;
		default_state = def_st;
		domain = d;
		itStrategy = its;
	}

	private boolean analyze(IterationStrategy strategy) {
		return analyze(strategy.getProp(), strategy.getStrategy());
	}

	private boolean analyze(Properties strategyProp, StrategyNode st) {
		boolean notStable;
		boolean changed;
		int WPcount = 0;
		do {
			notStable = false;
			Iterator<StrategyNode> l = st.getStrategy().iterator();
			WPcount = st.getWidenPoints().size();
			while (l.hasNext() && (WPcount > 0 || notStable)) {
				StrategyNode n = l.next();
				if (n.isLeaf()) {
					changed = analyze_node(strategyProp, n);
					if (n.isWiden()) {
						WPcount--;
						notStable = notStable || changed;
					}
				} else {
					analyze(strategyProp, n);
				}
			}
		} while (notStable);
		return false;
	}

	private boolean analyze_node(Properties strategyProp, StrategyNode stn) {
		Node n = stn.getNode();
		AbstractState currState = table.get(n);

		List<AbstractState> lst = new ArrayList<AbstractState>();

		AbstractState st = currState;
		for (Transition t : stn.getInTransitionOrdered()) {
			st = domain.exec(t, table.get(t.getSrcNode()));
			lst.add(st);
		}
		lst.add(currState);

		st = domain.lub(lst);
		if (table.modify(n, st)) {
			System.out.println(n + ":\t" + table);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void start() {
		Method m;

		m = call.getMethodRef();
		System.out.println("launch: " + m.getLabel());

		List<Variable> vs = new ArrayList<Variable>(m.getVariables());
		vs.addAll(m.getParameters());
		vs.addAll(program.getGlobalVariables());
		AbstractState bt = domain.bottom(vs);
		vs = new ArrayList<Variable>(call.getArgs());
		vs.addAll(program.getGlobalVariables());
		AbstractState def = domain.project(default_state, vs);// call.getArgs());
		def = domain.rename(def, call.getArgs(), m.getParameters());
		def = domain.extend(bt, def);

		for (Node n : m.getControlFlowGraph().getNodes()) {
			if (m.getInitNode().equals(n))
				table.set(n, def);
			else
				table.set(n, bt);
		}
		System.out.println("\t" + table);
		analyze(itStrategy);
		System.out.println("\t" + table);

	}

	@Override
	public Store getResult() {
		return table;
	}

}
