package glat.domains.intervals;

import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

import glat.domains.AbstractState;
import glat.domains.nonrel.AbstractValue;
import glat.domains.nonrel.NonRelAbstractDomain;
import glat.domains.nonrel.NonRelAbstractState;
import glat.program.instructions.Expression;
import glat.program.instructions.expressions.Terminal;
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

	protected AbstractValue evaluate_arithm_expression(TypeOperator operator, List<AbstractValue> abst_values) {

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
		if (e instanceof Terminal) {
			throw new UnsupportedOperationException("Boolean cannot be a terminal");
		} else {
			return nonRel_b;
		}
	}

	@Override
	public AbstractState defaultState(List<Variable> vars) {
		return new IntervalsAbstState(vars);
	}

	@Override
	protected AbstractValue evaluate_arithm_expression(NonRelAbstractState b, TypeOperator operator, Terminal t1, Terminal t2) {
		IntervalsAbstValue op1IntV = (IntervalsAbstValue) evaluate_terminal(b, t1);
		IntervalsAbstValue op2IntV = (IntervalsAbstValue) evaluate_terminal(b, t2);

		double l, r;

		switch (operator) {
		case ADD:

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
		}
		throw new UnsupportedOperationException("Complete the cases of atrith operations for INTERVALS");
	}

}
