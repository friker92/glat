package glat.domains.sign;

import java.util.List;

import glat.domains.AbstractState;
import glat.domains.nonrel.AbstractValue;
import glat.domains.nonrel.NonRelAbstractDomain;
import glat.program.instructions.expressions.terminals.Values;
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
		for( Variable v : vars ) {
			a.setValue(v, SignAbstValue.TOP);
		}
		return a;
	}

	@Override
	protected AbstractValue abstract_value(Values v) {
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
		default:
			break;
		}
		return SignAbstValue.BOT;
	}

}
