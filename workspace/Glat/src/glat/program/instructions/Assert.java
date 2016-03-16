package glat.program.instructions;

import glat.program.Instruction;
import glat.program.instructions.expressions.Terminal;

public class Assert extends Instruction {
	public Assert(){
		super();
		type = TypeInst.ASSERT;
	}

	public Assert(String opc, Terminal term1, Terminal term2) {
		type = TypeInst.ASSERT;
		expr = new Expression(opc,term1,term2);
	}
	@Override
	public String toString(){
		return type.name()+": "+expr.toString();
	}
	private Expression expr;
}
