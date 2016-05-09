package glat.domain.sign;

import java.util.List;

import glat.domain.AbstractDomain;
import glat.domain.AbstractState;
import glat.domain.nonrel.AbstractValue;
import glat.domain.nonrel.NonRelAbstractDomain;
import glat.domain.nonrel.NonRelAbstractState;
import glat.program.Instruction;
import glat.program.instructions.Assignment;
import glat.program.instructions.Expression;
import glat.program.instructions.expressions.CompoundExpr;
import glat.program.instructions.expressions.Terminal;
import glat.program.instructions.expressions.terminals.Values;
import glat.program.instructions.expressions.terminals.Variable;
import glat.program.instructions.expressions.terminals.values.NonDeterministicValue;

public class SignAbstDomain extends NonRelAbstractDomain {

	@Override
	public AbstractState bottom(List<Variable> vars) {
		return new SignAbstState(vars);
	}

	@Override
	public AbstractState exec(Instruction intsr, AbstractState a) {

		NonRelAbstractState nonRel_a = (NonRelAbstractState) a;
		NonRelAbstractState nonRel_b = (NonRelAbstractState) a.copy();

		switch (intsr.getType()) {
		case ASSIGNMENT:
			Assignment assignInstr = (Assignment) intsr;
			Expression e = assignInstr.getExpr();
			nonRel_b.setValue(assignInstr.getDest(), exprV(nonRel_b,e));
			break;
		default:
			break;
		}

		return nonRel_b;
	}
	
	
	private AbstractValue exprV(AbstractState b, Expression e){
		if(e instanceof CompoundExpr){
			CompoundExpr exp = (CompoundExpr) e;
			if (exp.getOperands().size() == 1)
				return exprV(b, exp.getOperand(0));
			else
				return op(b, exp.getOperator(), exprV(b, exp.getOperand(0)), exprV(b, exp.getOperand(1)));
		}else{
			return getV(b, (Terminal)e);
		}
	}

	private AbstractValue op(AbstractState b, String operator, AbstractValue v1, AbstractValue v2) {
		if (v1.equals(SignAbstValue.BOT) || v2.equals(SignAbstValue.BOT))
			return SignAbstValue.BOT;

		switch (operator) {
		case "+":
			if (v1.equals(SignAbstValue.TOP) || v2.equals(SignAbstValue.TOP))
				return SignAbstValue.TOP;
			else if (v1.equals(SignAbstValue.ZERO))
				return v2;
			else if (v2.equals(SignAbstValue.ZERO))
				return v1;
			else if (!v1.equals(v2))
				return SignAbstValue.TOP;
			else
				return v1;
		case "-":
			if (v1.equals(SignAbstValue.TOP) || v2.equals(SignAbstValue.TOP))
				return SignAbstValue.TOP;
			else if (v2.equals(SignAbstValue.ZERO))
				return v1;
			else if (v1.equals(SignAbstValue.ZERO))
				return (v2.equals(SignAbstValue.POS)) ? SignAbstValue.NEG : SignAbstValue.POS;
			else if (!v1.equals(v2))
				return v1;
			else
				return SignAbstValue.TOP;
		case "/":
			if (v2.equals(SignAbstValue.ZERO))
				return SignAbstValue.BOT;
			else if (v1.equals(SignAbstValue.ZERO))
				return SignAbstValue.ZERO;
			else if (v1.equals(SignAbstValue.TOP) || v2.equals(SignAbstValue.TOP))
				return SignAbstValue.TOP;
			else if (v1.equals(v2))
				return SignAbstValue.POS;
			else
				return SignAbstValue.NEG;
		case "*":
			if (v1.equals(SignAbstValue.TOP) || v2.equals(SignAbstValue.TOP))
				return SignAbstValue.TOP;
			else if (v1.equals(SignAbstValue.ZERO) || v2.equals(SignAbstValue.ZERO))
				return SignAbstValue.ZERO;
			else if (v1.equals(v2))
				return SignAbstValue.POS;
			else
				return SignAbstValue.NEG;
		default:
			break;
		}
		return SignAbstValue.BOT;
	}

	private SignAbstValue getV(AbstractState a, Terminal t) {
		NonRelAbstractState nonRel_a = (NonRelAbstractState) a;
	
		if (t instanceof Variable) {
			return (SignAbstValue) nonRel_a.getValue((Variable) t);
		} else {
			Values v = (Values) t;
			if (v instanceof NonDeterministicValue) {
				return SignAbstValue.TOP;
			} else {
				float f = v.getFloatNumber();
				if (f == 0)
					return SignAbstValue.ZERO;
				else if (f > 0)
					return SignAbstValue.POS;
				else
					return SignAbstValue.NEG;
			}
		}
	}

	@Override
	public AbstractState top(List<Variable> vars) {
		return null;
	}


}
