package mtanalysis.fixpoint;

import glat.program.ControlFlowGraph;
import glat.program.Node;
import mtanalysis.domains.AbstractState;
import mtanalysis.interferences.Interference;
import mtanalysis.interferences.InterferenceSet;
import mtanalysis.stores.Store;

public class ThreadInfo {
	private String id;
	private Store<Node,AbstractState> store;
	private ControlFlowGraph cfg;
	private InterferenceSet interferences;
	
	public ThreadInfo(String id,Store<Node, AbstractState> store,ControlFlowGraph cfg){
		this.id = id;
		this.store = store;
		this.cfg = cfg;
		this.interferences = new InterferenceSet();
	}
	
	public String getId() {
		return id;
	}
	
	public Store<Node, AbstractState> getStore(){
		return store;
	}
	
	public ControlFlowGraph getCFG(){
		return cfg;
	}

	public void addInterference(Interference interference) {
		interferences.add(interference);
		
	}
}
