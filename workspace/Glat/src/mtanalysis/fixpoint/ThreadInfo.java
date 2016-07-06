package mtanalysis.fixpoint;

import glat.program.ControlFlowGraph;
import glat.program.Node;
import glat.program.instructions.Call;
import mtanalysis.domains.AbstractState;
import mtanalysis.interferences.Interference;
import mtanalysis.interferences.InterferenceSet;
import mtanalysis.stores.Store;

public class ThreadInfo {
	private String id;
	private Store<Node,AbstractState> store;
	private Call call;
	private InterferenceSet interferences;
	
	public ThreadInfo(String id,Store<Node, AbstractState> store,Call call){
		this.id = id;
		this.store = store;
		this.call = call;
		this.interferences = new InterferenceSet();
	}
	
	public String getId() {
		return id;
	}
	
	public Store<Node, AbstractState> getStore(){
		return store;
	}
	
	public Call getCall(){
		return call;
	}

	public void addInterference(Interference interference) {
		interferences.add(interference);
		
	}

	public InterferenceSet getInterferences() {
		return interferences;
	}
}
