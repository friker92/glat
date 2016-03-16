package glat.program.instructions;

import java.util.List;
import java.util.Vector;

import glat.program.Instruction;
import glat.program.Method;
import glat.program.instructions.expressions.terminals.Variable;

public class Call extends Instruction {
	public Call(){
		super();
		type = TypeInst.ASYNCCALL;
		args = new Vector<Variable>();
	}
	public Call(String name, String ty){
		this.name = name;
		sync = (ty.equals("sync"))?true:false;
		type = (sync)?TypeInst.SYNCCALL:TypeInst.ASYNCCALL;
		args = new Vector<Variable>();
	}
	@Override
	public String toString() {
		return type.name()+": "+name+" "+args.toString();
	}
	public void addParameter(Variable v){
		args.add(v);
	}
	
	public List<Variable> getArgs(){
		return args;
	}
	
	public Method getMethod(){
		return meth;
	}
	
	public void setMethod(Method m){
		meth = m;
	}
	
	public String getName(){
		return name;
	}

	private Vector<Variable> args;
	private boolean sync;
	private String name;
	private Method meth;
}
