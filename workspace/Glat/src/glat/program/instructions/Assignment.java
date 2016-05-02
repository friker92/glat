package glat.program.instructions;

import glat.program.GlatInstruction;
import glat.program.instructions.expressions.Terminal;
import glat.program.instructions.expressions.terminals.Variable;

public class Assignment extends GlatInstruction {

	public Assignment(Variable d, Terminal e) {
		type = TypeInst.ASSIGNMENT;
		expr = e;
		dest = d;
	}

	@Override
	public String toString(){
		return type.name()+": "+dest.toString()+" := "+expr.toString();
	}
	
	public Terminal getExpr(){
		return expr;
	}
	
	public Variable getDest(){
		return dest;
	}

	private Terminal expr;
	private Variable dest;

}
