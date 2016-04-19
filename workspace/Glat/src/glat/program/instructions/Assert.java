package glat.program.instructions;

import glat.program.GlatInstruction;
import glat.program.instructions.expressions.Terminal;

public class Assert extends GlatInstruction {
	public Assert(){
		super();
		type = TypeInst.ASSERT;
	}

	public Assert(Expression e) {
		type = TypeInst.ASSERT;
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
