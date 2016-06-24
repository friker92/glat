package mtanalysis.domains.sign;

import java.util.List;

import glat.program.instructions.expressions.Terminal;
import glat.program.instructions.expressions.TypeArithOperator;
import glat.program.instructions.expressions.TypeBoolOperator;
import glat.program.instructions.expressions.TypeValue;
import glat.program.instructions.expressions.terminals.Value;
import glat.program.instructions.expressions.terminals.Variable;
import glat.program.instructions.expressions.terminals.values.NonDeterministicValue;
import mtanalysis.domains.AbstractState;
import mtanalysis.domains.nonrel.AbstractValue;
import mtanalysis.domains.nonrel.BottomAbstractValue;
import mtanalysis.domains.nonrel.NonRelAbstractDomain;
import mtanalysis.domains.nonrel.NonRelAbstractState;

public class SignAbstDomain extends NonRelAbstractDomain {

	@Override
	public AbstractState bottom(List<Variable> vars) {
		SignAbstState a = new SignAbstState(vars);
		for (Variable v : vars) {
			a.setValue(v, BottomAbstractValue.getInstance());
		}
		return a;
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
	public AbstractState defaultState(List<Variable> vars) {
		return new SignAbstState(vars);
	}

	@Override
	protected AbstractValue nondet_abstract_value(NonDeterministicValue t) {
		return SignAbstValue.TOP;
	}

	@Override
	protected AbstractValue abstract_value(Value v) {
		if (v.getType() == TypeValue.INT || v.getType() == TypeValue.FLOAT) {
			float f = v.getFloatNumber();
			if (f == 0)
				return SignAbstValue.ZERO;
			else if (f > 0)
				return SignAbstValue.POS;
			else
				return SignAbstValue.NEG;
		} else {
			return SignAbstValue.TOP;
		}

	}

	@Override
	protected AbstractValue evaluate_arithm_expression(NonRelAbstractState b, TypeArithOperator operator, Terminal t1,
			Terminal t2) {
		AbstractValue v1 = evaluate_terminal(b, t1);
		AbstractValue v2 = evaluate_terminal(b, t2);

		switch (operator) {
		case ADD:
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
		case SUB:
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
		case DIV:
			if (v2.equals(SignAbstValue.ZERO))
				return BottomAbstractValue.getInstance();
			else if (v1.equals(SignAbstValue.ZERO))
				return SignAbstValue.ZERO;
			else if (v1.equals(SignAbstValue.TOP) || v2.equals(SignAbstValue.TOP))
				return SignAbstValue.TOP;
			else if (v1.equals(v2))
				return SignAbstValue.POS;
			else
				return SignAbstValue.NEG;
		case MUL:
			if (v1.equals(SignAbstValue.TOP) || v2.equals(SignAbstValue.TOP))
				return SignAbstValue.TOP;
			else if (v1.equals(SignAbstValue.ZERO) || v2.equals(SignAbstValue.ZERO))
				return SignAbstValue.ZERO;
			else if (v1.equals(v2))
				return SignAbstValue.POS;
			else
				return SignAbstValue.NEG;
		default:
			throw new UnsupportedOperationException("Invalid operand in arithmetic expression: " + operator);
		}
	}

	@Override
	protected AbstractState reduce_state(NonRelAbstractState nonRel_b, TypeBoolOperator operator, Terminal t1,
			Terminal t2) {
		// TODO : reduce state by boolean expresion
		return nonRel_b;
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
