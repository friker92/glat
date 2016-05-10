package glat.domains.intervals;

import java.util.List;

import glat.domains.AbstractState;
import glat.domains.nonrel.AbstractValue;
import glat.domains.nonrel.NonRelAbstractDomain;
import glat.domains.nonrel.NonRelAbstractState;
import glat.program.instructions.Expression;
import glat.program.instructions.expressions.TypeOperator;
import glat.program.instructions.expressions.terminals.Value;
import glat.program.instructions.expressions.terminals.Variable;
import glat.program.instructions.expressions.terminals.values.NonDeterministicValue;

public class IntervalsAbstDomain extends NonRelAbstractDomain {


	@Override
	public AbstractState bottom(List<Variable> vars) {
		return new IntervalsAbstState(vars);
	}

	@Override
	public AbstractState top(List<Variable> vars) {
		IntervalsAbstState a = new IntervalsAbstState(vars);
		AbstractValue topValue = new IntervalsAbstValue(-IntervalsAbstValue.inf, IntervalsAbstValue.inf);

		for (Variable v : vars) {
			a.setValue(v, topValue);
		}
		return a;
	}

	@Override
	protected AbstractValue nondet_abstract_value(NonDeterministicValue t) {
		return new IntervalsAbstValue(-IntervalsAbstValue.inf, IntervalsAbstValue.inf);
	}

	@Override
	protected AbstractValue abstract_value(Value v) {
		float f = v.getFloatNumber();
		return new IntervalsAbstValue(f, f);
	}

	@Override
	protected AbstractValue evaluate_expression(TypeOperator operator, List<AbstractValue> abst_values) {

		IntervalsAbstValue op1IntV = (IntervalsAbstValue) abst_values.get(0);
		IntervalsAbstValue op2IntV = (IntervalsAbstValue) abst_values.get(1);

		switch (operator) {
		case ADD:

			double l, r;
			if (op1IntV.getLeftLimit() == -IntervalsAbstValue.inf || op2IntV.getLeftLimit() == -IntervalsAbstValue.inf) {
				l = -IntervalsAbstValue.inf;
			} else {
				l = op1IntV.getLeftLimit() + op2IntV.getLeftLimit();
			}

			if (op1IntV.getRightLimit() == IntervalsAbstValue.inf || op2IntV.getRightLimit() == IntervalsAbstValue.inf) {
				r = IntervalsAbstValue.inf;
			} else {
				r = op1IntV.getRightLimit() + op2IntV.getRightLimit();
			}
			return new IntervalsAbstValue(l, r);

		default:
			break;
		}
		return null;
	}

	@Override
	public boolean hasInfiniteAscendingChains() {
		return true;
	}

	@Override
	public boolean hasInfiniteDescendingChains() {
		return true;
	}

	@Override
	protected AbstractState evaluate_boolean_expression(NonRelAbstractState nonRel_b, Expression e) {
		// TODO Auto-generated method stub
		return null;
	}

}
