package mtanalysis.domains.product;

import java.util.ArrayList;
import java.util.List;

import glat.program.Instruction;
import glat.program.instructions.expressions.terminals.Variable;
import mtanalysis.domains.AbstractDomain;
import mtanalysis.domains.AbstractState;
import mtanalysis.domains.BottomState;

public class ProductAbstractDomain implements AbstractDomain {

	private List<AbstractDomain> domains;
	
	public ProductAbstractDomain(List<AbstractDomain> ds){
		domains = ds;
	}

	@Override
	public AbstractState top(List<Variable> vars) {
		List<AbstractState> sts = new ArrayList<AbstractState>();
		for (AbstractDomain d : domains) {
			sts.add(d.top(vars));
		}
		ProductAbstractState p = new ProductAbstractState(sts);
		return p;
	}

	@Override
	public AbstractState bottom(List<Variable> vars) {
		List<AbstractState> sts = new ArrayList<AbstractState>();
		for (AbstractDomain d : domains) {
			sts.add(d.bottom(vars));
		}
		ProductAbstractState p = new ProductAbstractState(sts);
		return p;
	}

	@Override
	public AbstractState defaultState(List<Variable> vars) {
		List<AbstractState> sts = new ArrayList<AbstractState>();
		for (AbstractDomain d : domains) {
			sts.add(d.defaultState(vars));
		}
		ProductAbstractState p = new ProductAbstractState(sts);
		return p;
	}

	@Override
	public AbstractState lub(AbstractState a, AbstractState b) {
		ProductAbstractState p_a = (ProductAbstractState) a;
		ProductAbstractState p_b = (ProductAbstractState) b;
		List<AbstractState> sts = new ArrayList<AbstractState>();
		for (int i = 0; i < domains.size(); i++) {
			sts.add(domains.get(i).lub(p_a.getAbstractState(i), p_b.getAbstractState(i)));
		}
		ProductAbstractState p = new ProductAbstractState(sts);
		return p;
	}

	@Override
	public AbstractState glb(AbstractState a, AbstractState b) {
		ProductAbstractState p_a = (ProductAbstractState) a;
		ProductAbstractState p_b = (ProductAbstractState) b;
		List<AbstractState> sts = new ArrayList<AbstractState>();
		for (int i = 0; i < domains.size(); i++) {
			AbstractState s = domains.get(i).glb(p_a.getAbstractState(i), p_b.getAbstractState(i));
			if (s.equals(BottomState.getInstance()))
				return BottomState.getInstance();
			sts.add(s);
		}
		ProductAbstractState p = new ProductAbstractState(sts);
		return p;
	}

	@Override
	public AbstractState widen(AbstractState a, AbstractState b) {
		ProductAbstractState p_a = (ProductAbstractState) a;
		ProductAbstractState p_b = (ProductAbstractState) b;
		List<AbstractState> sts = new ArrayList<AbstractState>();
		for (int i = 0; i < domains.size(); i++) {
			sts.add(domains.get(i).widen(p_a.getAbstractState(i), p_b.getAbstractState(i)));
		}
		ProductAbstractState p = new ProductAbstractState(sts);
		return p;
	}

	@Override
	public boolean hasInfiniteAscendingChains() {
		boolean r = false;
		for (AbstractDomain d : domains) {
			r |= d.hasInfiniteAscendingChains();
		}
		return r;
	}

	@Override
	public boolean hasInfiniteDescendingChains() {
		boolean r = false;
		for (AbstractDomain d : domains) {
			r |= d.hasInfiniteDescendingChains();
		}
		return r;
	}

	@Override
	public boolean lte(AbstractState a, AbstractState b) {
		boolean r = false;
		for (AbstractDomain d : domains) {
			r |= d.hasInfiniteDescendingChains();
		}
		return r;
	}

	@Override
	public AbstractState exec(Instruction instr, AbstractState a) {
		ProductAbstractState p_a = (ProductAbstractState) a;
		List<AbstractState> sts = new ArrayList<AbstractState>();
		for (int i = 0; i < domains.size(); i++) {
			sts.add(domains.get(i).exec(instr, p_a.getAbstractState(i)));
		}
		ProductAbstractState p = new ProductAbstractState(sts);
		return p;
	}

	@Override
	public AbstractState extend(AbstractState s0, AbstractState st) {
		ProductAbstractState p_a = (ProductAbstractState) s0;
		ProductAbstractState p_b = (ProductAbstractState) st;
		List<AbstractState> sts = new ArrayList<AbstractState>();
		for (int i = 0; i < domains.size(); i++) {
			sts.add(domains.get(i).extend(p_a.getAbstractState(i), p_b.getAbstractState(i)));
		}
		ProductAbstractState p = new ProductAbstractState(sts);
		return p;
	}

	@Override
	public AbstractState project(AbstractState s0, List<Variable> lv) {
		ProductAbstractState p_a = (ProductAbstractState) s0;
		List<AbstractState> sts = new ArrayList<AbstractState>();
		for (int i = 0; i < domains.size(); i++) {
			sts.add(domains.get(i).project(p_a.getAbstractState(i), lv));
		}
		ProductAbstractState p = new ProductAbstractState(sts);
		return p;
	}

	@Override
	public AbstractState rename(AbstractState s0, List<Variable> actual, List<Variable> formal) {
		ProductAbstractState p_a = (ProductAbstractState) s0;
		List<AbstractState> sts = new ArrayList<AbstractState>();
		for (int i = 0; i < domains.size(); i++) {
			sts.add(domains.get(i).rename(p_a.getAbstractState(i), actual, formal));
		}
		ProductAbstractState p = new ProductAbstractState(sts);
		return p;
	}

}
