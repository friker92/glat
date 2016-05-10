package glat.domains.constprop;

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

public class ConstPropDomain extends NonRelAbstractDomain {

	@Override
	public AbstractState top(List<Variable> vars) {
		ConsPropAbstState a = new ConsPropAbstState(vars);
		ConsPropTOP top = new ConsPropTOP();
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
		return new ConsPropTOP();
	}

	@Override
	protected AbstractValue abstract_value(Value t) {
		return new ConsPropAbstValue(t.getFloatNumber());
	}

	@Override
	protected AbstractValue evaluate_expression(TypeOperator operator, List<AbstractValue> abst_values) {

		if ( abst_values.contains( new ConsPropTOP() ) ) {
			return new ConsPropTOP();
		}

		if ( abst_values.contains( new ConsPropBOT() ) ) {
			return new ConsPropBOT();
		}

		double res = 0.0;
		
		switch (operator) {
		case ADD:
			for( AbstractValue v : abst_values ) {
				res += ((ConsPropAbstValue) v).getValue();
			}
			break;
		default:
			break;
		}
		
		return new ConsPropAbstValue(res);
	}

	@Override
	public AbstractState widen(AbstractState a, AbstractState b) {
		return lub(a,b);
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
	protected AbstractState evaluate_boolean_expression(NonRelAbstractState nonRel_b, Expression e) {
		// TODO Auto-generated method stub
		return null;
	}
}
