package mtanalysis.interferences;

import java.util.Vector;

import mtanalysis.domains.AbstractDomain;
import mtanalysis.domains.AbstractState;

public class InterferenceSet {
	private Vector<Interference> set;
	
	public InterferenceSet(){
		this.set = new Vector<Interference>();
	}
	
	public AbstractState applyInterferences(AbstractDomain domain, AbstractState st){
		AbstractState st_prime = st.copy();
		for(Interference i : set){
			st_prime = domain.lub(st_prime,i.incorporate(st));
		}
		return st_prime;
	}
	
	@Override
	public String toString(){
		String str = "[";
		for (Object n : set) {
			str += "\n\t" + n;
		}
		str += "\n]";
		return str;
	}

	public void add(Interference interference) {
		this.set.addElement(interference);
	}

	public void addSet(InterferenceSet interferences) {
		this.set.addAll(interferences.set);		
	}
}
