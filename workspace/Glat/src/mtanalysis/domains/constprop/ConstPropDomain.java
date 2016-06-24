package mtanalysis.domains.constprop;

import java.util.List;

import glat.program.instructions.expressions.Terminal;
import glat.program.instructions.expressions.TypeArithOperator;
import glat.program.instructions.expressions.TypeBoolOperator;
import glat.program.instructions.expressions.terminals.Value;
import glat.program.instructions.expressions.terminals.Variable;
import glat.program.instructions.expressions.terminals.values.NonDeterministicValue;
import mtanalysis.domains.AbstractState;
import mtanalysis.domains.nonrel.AbstractValue;
import mtanalysis.domains.nonrel.NonRelAbstractDomain;
import mtanalysis.domains.nonrel.NonRelAbstractState;

public class ConstPropDomain extends NonRelAbstractDomain {

	@Override
	public AbstractState top(List<Variable> vars) {
		ConsPropAbstState a = new ConsPropAbstState(vars);
		ConsPropTOP top = ConsPropTOP.getInstance();
		for (Variable v : vars) {
			a.setValue(v, top);
		}
		return a;
	}

	@Override
	public AbstractState bottom(List<Variable> vars) {
		return new ConsPropAbstState(vars);
	}

	@Override
	protected AbstractValue nondet_abstract_value(NonDeterministicValue t) {
		return ConsPropTOP.getInstance();
	}

	@Override
	protected AbstractValue abstract_value(Value t) {
		return new ConsPropValue(t.getFloatNumber());
	}

	@Override
	public AbstractState widen(AbstractState a, AbstractState b) {
		return lub(a, b);
	}

	@Override
	public boolean hasInfiniteAscendingChains() {
		return false;
	}

	@Override
	public boolean hasInfiniteDescendingChains() {
		return false;
	}

	@Override
	protected AbstractState reduce_state(NonRelAbstractState nonRel_b, TypeBoolOperator operator, Terminal t1, Terminal t2) {
		return nonRel_b;
	}

	@Override
	public AbstractState defaultState(List<Variable> vars) {
		return new ConsPropAbstState(vars);
	}

	@Override
	protected AbstractValue evaluate_arithm_expression(NonRelAbstractState b, TypeArithOperator operator, Terminal t1,
			Terminal t2) {

		ConsPropAbstValue v1 = (ConsPropAbstValue) evaluate_terminal(b, t1);
		ConsPropAbstValue v2 = (ConsPropAbstValue) evaluate_terminal(b, t2);

		if (v1.equals(ConsPropTOP.getInstance()) || v2.equals(ConsPropTOP.getInstance())) {
			return ConsPropTOP.getInstance();
		}
		if (v1.equals(ConsPropBOT.getInstance()) || v2.equals(ConsPropBOT.getInstance())) {
			return ConsPropBOT.getInstance();
		}

		double res = 0.0;
		double valueV1 = ((ConsPropValue) v1).getValue();
		double valueV2 = ((ConsPropValue) v2).getValue();

		switch (operator) {
		case ADD:
			res = valueV1 + valueV2;
			break;
		case SUB:
			res = valueV1 - valueV2;
			break;
		case DIV:
			if(valueV2 == 0)
				return ConsPropBOT.getInstance();
			res = valueV1 / (1.0 * valueV2);
			break;
		case MUL:
			res = valueV1 * valueV2;
			break;
		default:
			break;
		}

		return new ConsPropValue(res);
	}
}
