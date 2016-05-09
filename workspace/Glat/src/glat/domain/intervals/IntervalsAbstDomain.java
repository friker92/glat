package glat.domain.intervals;

import java.util.List;

import glat.domain.AbstractDomain;
import glat.domain.AbstractState;
import glat.domain.nonrel.AbstractValue;
import glat.domain.nonrel.NonRelAbstractDomain;
import glat.domain.nonrel.NonRelAbstractState;
import glat.domain.sign.SignAbstState;
import glat.domain.sign.SignAbstValue;
import glat.program.Instruction;
import glat.program.instructions.Assignment;
import glat.program.instructions.Expression;
import glat.program.instructions.expressions.CompoundExpr;
import glat.program.instructions.expressions.Terminal;
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
	public AbstractState exec(Instruction intsr, AbstractState a) {
		NonRelAbstractState nonRel_a = (NonRelAbstractState) a;
		NonRelAbstractState nonRel_b = (NonRelAbstractState) a.copy();

		switch (intsr.getType()) {
		case ASSIGNMENT:
			Assignment assignInstr = (Assignment) intsr;
			Expression e = assignInstr.getExpr();
			nonRel_b.setValue(assignInstr.getDest(), exprV(nonRel_b, e));
			break;
		default:
			break;
		}

		return nonRel_b;

	}

	private AbstractValue exprV(AbstractState b, Expression e) {
		if (e instanceof CompoundExpr) {
			CompoundExpr exp = (CompoundExpr) e;
			if (exp.getOperands().size() == 1)
				return exprV(b, exp.getOperand(0));
			else
				return op(b, exp.getOperator(), exprV(b, exp.getOperand(0)), exprV(b, exp.getOperand(1)));
		} else {
			return getV(b, (Terminal) e);
		}
	}

	private IntervalsAbstValue getV(AbstractState a, Terminal t) {
		NonRelAbstractState nonRel_a = (NonRelAbstractState) a;

		if (t instanceof Variable) {
			return (IntervalsAbstValue) nonRel_a.getValue((Variable) t);
		} else {
			Values v = (Values) t;
			if (v instanceof NonDeterministicValue) {
				return new IntervalsAbstValue(-inf, inf);
			} else {
				float f = v.getFloatNumber();
				return new IntervalsAbstValue(f,f);
			}
		}
	}

	private AbstractValue op(AbstractState b, String operator, AbstractValue op1, AbstractValue op2) {
		
		IntervalsAbstValue op1IntV = (IntervalsAbstValue) op1;
		IntervalsAbstValue op2IntV = (IntervalsAbstValue) op2;
		
		
		switch (operator) {
		case "+":
			
			double l,r;
			if ( op1IntV.getLeftLimit() == -inf || op2IntV.getLeftLimit() == -inf ) {
				l = -inf;
			} else {
				l = op1IntV.getLeftLimit()+op2IntV.getLeftLimit();
			}
			
			if ( op1IntV.getRightLimit() == inf || op2IntV.getRightLimit() == inf ) {
				r = inf;
			} else {
				r = op1IntV.getRightLimit()+op2IntV.getRightLimit();
			}
			return new IntervalsAbstValue(l,r);

		default:
			break;
		}
		return null;
	}

	@Override
	public AbstractState top(List<Variable> vars) {
		// TODO Auto-generated method stub
		return null;
	}

}
