package glat.program.instructions;

import glat.program.GlatInstruction;
import glat.program.instructions.expressions.Terminal;

public class Assume extends GlatInstruction {
	public Assume(){
		super();
		type = TypeInst.ASSUME;
	}
	public Assume(String opc, Terminal term1, Terminal term2) {
		type = TypeInst.ASSUME;
		expr = new Expression(opc,term1,term2);
	}
	@Override
	public String toString(){
		return type.name()+": "+expr.toString();
	}
	private Expression expr;
}
