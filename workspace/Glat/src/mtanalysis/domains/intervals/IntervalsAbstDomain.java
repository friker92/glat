package mtanalysis.domains.intervals;

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
	protected AbstractValue evaluate_arithm_expression(NonRelAbstractState b, TypeArithOperator operator, Terminal t1, Terminal t2) {
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



	
	protected AbstractValue reduce_state(NonRelAbstractState nonRel_b, TypeBoolOperator operator, Variable t1, Terminal t2) {
		IntervalsAbstValue v1 = (IntervalsAbstValue) evaluate_terminal(nonRel_b, t1);
		IntervalsAbstValue v2 = (IntervalsAbstValue) evaluate_terminal(nonRel_b, t2);
		
		switch(operator){
		case EQ:
			if ( v1.getRightLimit() < v2.getRightLimit() )
				return new IntervalsAbstValue(1,-1);
			if ( v1.getLeftLimit() > v2.getLeftLimit() )
				return new IntervalsAbstValue(1,-1);
			return new IntervalsAbstValue(v2.getLeftLimit(),v2.getRightLimit());
		case GT: // TODO : problem with = 
			if ( v1.getRightLimit() <= v2.getRightLimit() )
				return new IntervalsAbstValue(1,-1);
			return new IntervalsAbstValue(Math.max(v1.getLeftLimit(),v2.getLeftLimit()),v1.getRightLimit());
		case GTE:
			if ( v1.getRightLimit() < v2.getRightLimit() )
				return new IntervalsAbstValue(1,-1);
			return new IntervalsAbstValue(Math.max(v1.getLeftLimit(),v2.getLeftLimit()),v1.getRightLimit());
		case LT: // TODO : problem with = 
			if ( v1.getLeftLimit() >= v2.getLeftLimit() )
				return new IntervalsAbstValue(1,-1);
			return new IntervalsAbstValue(v1.getLeftLimit(),Math.min(v1.getRightLimit(),v2.getRightLimit()));
		case LTE:
			if ( v1.getLeftLimit() > v2.getLeftLimit() )
				return new IntervalsAbstValue(1,-1);
			return new IntervalsAbstValue(v1.getLeftLimit(),Math.min(v1.getRightLimit(),v2.getRightLimit()));
		case NEQ:// TODO : how to create two possible value?
			// x = [ -3 , 3]
			// reduce by ( x != 0 )
			// x  = [-3, 0 ) U (0, 3]
			break;
		default:
			break;
		
		}

		return v1;
	}
	@Override
	protected AbstractState reduce_state(NonRelAbstractState nonRel_b, TypeBoolOperator operator, Terminal t1, Terminal t2) {
		NonRelAbstractState copy_b = (NonRelAbstractState) nonRel_b.copy();
		boolean change = false;
		AbstractValue v;
		if( t1 instanceof Variable){
			v = reduce_state(copy_b,operator,(Variable)t1,t2);
			copy_b.setValue((Variable)t1, v);
			change = true;
		}
		if( t2 instanceof Variable){
			v = reduce_state(copy_b,operator.flip(),(Variable)t2,t1);
			copy_b.setValue((Variable)t2, v);
			change = true;
		}
		if(change)
			return copy_b;
		else
			return nonRel_b;
	}

	@Override
	public AbstractState defaultState(List<Variable> vars) {
		return new IntervalsAbstState(vars);
	}





}
