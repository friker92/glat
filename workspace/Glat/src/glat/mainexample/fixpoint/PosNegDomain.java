package glat.mainexample.fixpoint;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import glat.fixpoint.AbstractDomain;
import glat.fixpoint.AbstractState;
import glat.program.GlatInstruction;
import glat.program.instructions.Assignment;
import glat.program.instructions.Expression;
import glat.program.instructions.expressions.CompoundExpr;
import glat.program.instructions.expressions.Terminal;
import glat.program.instructions.expressions.terminals.Values;
import glat.program.instructions.expressions.terminals.Variable;

public class PosNegDomain implements AbstractDomain {

	private PosNegState c(AbstractState s) {
		return (PosNegState) s;
	}

	@Override
	public AbstractState bottom(List<Variable> vars) {
		PosNegState p = new PosNegState();
		vars.forEach((d) -> p.init(d));
		return p;
	}

	@Override
	public AbstractState empty() {
		return new PosNegState();
	}

	
	
	public PosNegValues getV(PosNegState pt, Terminal t) {
		
		if (t instanceof Variable)
			return pt.get((Variable) t);
		else {
			Values v = (Values) t;
			if (v.getType().equals("nondeterministic"))
				return PosNegValues.TOP;
			else {
				float f = v.getFloatNumber();
				if (f == 0)
					return PosNegValues.ZERO;
				else if (f > 0)
					return PosNegValues.POS;
				else
					return PosNegValues.NEG;
			}
		}
	}

	@Override
	public AbstractState abstractExec(GlatInstruction i, AbstractState st) {
		PosNegState pt = new PosNegState();
		pt.extend(c(st));
		switch (i.getType()) {
		case ASSIGNMENT:
			Assignment a = (Assignment) i;
			pt.add(a.getDest(), exprV(pt,a.getExpr()));
			break;
		case ASSERT:
		case ASSUME:
			break;
		case ASYNCCALL:
		case SYNCCALL:
			break;
		case JOIN:
			break;
		case LOCK:
		case UNLOCK:
			break;
		case RETURN:
			break;
		default:
			break;
		}
		return pt;
	}
	
	private PosNegValues exprV(PosNegState pt, Expression e){
		if(e instanceof CompoundExpr){
			CompoundExpr exp = (CompoundExpr) e;
			if (exp.getOperands().size() == 1)
				return exprV(pt, exp.getOperand(0));
			else
				return op(pt, exp.getOperator(), exprV(pt, exp.getOperand(0)), exprV(pt, exp.getOperand(1)));
		}else{
			return getV(pt, (Terminal)e);
		}
	}

	private PosNegValues op(PosNegState pt, String op, PosNegValues v1, PosNegValues v2) {
		if (v1 == PosNegValues.NONE || v2 == PosNegValues.NONE)
			return PosNegValues.NONE;
		switch (op) {
		case "+":
			if (v1 == PosNegValues.TOP || v2 == PosNegValues.TOP)
				return PosNegValues.TOP;
			else if (v1 == PosNegValues.ZERO)
				return v2;
			else if (v2 == PosNegValues.ZERO)
				return v1;
			else if (v1 != v2)
				return PosNegValues.TOP;
			else
				return v1;
		case "-":
			if (v1 == PosNegValues.TOP || v2 == PosNegValues.TOP)
				return PosNegValues.TOP;
			else if (v2 == PosNegValues.ZERO)
				return v1;
			else if (v1 == PosNegValues.ZERO)
				return (v2 == PosNegValues.POS) ? PosNegValues.NEG : PosNegValues.POS;
			else if (v1 != v2)
				return v1;
			else
				return PosNegValues.TOP;
		case "/":
			if (v2 == PosNegValues.ZERO)
				return PosNegValues.NONE;
			else if (v1 == PosNegValues.ZERO)
				return PosNegValues.ZERO;
			else if (v1 == PosNegValues.TOP || v2 == PosNegValues.TOP)
				return PosNegValues.TOP;
			else if (v1 == v2)
				return PosNegValues.POS;
			else
				return PosNegValues.NEG;
		case "*":
			if (v1 == PosNegValues.TOP || v2 == PosNegValues.TOP)
				return PosNegValues.TOP;
			else if (v1 == PosNegValues.ZERO || v2 == PosNegValues.ZERO)
				return PosNegValues.ZERO;
			else if (v1 == v2)
				return PosNegValues.POS;
			else
				return PosNegValues.NEG;
		default:
			break;
		}
		return PosNegValues.NONE;
	}

	@Override
	public AbstractState extend(AbstractState s0, AbstractState st) {
		PosNegState p0 = new PosNegState(), p1 = c(st);
		p0.extend(c(s0));
		p0.extend(p1);
		return p0;
	}

	@Override
	public AbstractState project(AbstractState s0, List<Variable> lv) {
		PosNegState s = c(s0);
		PosNegState s1 = new PosNegState();
		lv.forEach((var) -> s1.add(var, s.get(var)));
		return s1;
	}

	@Override
	public AbstractState rename(AbstractState s0, List<Variable> actual, List<Variable> formal) {
		PosNegState p0 = new PosNegState();
		p0.extend(c(s0));

		Iterator<Variable> iA = actual.iterator(), iF = formal.iterator();
		while (iA.hasNext() && iF.hasNext()) {
			p0.rename(iA.next(),  iF.next());
		}
		return p0;
	}

	@Override
	public AbstractState lub(AbstractState s0, AbstractState s1) {
		PosNegState p0 = new PosNegState(c(s0)), p1 = new PosNegState(c(s1));
		return p0.lub(p1);
	}

	@Override
	public boolean le(AbstractState s0, AbstractState s1) {
		Set<Variable> vs = c(s0).getVariables();
		for ( Variable v : vs ) {
			PosNegValues v1 = c(s0).get(v);
			PosNegValues v2 = c(s1).get(v);
			if ( v1 == PosNegValues.TOP && v2 != PosNegValues.TOP) return false;
			if ( v1 == PosNegValues.NEG && !(v2 == PosNegValues.TOP || v2 == PosNegValues.NEG )) return false;
			if ( v1 == PosNegValues.POS && !(v2 == PosNegValues.TOP || v2 == PosNegValues.POS )) return false;
			if ( v1 == PosNegValues.ZERO && !(v2 == PosNegValues.TOP || v2 == PosNegValues.ZERO )) return false;
		}
		
		return true;
	//	return c(s0).size() <= c(s1).size();
	}

}
