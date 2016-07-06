package mtanalysis.fixpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
		Map<ThreadInfo,StrategyNode> vStrategyNode = new HashMap<ThreadInfo,StrategyNode>();
		
		for(ThreadInfo th: this.vth){
			Call c = th.getCall();
			Method m = c.getMethodRef();
			vStrategyNode.put(th,iterStrategy.getStrategy(m.getControlFlowGraph()));
		}
		
		boolean change = true;
		int count = 0;
		while(change){
			change = false;
			InterferenceSet interferences  = new InterferenceSet();
			for(ThreadInfo th : this.vth){
				interferences.addSet(th.getInterferences());
			}
			System.out.println("Vuelta "+count+": \n**itfs: "+interferences+"\n**store: ");
			this.getResult();
			for(ThreadInfo th : this.vth){
				boolean chng = start_fixpoint(th,vStrategyNode.get(th),interferences);
				change = change || chng;
			}	

			count++;
		}
		InterferenceSet interferences  = new InterferenceSet();
		for(ThreadInfo th : this.vth){
			interferences.addSet(th.getInterferences());
		}
		System.out.println("Vuelta "+count+": \n**itfs: "+interferences+"\n**store: ");
		this.getResult();
	}

	@Override
	public Store<Node, AbstractState> getResult() {
		for(ThreadInfo th : vth){
			System.out.println(""+th.getId()+" : "+th.getStore());
		}
		return null;
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
			widenPointsCount = st.getImportantPoints().size();
			while (l.hasNext() && (widenPointsCount > 0 || notStable)) {
				StrategyNode n = l.next();
				if (n.isLeaf()) {
					changed = analyze_node(th,n,interferences);
					if (n.isImportantNode()) {
						widenPointsCount--;
						notStable = notStable || changed;
						global_change = global_change || changed;
					}
				} else {
					changed = start_fixpoint(th,n,interferences);
					global_change = global_change || changed;
				}
			}
		} while (notStable);
		return global_change;
	}

	private boolean analyze_node(ThreadInfo th,StrategyNode stn,InterferenceSet interferences) {
		Store<Node, AbstractState> store = th.getStore();
		Node n = stn.getCFGNode();
		List<AbstractState> lst = new ArrayList<AbstractState>();
		
		AbstractState dest_curr_st = store.getValue(n);
		AbstractState tmp_st, exec_st, src_st, st;
		
		for (Transition t : stn.getInTransitions()) {
			src_st = store.getValue(t.getSrcNode());

			exec_st = domain.exec(t, src_st);
			if(isWriteGlobal(t)){
				AbstractState global_src = domain.project(src_st, program.getGlobalVariables());
				AbstractState global_exec = domain.project(exec_st, program.getGlobalVariables());
				th.addInterference(new FlowInsensitiveInterference(domain, global_src, global_exec));
			}
			lst.add(exec_st);
		}
		lst.add(dest_curr_st);

		tmp_st = domain.lub(lst);
		boolean changed = false;
		
		st = applyInterference(interferences, n, tmp_st);
		
		if (!domain.lte(st, dest_curr_st)) {
			if (store.getCount(n) > widenDelay ) {
				store.setValue(n, domain.widen(dest_curr_st, st));
				store.setCount(n, 0);
			} else {
				store.setValue(n, st);
			}
			changed= true;
		}
		return changed;
	}



	private AbstractState applyInterference(InterferenceSet interferences,Node n, AbstractState st_prime) {
		AbstractState st = st_prime.copy();
		if(isReadGlobal(n)){
			st = interferences.applyInterferences(this.domain, st_prime);			
		}
		return st;
	}

	private boolean isWriteGlobal(Transition t) {
		if(!t.hasProp("isWriteGlobal"))return false;
		return (boolean) t.getPropValue("isWriteGlobal");
	}
	
	private boolean isReadGlobal(Node n) {
		if(!n.hasProp("isReadGlobal"))return false;
		return (boolean) n.getPropValue("isReadGlobal");
	}


}
