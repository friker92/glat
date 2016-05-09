package glat.domains.intervals;

import java.util.List;

import glat.domains.AbstractState;
import glat.domains.nonrel.AbstractValue;
import glat.domains.nonrel.NonRelAbstractDomain;
import glat.program.instructions.expressions.terminals.Values;
import glat.program.instructions.expressions.terminals.Variable;
import glat.program.instructions.expressions.terminals.values.NonDeterministicValue;

public class IntervalsAbstDomain extends NonRelAbstractDomain {

	public static double inf = Double.MAX_VALUE;

	@Override
	public AbstractState bottom(List<Variable> vars) {
		return new IntervalsAbstState(vars);
	}

	@Override
	public AbstractState top(List<Variable> vars) {
		IntervalsAbstState a = new IntervalsAbstState(vars);
		AbstractValue topValue = new IntervalsAbstValue(-inf, inf);
		
		for( Variable v : vars ) {
			a.setValue(v, topValue);
		}
		return a;
	}

	@Override
	protected AbstractValue abstract_value(Values v) {
		if (v instanceof NonDeterministicValue) {
			return new IntervalsAbstValue(-inf, inf);
		} else {
			float f = v.getFloatNumber();
			return new IntervalsAbstValue(f, f);
		}
	}

	@Override
	protected AbstractValue evaluate_expression(String operator, List<AbstractValue> abst_values) {

		IntervalsAbstValue op1IntV = (IntervalsAbstValue) abst_values.get(0);
		IntervalsAbstValue op2IntV = (IntervalsAbstValue) abst_values.get(1);

		switch (operator) {
		case "+":

			double l, r;
			if (op1IntV.getLeftLimit() == -inf || op2IntV.getLeftLimit() == -inf) {
				l = -inf;
			} else {
				l = op1IntV.getLeftLimit() + op2IntV.getLeftLimit();
			}

			if (op1IntV.getRightLimit() == inf || op2IntV.getRightLimit() == inf) {
				r = inf;
			} else {
				r = op1IntV.getRightLimit() + op2IntV.getRightLimit();
			}
			return new IntervalsAbstValue(l, r);

		default:
			break;
		}
		return null;
	}

}
