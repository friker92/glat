package mtanalysis.interferences;

import mtanalysis.domains.AbstractDomain;
import mtanalysis.domains.AbstractState;
import mtanalysis.domains.BottomState;

public class FlowInsensitiveInterference implements Interference {

	private AbstractState source;
	private AbstractState target;
	private AbstractDomain domain;
	
	public FlowInsensitiveInterference(AbstractDomain d, AbstractState src, AbstractState trgt){
		domain = d;
		source = src;
		target = trgt;
	}
	@Override
	public AbstractState incorporate(AbstractState st) {
		if(domain.glb(source,st).equals(BottomState.getInstance()))
			return null;
		return domain.lub(target, st);
	}
	@Override
	public String toString() {
		return source + " -> "+target;
	}

}
