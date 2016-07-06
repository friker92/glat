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
		AbstractState global_st = domain.project(st, source.getVars());
		if(domain.glb(source,global_st).equals(BottomState.getInstance()))
			return null;
		AbstractState ret = domain.lub(target, global_st);
		return domain.extend(st,ret);
	}
	@Override
	public String toString() {
		return source + " -> "+target;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof FlowInsensitiveInterference){
			FlowInsensitiveInterference fobj = (FlowInsensitiveInterference)obj;
			return this.source.equals(fobj.source) && this.target.equals(fobj.target) && this.domain.equals(fobj.domain);
		}
		return super.equals(obj);
	}

}
