package glat.domains.nonrel;

import java.util.Iterator;
import java.util.List;

import glat.domains.AbstractDomain;
import glat.domains.AbstractState;
import glat.domains.BottomState;
import glat.program.Instruction;
import glat.program.instructions.Assignment;
import glat.program.instructions.Assume;
import glat.program.instructions.Expression;
import glat.program.instructions.expressions.CompoundArithExpr;
import glat.program.instructions.expressions.CompoundBoolExpr;
import glat.program.instructions.expressions.Terminal;
import glat.program.instructions.expressions.TypeArithOperator;
import glat.program.instructions.expressions.TypeBoolOperator;
import glat.program.instructions.expressions.terminals.Value;
import glat.program.instructions.expressions.terminals.Variable;
import glat.program.instructions.expressions.terminals.values.NonDeterministicValue;

public abstract class NonRelAbstractDomain implements AbstractDomain {

	@Override
	public AbstractState lub(AbstractState a, AbstractState b) {
		NonRelAbstractState nonRel_a = (NonRelAbstractState) a;
		NonRelAbstractState nonRel_b = (NonRelAbstractState) b;

		List<Variable> vars = a.getVars();

		NonRelAbstractState c = (NonRelAbstractState) defaultState(vars);

		for (Variable v : vars) {
			c.setValue(v, nonRel_a.getValue(v).lub(nonRel_b.getValue(v)));
		}

		return c;
	}

	@Override
	public AbstractState widen(AbstractState a, AbstractState b) {
		NonRelAbstractState nonRel_a = (NonRelAbstractState) a;
		NonRelAbstractState nonRel_b = (NonRelAbstractState) b;

		List<Variable> vars = a.getVars();

		NonRelAbstractState c = (NonRelAbstractState) bottom(vars);

		for (Variable v : vars) {
			c.setValue(v, nonRel_a.getValue(v).widen(nonRel_b.getValue(v)));
		}

		return c;
	}
	
	@Override
	public boolean lte(AbstractState a, AbstractState b) {
		NonRelAbstractState nonRel_a = (NonRelAbstractState) a;
		NonRelAbstractState nonRel_b = (NonRelAbstractState) b;

		List<Variable> vars = a.getVars();

		for (Variable v : vars) {
			if (!nonRel_a.getValue(v).lte(nonRel_b.getValue(v))) {
				return false;
			}
		}

		return true;
	}

	@Override
	public AbstractState exec(Instruction instr, AbstractState a) {

		NonRelAbstractState nonRel_a = (NonRelAbstractState) a;
		Expression e;
		
		switch (instr.getType()) {
		case ASSIGNMENT: 
			Assignment assignInstr = (Assignment) instr;
			e = assignInstr.getExpr();
			NonRelAbstractState nonRel_b = (NonRelAbstractState) nonRel_a.copy();
			AbstractValue res = evaluate_arithm_expression(nonRel_a, e);
			if ( res instanceof BottomAbstractValue ) { 
				return new BottomState();
			} else {
				nonRel_b.setValue(assignInstr.getDest(), res);
				return nonRel_b;
			}
		case ASSUME:
			Assume assumeInstr = (Assume) instr;
			e = assumeInstr.getExpr();
			return reduce_state(nonRel_a, e);
		case ASSERT:
			return a; // TODO: check this
		default:
			throw new UnsupportedOperationException("Invalid instruction: "+instr);
		}
	}


	protected AbstractValue evaluate_arithm_expression(NonRelAbstractState b, Expression exp) {
		if (exp instanceof CompoundArithExpr) {
			CompoundArithExpr compundExp = (CompoundArithExpr) exp;
			return evaluate_arithm_expression(b, compundExp.getOperator(), compundExp.getOperandLeft(), compundExp.getOperandRight());	
		} else {
			return evaluate_terminal(b, (Terminal) exp);
		}
	}
	
	protected AbstractState reduce_state(NonRelAbstractState b, Expression exp) {
		if (exp instanceof CompoundBoolExpr) {
			CompoundBoolExpr compundExp = (CompoundBoolExpr) exp;
			return reduce_state(b, compundExp.getOperator(), compundExp.getOperandLeft(), compundExp.getOperandRight());	
		} 
		return new BottomState();
	}
	
	protected AbstractValue evaluate_terminal(NonRelAbstractState a, Terminal t) {
		if (t instanceof Variable) {
			return a.getValue((Variable) t);
		} else if (t instanceof NonDeterministicValue) {
			return nondet_abstract_value( (NonDeterministicValue) t);
		} else {
			return abstract_value( (Value) t);
		}
	}

	/**
	 * Returns a non-deterministic value that correspond to t 
	 * @param t
	 * @return
	 */
	protected abstract AbstractValue nondet_abstract_value(NonDeterministicValue t);
	
	/**
	 * Returns an abstraction of t
	 * @param t
	 * @return
	 */
	protected abstract AbstractValue abstract_value(Value t);
	
	/**
	 * returns a restricted abstract state, depending on the condition
	 * 
	 * @param nonRel_b
	 * @param e
	 * @return
	 */
	protected abstract AbstractState reduce_state(NonRelAbstractState nonRel_b, TypeBoolOperator operator, Terminal t1, Terminal t2);
	
	/**
	 * Returns a new abstract value the corresponds to the evaluation of the expression 
	 * 
	 * @param b
	 * @param operator
	 * @param t1
	 * @param t2
	 * @return
	 */
	protected abstract AbstractValue evaluate_arithm_expression(NonRelAbstractState b, TypeArithOperator operator, Terminal t1, Terminal t2);

	@Override
	public AbstractState rename(AbstractState st, AbstractState bt, List<Variable> current, List<Variable> newValue){
		if(current.size() != newValue.size()) // TODO: exception params
			throw new ExceptionInInitializerError();
		NonRelAbstractState nonRel_st = (NonRelAbstractState) st;
		NonRelAbstractState nonRel_copy = (NonRelAbstractState) bt.copy();
		
		Iterator<Variable> it_curr = current.iterator(), it_new = newValue.iterator();
		while (it_curr.hasNext() && it_new.hasNext()) {
			nonRel_copy.setValue(it_new.next(), nonRel_st.getValue(it_curr.next()));
		}
		return nonRel_copy;
	}

}
