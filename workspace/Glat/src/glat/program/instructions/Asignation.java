package glat.program.instructions;

import glat.program.Instruction;
import glat.program.instructions.expressions.terminals.Variable;

public class Asignation extends Instruction {

	public Asignation(Variable d, Expression e) {
		type = InstType.ASIGNATION;
		expr = e;
		dest = d;
	}

	@Override
	public String toString(){
		return type.name()+": "+dest.toString()+" := "+expr.toString();
	}
	
	public Expression getExpr(){
		return expr;
	}
	
	public Variable getDest(){
		return dest;
	}

	private Expression expr;
	private Variable dest;

}
