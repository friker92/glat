package glat.program.instructions;

import glat.program.GlatInstruction;
import glat.program.instructions.expressions.Terminal;

public class Assume extends GlatInstruction {
	public Assume(){
		super();
		type = TypeInst.ASSUME;
	}
	public Assume(Expression e) {
		type = TypeInst.ASSUME;
		expr = e;
	}
	@Override
	public String toString(){
		return type.name()+": "+expr.toString();
	}
	public Expression getExpr(){
		return expr;
	}
	private Expression expr;
}
