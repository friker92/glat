package glat.fixpoint;

import glat.program.GlatInstruction;
//import glat.program.GlatMethod;

public class Event {
	public Event(TypeEvent ty, GlatInstruction i, AbstractState s) {
		//if (ty != TypeEvent.EXEC) throw new Error ("With GlatInstruction, Events have to be of type EXEC");
		type = ty;
		inst = i;
		st = s;
	}

	/*public Event(TypeEvent ty, GlatMethod m, AbstractState s) {
		if (ty != TypeEvent.CALL) throw new Error ("With GlatMethod, Events have to be of type CALL");
		type = ty;
		meth = m;
		st = s;
	}*/

	/*public GlatMethod getMethod() {
		if (meth==null) throw new Error ("With GlatMethod, Events have to be of type CALL");
		return meth;
	}*/

	public GlatInstruction getInst() {
		if (inst==null) throw new Error ("With GlatInstruction, Events have to be of type EXEC");
		return inst;
	}
	
	public AbstractState getState(){return st;}

	public TypeEvent getType() {return type;}
	
	public String toString(){
		return "Event: "+type+" - "+inst+" <<>> "+st;
	}

	private TypeEvent type;
	private GlatInstruction inst;
	//private GlatMethod meth;
	private AbstractState st;
}
