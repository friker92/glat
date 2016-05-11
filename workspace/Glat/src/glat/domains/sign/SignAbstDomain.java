package glat.domains.sign;

import java.util.List;

import glat.domains.AbstractState;
import glat.domains.BottomState;
import glat.domains.nonrel.AbstractValue;
import glat.domains.nonrel.NonRelAbstractDomain;
import glat.domains.nonrel.NonRelAbstractState;
import glat.program.instructions.Expression;
import glat.program.instructions.expressions.CompoundExpr;
import glat.program.instructions.expressions.Terminal;
import glat.program.instructions.expressions.TypeOperator;
import glat.program.instructions.expressions.TypeValue;
import glat.program.instructions.expressions.terminals.Value;
import glat.program.instructions.expressions.terminals.Variable;
import glat.program.instructions.expressions.terminals.values.NonDeterministicValue;

public class SignAbstDomain extends NonRelAbstractDomain {

	@Override
	public AbstractState bottom(List<Variable> vars) {
		return new BottomState(vars);
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
	protected AbstractValue evaluate_arithm_expression(NonRelAbstractState b, TypeOperator operator, Terminal t1,
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
				return null;
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

	protected AbstractState evaluate_boolean_expression(NonRelAbstractState b, TypeOperator op, Variable v1, Variable v2) {
		switch (op) {
		case EQ:
			// handle
			break;
		case GT:
			// handle
			break;
		case GTE:
			// handle
			break;
		case LT:
			return evaluate_boolean_expression(b, TypeOperator.GT, v2, v1);
		case LTE:
			return evaluate_boolean_expression(b, TypeOperator.GTE, v2, v1);
		case NEQ:
			// handle
			break;
		default:
			break;
		}
		return null;
	}

	protected AbstractState evaluate_boolean_expression(NonRelAbstractState b, TypeOperator op, Variable v1, Value v2) {
		switch (op) {
		case EQ:
			// handle
			break;
		case GT:
			// handle
			break;
		case GTE:
			// handle
			break;
		case LT:
			// handle
			break;
		case LTE:
			// handle
			break;
		case NEQ:
			// handle
			break;
		default:
			break;
		}
		return null;
	}

	protected AbstractState evaluate_boolean_expression(NonRelAbstractState b, TypeOperator op, Value v1, Value v2) {
		switch (op) {
		case EQ:
			// handle
			break;
		case GT:
			// handle
			break;
		case GTE:
			// handle
			break;
		case LT:
			return evaluate_boolean_expression(b, TypeOperator.GT, v2, v1);
		case LTE:
			return evaluate_boolean_expression(b, TypeOperator.GTE, v2, v1);
		case NEQ:
			// handle
			break;
		default:
			break;
		}
		return null;
	}

	@Override
	protected AbstractState evaluate_boolean_expression(NonRelAbstractState b, Expression e) {
		if (e instanceof Terminal) {
			return b;
		}

		NonRelAbstractState a = (NonRelAbstractState) b.copy();

		CompoundExpr compExp_e = (CompoundExpr) e;
		TypeOperator op = compExp_e.getOperator();
		Variable op1 = (Variable) compExp_e.getOperandLeft();
		SignAbstValue v1 = (SignAbstValue) evaluate_arithm_expression(b, op1);
		SignAbstValue v2 = (SignAbstValue) evaluate_arithm_expression(b, compExp_e.getOperandRight());

		switch (op) {
		case EQ:
			break;
		case GT:
			break;
		case GTE:
			if (v1.equals(v2)) {
				return a;
			}

			if (v2.equals(SignAbstValue.TOP)) {
				return a;
			}

			switch (v1) {
			case NEG:
				return new BottomState();
			case POS:
				return a;
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
			break;
		case LT:
			break;
		case LTE:
			break;
		case NEQ:
			break;
		default:
			break;
		}

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
