package glat.program.instructions;

import java.util.List;
import java.util.Vector;

import glat.program.GlatInstruction;
import glat.program.GlatMethod;
import glat.program.instructions.expressions.terminals.Variable;

public class Call extends GlatInstruction {
	public Call(){
		super();
		type = TypeInst.ASYNCCALL;
		args = new Vector<Variable>();
	}
	public Call(String name, String ty){
		this.name = name;
		sync = (ty.equals("sync")||ty.equals("call"))?true:false;
		type = (sync)?TypeInst.SYNCCALL:TypeInst.ASYNCCALL;
		args = new Vector<Variable>();
		ret = null;
	}
	@Override
	public String toString() {
		if(ret != null)
			return type.name()+": "+ret.toString()+" := "+name+" "+args.toString();
		else
			return type.name()+": "+name+" "+args.toString();
	}
	public void addParameter(Variable v){
		args.add(v);
	}
	
	public List<Variable> getArgs(){
		return args;
	}
	
	public GlatMethod getMethodRef(){
		return meth;
	}
	
	public void setMethodRef(GlatMethod m){
		meth = m;
	}
	
	public String getName(){
		return name;
	}
	
	public void setReturn(Variable v){
		ret = v;
	}
	
	public Variable getReturn(){
		return ret;
	}

	private Vector<Variable> args;
	private boolean sync;
	private String name;
	private GlatMethod meth;
	private Variable ret;
}
