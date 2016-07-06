package mtanalysis.fixpoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import glat.program.ControlFlowGraph;
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
import mtanalysis.interferences.InterferenceSet;
import mtanalysis.stores.Store;
import mtanalysis.strategies.IterationStrategy;
import mtanalysis.strategies.StrategyNode;

public class ThSimpleFixpoint implements Fixpoint {

	private AbstractDomain domain;
	private GlatProgram program;
	private IterationStrategy iterStrategy;
	private Vector<ThreadInfo> vth;
	private int widenDelay;

	public ThSimpleFixpoint(GlatProgram program, Vector<ThreadInfo> vth,
			AbstractDomain domain, IterationStrategy itStrategy) {
		this.program = program;
		this.vth = vth;
		this.domain = domain;
		this.iterStrategy = itStrategy;
		setIterationProperties();
	}


	@Override
	public void start() {
		boolean change = true;
		while(change){
			change = false;
			InterferenceSet interferences  = new InterferenceSet();
			for(ThreadInfo th : this.vth){
				interferences.addSet(interferences);
			}
			for(ThreadInfo th : this.vth){
				boolean chng = start_fixpoint(th,iterStrategy.getStrategy(th.getCFG()),interferences);
				change = change || chng;
			}	
		
		}
	}

	@Override
	public Store<Node, AbstractState> getResult() {
		return vth.get(0).getStore();
	}
	
	private void setIterationProperties(){
		this.widenDelay = 3;	
	}

	private boolean start_fixpoint(ThreadInfo th,StrategyNode st,InterferenceSet interferences) {
		boolean global_change = false;
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
					changed = analyze_node(th,n,interferences);
					if (n.isWidenNode()) {
						widenPointsCount--;
						notStable = notStable || changed;
						global_change = global_change || changed;
					}
				} else {
					start_fixpoint(th,n,interferences);
				}
			}
		} while (notStable);
		return global_change;
	}

	private boolean analyze_node(ThreadInfo th,StrategyNode stn,InterferenceSet interferences) {
		Store<Node, AbstractState> store = th.getStore();
		Node n = stn.getCFGNode();
		AbstractState currState = store.getValue(n);

		List<AbstractState> lst = new ArrayList<AbstractState>();

		for (Transition t : stn.getInTransitions()) {
			AbstractState localState;
			AbstractState st = store.getValue(t.getSrcNode());
			if(isReadGlobal(t)){
				localState = applyInterference(interferences, t, st);
			}else{
				localState = st;
			}
			st = domain.exec(t, localState);
			if(isWriteGlobal(t)){
				th.addInterference(new FlowInsensitiveInterference(domain, localState, st));
			}
			lst.add(st);
		}

		lst.add(currState);

		AbstractState st = domain.lub(lst);
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



	private AbstractState applyInterference(InterferenceSet interferences,Transition t, AbstractState st_prime) {
		AbstractState st = st_prime.copy();
		if(isWriteGlobal(t)){
			st = interferences.applyInterferences(this.domain, st_prime);			
		}
		return st;
	}

	private boolean isWriteGlobal(Transition t) {
		if(!t.hasProp("isWriteGlobal"))return false;
		return (boolean) t.getPropValue("isWriteGlobal");
	}
	
	private boolean isReadGlobal(Transition t) {
		if(!t.hasProp("isReadGlobal"))return false;
		return (boolean) t.getPropValue("isReadGlobal");
	}



}
