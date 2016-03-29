package glat.program;
import java.util.List;
import java.util.Vector;

/*import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
*/
import glat.program.instructions.TypeInst;

public abstract class Instruction {
/*
 method - transition - location
 Type
*/
	public Instruction(){
	}

	
	public  abstract String toString();

	public String getLabel(){
		return trans.getLabel()+"_inst:"+position;
	}
	
	public TypeInst getType(){
		return type;
	}
	
	public void setMethod(Method m) {
		method = m;		
	}
	public Method getMethod() {
		return method;		
	}
	
	public void setPosition(int p) {
		position = p;
	}
	
	public void setTransition(Transition t){
		trans = t;
	}
	
	public List<Instruction> getNextInsts(){
		if (next != null){
			List<Instruction> insts = new Vector<Instruction>();
			insts.add(next);
			return insts;
		}else{
			return method.getFirstInstsFrom(trans.getDestination());
		}
	}
	public void setNextInst(Instruction n){
		next = n;
	}
	
	protected Method method;
	protected Transition trans;
	protected int position;
	protected TypeInst type;
	protected Instruction next;

}