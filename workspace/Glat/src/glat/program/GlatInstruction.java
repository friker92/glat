package glat.program;
import java.util.List;
import java.util.Vector;

/*import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
*/
import glat.program.instructions.TypeInst;

public abstract class GlatInstruction extends GlatClass implements Instruction{
/*
 method - transition - location
 Type
*/
	public GlatInstruction(){
	}
	
	/*##############################
	 *        Access Methods       *
	 ##############################*/
	
	public  abstract String toString();

	public String getLabel(){
		return "inst:("+position_l+","+position_c+") "+type;//+"_"+trans.getLabel();
	}
	
	public TypeInst getType(){
		return type;
	}
	
	public GlatMethod getMethod() {
		return method;		
	}
	
	public List<GlatInstruction> getNextInsts(){
		if (next != null){
			List<GlatInstruction> insts = new Vector<GlatInstruction>();
			insts.add(next);
			return insts;
		}else{
			return method.getFirstInstsFrom(trans.getTargetNode());
		}
	}
	
	/*##############################
	 *        Build Methods        *
	 ##############################*/
	
	public void setUbication(GlatMethod m, GlatTransition tr) {
		setMethod(m);
		setTransition(tr);
	}
	
	public void setMethod(GlatMethod m) {
		method = m;		
	}
	
	public void setPosition(int l,int c) {
		position_l = l;
		position_c = c;
	}
	
	public void setTransition(GlatTransition t){
		trans = t;
	}
	
	public void setNextInst(GlatInstruction n){
		next = n;
	}
	
	/*##############################
	 *     Internal Attributes     *
	 ##############################*/
	
	protected GlatMethod method;
	protected GlatTransition trans;
	protected int position_l;
	protected int position_c;
	protected TypeInst type;
	protected GlatInstruction next;

}