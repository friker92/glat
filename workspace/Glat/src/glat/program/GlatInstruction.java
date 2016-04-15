package glat.program;
import java.util.List;
import java.util.Vector;

/*import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
*/
import glat.program.instructions.TypeInst;

public abstract class GlatInstruction {
/*
 method - transition - location
 Type
*/
	public GlatInstruction(){
	}

	
	public  abstract String toString();

	public String getLabel(){
		return "inst:("+position_l+","+position_c+") "+type;//+"_"+trans.getLabel();
	}
	
	public TypeInst getType(){
		return type;
	}
	
	public void setMethod(GlatMethod m) {
		method = m;		
	}
	public GlatMethod getMethod() {
		return method;		
	}
	
	public void setPosition(int l,int c) {
		position_l = l;
		position_c = c;
	}
	
	public void setTransition(GlatTransition t){
		trans = t;
	}
	
	public List<GlatInstruction> getNextInsts(){
		if (next != null){
			List<GlatInstruction> insts = new Vector<GlatInstruction>();
			insts.add(next);
			return insts;
		}else{
			return method.getFirstInstsFrom(trans.getDestination());
		}
	}
	public void setNextInst(GlatInstruction n){
		next = n;
	}
	
	protected GlatMethod method;
	protected GlatTransition trans;
	protected int position_l;
	protected int position_c;
	protected TypeInst type;
	protected GlatInstruction next;
	public void setUbication(GlatMethod m, GlatTransition tr) {
		setMethod(m);
		setTransition(tr);
	}


}