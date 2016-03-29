package glat.fixpoint;

import glat.program.Instruction;
//import glat.program.Method;

public class Event {
	public Event(TypeEvent ty, Instruction i, AbstractState s) {
		//if (ty != TypeEvent.EXEC) throw new Error ("With Instruction, Events have to be of type EXEC");
		type = ty;
		inst = i;
		st = s;
	}

	/*public Event(TypeEvent ty, Method m, AbstractState s) {
		if (ty != TypeEvent.CALL) throw new Error ("With Method, Events have to be of type CALL");
		type = ty;
		meth = m;
		st = s;
	}*/

	/*public Method getMethod() {
		if (meth==null) throw new Error ("With Method, Events have to be of type CALL");
		return meth;
	}*/

	public Instruction getInst() {
		if (inst==null) throw new Error ("With Instruction, Events have to be of type EXEC");
		return inst;
	}
	
	public AbstractState getState(){return st;}

	public TypeEvent getType() {return type;}

	private TypeEvent type;
	private Instruction inst;
	//private Method meth;
	private AbstractState st;
}
