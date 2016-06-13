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
	@Override
	protected AbstractValue evaluate_arithm_expression(NonRelAbstractState b, TypeOperator operator, Terminal t1, Terminal t2) {
		IntervalsAbstValue op1IntV = (IntervalsAbstValue) evaluate_terminal(b, t1);
		IntervalsAbstValue op2IntV = (IntervalsAbstValue) evaluate_terminal(b, t2);
		if (op1IntV.isBottom() || op2IntV.isBottom())
			return new IntervalsAbstValue(1, -1); // TODO: bottom value or error
			
		double l, r;
		
		switch (operator) {
		case ADD:

			if (op1IntV.getLeftLimit() == -IntervalsAbstValue.inf || op2IntV.getLeftLimit() == -IntervalsAbstValue.inf) {
				l = -IntervalsAbstValue.inf;
			} else {
				l = op1IntV.getLeftLimit() + op2IntV.getLeftLimit();
			}
			if(op1IntV.getLeftLimit() > 0 &&  op2IntV.getLeftLimit() > 0 && (l < 0 || l > IntervalsAbstValue.inf) ){ // overflow
				l = IntervalsAbstValue.inf;
			} else if(op1IntV.getLeftLimit() < 0 &&  op2IntV.getLeftLimit() < 0 && (l > 0 || l < -IntervalsAbstValue.inf)){ // "under"flow
				l = -IntervalsAbstValue.inf;
			}
			
			if (op1IntV.getRightLimit() == IntervalsAbstValue.inf || op2IntV.getRightLimit() == IntervalsAbstValue.inf) {
				r = IntervalsAbstValue.inf;
			} else {
				r = op1IntV.getRightLimit() + op2IntV.getRightLimit();
			}
			
			if(op1IntV.getRightLimit() > 0 &&  op2IntV.getRightLimit() > 0 && (r < 0 || r > IntervalsAbstValue.inf)){ // overflow
				r = IntervalsAbstValue.inf;
			} else if(op1IntV.getRightLimit() < 0 &&  op2IntV.getRightLimit() < 0 && (r > 0 || r < -IntervalsAbstValue.inf)){ // "under"flow
				r = -IntervalsAbstValue.inf;
			}
			return new IntervalsAbstValue(l, r);
		case SUB:

			if (op1IntV.getLeftLimit() == -IntervalsAbstValue.inf || op2IntV.getRightLimit() == IntervalsAbstValue.inf) {
				l = -IntervalsAbstValue.inf;
			} else {
				l = op1IntV.getLeftLimit() - op2IntV.getRightLimit();
			}
			if(op1IntV.getLeftLimit() > 0 &&  op2IntV.getRightLimit() < 0 && (l < 0 || l > IntervalsAbstValue.inf) ){ // overflow
				l = IntervalsAbstValue.inf;
			} else if(op1IntV.getLeftLimit() < 0 &&  op2IntV.getRightLimit() > 0 && (l > 0 || l < -IntervalsAbstValue.inf)){ // "under"flow
				l = -IntervalsAbstValue.inf;
			}
			
			if (op1IntV.getRightLimit() == IntervalsAbstValue.inf || op2IntV.getLeftLimit() == -IntervalsAbstValue.inf) {
				r = IntervalsAbstValue.inf;
			} else {
				r = op1IntV.getRightLimit() - op2IntV.getLeftLimit();
			}
			
			if(op1IntV.getRightLimit() > 0 &&  op2IntV.getLeftLimit() < 0 && (r < 0 || r > IntervalsAbstValue.inf)){ // overflow
				r = IntervalsAbstValue.inf;
			} else if(op1IntV.getRightLimit() < 0 &&  op2IntV.getLeftLimit() > 0 && (r > 0 || r < -IntervalsAbstValue.inf)){ // "under"flow
				r = -IntervalsAbstValue.inf;
			}
			return new IntervalsAbstValue(l, r);
			
		case MUL:
			
			if (op1IntV.getLeftLimit() == -IntervalsAbstValue.inf || op2IntV.getLeftLimit() == -IntervalsAbstValue.inf) {
				l = -IntervalsAbstValue.inf;
			} else {
				l = op1IntV.getLeftLimit() * op2IntV.getLeftLimit();
			}
			if((op1IntV.getLeftLimit() > 0 &&  op2IntV.getLeftLimit() > 0) // + * +
					|| ( op1IntV.getLeftLimit() < 0 &&  op2IntV.getLeftLimit() < 0 )){ // - * -
				if (l < 0 || l > IntervalsAbstValue.inf) { // overflow
					l = IntervalsAbstValue.inf;
				}
			} else { // + * - || - * + 
				if(l > 0 || l < -IntervalsAbstValue.inf){  // "under"flow
					l = -IntervalsAbstValue.inf;
				}
			}
			
			if (op1IntV.getRightLimit() == IntervalsAbstValue.inf || op2IntV.getRightLimit() == IntervalsAbstValue.inf) {
				r = IntervalsAbstValue.inf;
			} else {
				r = op1IntV.getRightLimit() * op2IntV.getRightLimit();
			}
			
			if((op1IntV.getRightLimit() > 0 &&  op2IntV.getRightLimit() > 0) // + * +
					|| ( op1IntV.getRightLimit() < 0 &&  op2IntV.getRightLimit() < 0 )){ // - * -
				if (l < 0 || l > IntervalsAbstValue.inf) { // overflow
					l = IntervalsAbstValue.inf;
				}
			} else {// + * - || - * + 
				if(l > 0 || l < -IntervalsAbstValue.inf){ // "under"flow
					l = -IntervalsAbstValue.inf;
				}
			}
			return new IntervalsAbstValue(l, r);
			
		case DIV:
			if(op2IntV.getLeftLimit() < 0 && op2IntV.getRightLimit() > 0)
				return new IntervalsAbstValue(1, -1); // TODO: bottom value or error
			
			if (op1IntV.getLeftLimit() == -IntervalsAbstValue.inf || op2IntV.getRightLimit() == IntervalsAbstValue.inf) {
				l = -IntervalsAbstValue.inf;
			} else {
				l = op1IntV.getLeftLimit() / op2IntV.getRightLimit();
			}			
		
			if((op1IntV.getLeftLimit() > 0 &&  op2IntV.getLeftLimit() > 0) // + / +
					|| ( op1IntV.getLeftLimit() < 0 &&  op2IntV.getLeftLimit() < 0 )){ // - / -
				if (l < 0 || l > IntervalsAbstValue.inf) { // overflow
					l = IntervalsAbstValue.inf;
				}
			} else { // + / - || - / +
				if(l > 0 || l < -IntervalsAbstValue.inf){  // "under"flow
					l = -IntervalsAbstValue.inf;
				}
			}
			
			if (op1IntV.getRightLimit() == IntervalsAbstValue.inf || op2IntV.getLeftLimit() == -IntervalsAbstValue.inf) {
				r = IntervalsAbstValue.inf;
			} else {
				r = op1IntV.getRightLimit() / op2IntV.getLeftLimit();
			}
			
			if((op1IntV.getRightLimit() > 0 &&  op2IntV.getRightLimit() > 0) // + / +
					|| ( op1IntV.getRightLimit() < 0 &&  op2IntV.getRightLimit() < 0 )) {// - / -
				if (l < 0 || l > IntervalsAbstValue.inf) { // overflow
					l = IntervalsAbstValue.inf;
				}
			} else {// + / - || - / + 
				if(l > 0 || l < -IntervalsAbstValue.inf){ // "under"flow
					l = -IntervalsAbstValue.inf;
				}
			}
			
			return new IntervalsAbstValue(l, r);
		}
		throw new UnsupportedOperationException("Complete the cases of atrith operations for INTERVALS");
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



}
