package glat.program.instructions;

import glat.program.Instruction;
import glat.program.instructions.expressions.Terminal;

public class Return extends Instruction {
	public Return(){
		super();
		type = InstType.RETURN;
	}
	public Return(Terminal v){
		var = v;
		type = InstType.RETURN;
	}

	@Override
	public String toString() {
		return type.name()+": "+var.toString();
	}

	public Terminal getVar(){
		return var;
	}
	private Terminal var;
}
