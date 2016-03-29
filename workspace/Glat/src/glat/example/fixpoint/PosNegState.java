package glat.example.fixpoint;

import java.util.HashMap;

import glat.fixpoint.AbstractState;

public class PosNegState implements AbstractState {

	private HashMap<String,PosNegDomain.V> st;
	
	public PosNegState(){
		st = new HashMap<String,PosNegDomain.V>();
	}
	
	public void add(String s, PosNegDomain.V v){
		st.put(s, v);
	}
	
}
