package glat.mainexample.fixpoint;

import java.util.HashMap;

import glat.fixpoint.AbstractState;

public class PosNegState implements AbstractState {

	private HashMap<String,PosNegValues> st;
	
	
	
	public PosNegState(){
		st = new HashMap<String,PosNegValues>();
	}
	public PosNegState(PosNegState pn){
		st = pn.st;
	}
	
	public PosNegValues Null(){
		return PosNegValues.NONE;
	}
	public void init(String s){
		add(s, Null());
	}
	
	public void rename(String s1,String s2){
		add(s2, get(s1));
		st.remove(s1);
	}
	
	public void add(String s, PosNegValues v){
		if(st.containsKey(s))
			st.replace(s, v);
		else
			st.put(s, v);
	}
	
	public PosNegValues get(String s){
		if(st.containsKey(s))
			return st.get(s);
		else
			return Null();
	}
	
	public void extend(PosNegState stt){
		stt.st.forEach((s,v)->st.put(s, v));
	}
	
	public int size(){
		return st.size();
	}
	
	@Override
	public String toString(){
		return st.toString();
	}
	
	public PosNegValues cmp(PosNegValues v1, PosNegValues v2){
		if(v1 == PosNegValues.NONE)
			return v2;
		if(v2 == PosNegValues.NONE)
			return v1;
		if(v1 == PosNegValues.TOP || v2 == PosNegValues.TOP || v1 != v2)
			return PosNegValues.TOP;
		return v1;
	}
	
	public PosNegState lub(PosNegState p1) {
		PosNegState r = new PosNegState();
		st.forEach((s,v)->r.add(s, cmp(p1.get(s),v)));
		p1.st.forEach((s,v)->r.add(s, cmp(this.get(s),v)));
		return r;
	}
}
