package glat.domains.nonrel;

import java.util.ArrayList;
import java.util.List;

import glat.domains.AbstractDomain;
import glat.domains.AbstractState;
import glat.program.Instruction;
import glat.program.instructions.Assignment;
import glat.program.instructions.Expression;
import glat.program.instructions.expressions.CompoundExpr;
import glat.program.instructions.expressions.Terminal;
import glat.program.instructions.expressions.terminals.Values;
import glat.program.instructions.expressions.terminals.Variable;

public abstract class NonRelAbstractDomain implements AbstractDomain {

	@Override
	public AbstractState lub(AbstractState a, AbstractState b) {
		NonRelAbstractState nonRel_a = (NonRelAbstractState) a;
		NonRelAbstractState nonRel_b = (NonRelAbstractState) b;

		List<Variable> vars = a.getVars();

		NonRelAbstractState c = (NonRelAbstractState) bottom(vars);

		for (Variable v : vars) {
			c.setValue(v, nonRel_a.getValue(v).lub(nonRel_b.getValue(v)));
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
	public AbstractState exec(Instruction intsr, AbstractState a) {

		NonRelAbstractState nonRel_a = (NonRelAbstractState) a;
		NonRelAbstractState nonRel_b = (NonRelAbstractState) nonRel_a.copy();

		switch (intsr.getType()) {
		case ASSIGNMENT:
			Assignment assignInstr = (Assignment) intsr;
			Expression e = assignInstr.getExpr();
			nonRel_b.setValue(assignInstr.getDest(), evaluate_expression(nonRel_b, e));
			break;
		default:
			break;
		}

		return nonRel_b;
	}

	protected AbstractValue evaluate_expression(NonRelAbstractState b, Expression exp) {
		if (exp instanceof CompoundExpr) {
			CompoundExpr compundExp = (CompoundExpr) exp;
			List<AbstractValue> abst_values = new ArrayList<AbstractValue>();
			for (Expression e : compundExp.getOperands()) {
				abst_values.add(evaluate_expression(b, e));
			}
			return evaluate_expression(compundExp.getOperator(), abst_values);	
		} else {
			return evaluate_terminal(b, (Terminal) exp);
		}
	}
	
	private AbstractValue evaluate_terminal(NonRelAbstractState a, Terminal t) {
	
		if (t instanceof Variable) {
			return a.getValue((Variable) t);
		} else {
			return abstract_value( (Values) t);
		}
	}
	
	protected abstract AbstractValue abstract_value(Values t);
	protected abstract AbstractValue evaluate_expression(String operator, List<AbstractValue> abst_values);


}
