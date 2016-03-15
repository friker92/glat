package glat.program;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import glat.program.instructions.InstType;

public abstract class Instruction {
/*
 method - transition - location
 Type
*/
	public Instruction(){
		gs = new Gson();
		String js = "{}";
		JsonElement e = gs.fromJson(js, JsonElement.class);
		data = e.getAsJsonObject();
	}
	public Instruction(String d){
		gs = new Gson();
		JsonElement e = gs.fromJson(d, JsonElement.class);
		data = e.getAsJsonObject();
	}
	public JsonObject getData(){
		return data;
	};
	
	public  abstract String toString();

	public String getLabel(){
		return trans.getLabel()+"_inst:"+position;
	}
	
	public InstType getType(){
		return type;
	}
	
	public void setMethod(Method m) {
		method = m;		
	}
	
	public void setPosition(int p) {
		position = p;
	}
	
	public void setTransition(Transition t){
		trans = t;
	}
	
	protected Method method;
	protected Transition trans;
	protected int position;
	protected JsonObject data;
	protected Gson gs;
	protected InstType type;

}