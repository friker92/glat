package glat.domains.sign;

import java.util.List;

import glat.domains.AbstractState;
import glat.domains.nonrel.AbstractValue;
import glat.domains.nonrel.NonRelAbstractDomain;
import glat.domains.nonrel.NonRelAbstractState;
import glat.program.instructions.Expression;
import glat.program.instructions.expressions.CompoundExpr;
import glat.program.instructions.expressions.Terminal;
import glat.program.instructions.expressions.terminals.Value;
import glat.program.instructions.expressions.terminals.Variable;
import glat.program.instructions.expressions.terminals.values.NonDeterministicValue;

public class SignAbstDomain extends NonRelAbstractDomain {

	@Override
	public AbstractState bottom(List<Variable> vars) {
		return new SignAbstState(vars);
	}

	@Override
	public AbstractState top(List<Variable> vars) {
		SignAbstState a = new SignAbstState(vars);
		for (Variable v : vars) {
			a.setValue(v, SignAbstValue.TOP);
		}
		return a;
	}

	@Override
	protected AbstractValue nondet_abstract_value(NonDeterministicValue t) {
		return SignAbstValue.TOP;
	}

	@Override
	protected AbstractValue abstract_value(Value v) {
		float f = v.getFloatNumber();
		if (f == 0)
			return SignAbstValue.ZERO;
		else if (f > 0)
			return SignAbstValue.POS;
		else
			return SignAbstValue.NEG;
	}

	@Override
	protected AbstractValue evaluate_expression(String operator, List<AbstractValue> abst_values) {
		AbstractValue v1 = abst_values.get(0);
		AbstractValue v2 = abst_values.get(1);

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
			// no need for break, all paths return
		default:
			break;
		}
		return SignAbstValue.BOT;
	}

	@Override
	protected AbstractState evaluate_boolean_expression(NonRelAbstractState b, Expression e) {
		if (e instanceof Terminal) {
			return b;
		}


		CompoundExpr compExp_e = (CompoundExpr) e;
		String op = compExp_e.getOperator();
		Variable op1 = (Variable) compExp_e.getOperand(0);
		SignAbstValue v1 = (SignAbstValue) evaluate_expression(b, op1);
		SignAbstValue v2 = (SignAbstValue) evaluate_expression(b, compExp_e.getOperand(1));
		System.out.println("> "+v2);
		if (v1.equals(v2)) {
			return b;
		}

		if (v1.equals(SignAbstValue.BOT) || v2.equals(SignAbstValue.BOT)) {
			return new SignAbstState(b.getVars());
		}

		if (v2.equals(SignAbstValue.TOP)) {
			return b;
		}

		switch (v1) {
		case NEG:
			return new SignAbstState(b.getVars());
		case POS:
			return b;
		case TOP:
			if (v2.equals(SignAbstValue.POS)) {
				b.setValue(op1, SignAbstValue.POS);
			}
			return b;
		case ZERO:
			if (v2.equals(SignAbstValue.POS)) {
				return new SignAbstState(b.getVars());
			} else {
				return b;
			}
			// break;
		default:
			break;
		}

		return b;
	}

	@Override
	public boolean hasInfiniteAscendingChains() {
		return false;
	}

	@Override
	public boolean hasInfiniteDescendingChains() {
		return false;
	}

}
