package glat.program.instructions;

import glat.program.Instruction;
import glat.program.instructions.expressions.Terminal;
import glat.program.instructions.expressions.terminals.Variable;

public class Lock extends Instruction {
	public Lock(){
		super();
		type = InstType.LOCK;
	}

	public Lock(String ty,Variable v){
		var = v;
		lock = (ty.equals("lock"))?true:false;
		type = (lock)?InstType.LOCK:InstType.UNLOCK;
	}

	@Override
	public String toString() {
		return type.name()+": "+var.toString();
	}

	public Terminal getVar(){
		return var;
	}
	private Variable var;
	private boolean lock;
}