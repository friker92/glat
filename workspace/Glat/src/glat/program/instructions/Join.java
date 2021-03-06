package glat.program.instructions;

import glat.program.GlatInstruction;
import glat.program.instructions.expressions.Terminal;
import glat.program.instructions.expressions.terminals.Variable;

public class Join extends GlatInstruction {
	public Join(){
		super();
		type = TypeInst.JOIN;
	}
	public Join(Variable v){
		var = v;
		type = TypeInst.JOIN;
	}

	@Override
	public String toString() {
		return type.name()+": "+var.toString();
	}

	public Terminal getVar(){
		return var;
	}
	private Variable var;


}
