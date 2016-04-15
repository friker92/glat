package glat.program.instructions;

import glat.program.GlatInstruction;
import glat.program.instructions.expressions.Terminal;
import glat.program.instructions.expressions.terminals.Variable;

public class Lock extends GlatInstruction {
	public Lock(){
		super();
		type = TypeInst.LOCK;
	}

	public Lock(String ty,Variable v){
		var = v;
		lock = (ty.equals("lock"))?true:false;
		type = (lock)?TypeInst.LOCK:TypeInst.UNLOCK;
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