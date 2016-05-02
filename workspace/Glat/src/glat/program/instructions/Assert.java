package glat.program.instructions;

import glat.program.GlatInstruction;
import glat.program.instructions.expressions.Terminal;

public class Assert extends GlatInstruction {
	public Assert(){
		super();
		type = TypeInst.ASSERT;
	}

	public Assert(Terminal e) {
		type = TypeInst.ASSERT;
		expr = e;
	}
	@Override
	public String toString(){
		return type.name()+": "+expr.toString();
	}
	public Terminal getExpr(){
		return expr;
	}
	private Terminal expr;
}
