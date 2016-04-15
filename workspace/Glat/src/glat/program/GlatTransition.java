package glat.program;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.jgrapht.graph.*;

public class GlatTransition extends DefaultWeightedEdge implements Transition {

	public GlatTransition(GlatMethod m,int p,String s, String d){
		source = s;
		dest = d;
		label = ""+m.getLabel()+p+":"+s+"->"+d+"";
		method = m;
		position = p;
		Tcode = new Vector<GlatInstruction>();
	}
	
	 public int hashCode()
	 {
	     return label.hashCode();
	 }
	 
	 public boolean equals(Object obj)
	 {
	     if (this == obj) {
	         return true;
	     }
	     if (obj == null) {
	         return false;
	     }
	     if (!(obj instanceof GlatTransition)) {
	         return false;
	     }

	     GlatTransition edge = (GlatTransition) obj;
	     return label.equals(edge.label);
	 }
	
	public void addInstruction(GlatInstruction i){
		i.setMethod(method);
		i.setTransition(this);
		if(Tcode.size()>0)
			Tcode.get(Tcode.size()-1).setNextInst(i);
		Tcode.add(i);
	}
	
	public Vector<GlatInstruction> getCode(){
		return Tcode;
	}
	
	public String getSource(){
		return source;
	}

	public String getDestination(){
		return dest;
	}
	public String getLabel(){
		return label;
	}
	
	public int getPosition(){
		return position;
	}
	
	public String toString(){
		String s = ""+source+" -> "+dest+" : { \n";
		for (GlatInstruction i : Tcode){
			s+="\t\t\t\t"+i.toString()+"\n";
		}
		s+= "\t\t\t}";
		return s;
	}
	
	public GlatInstruction getInst(int pos) {
		return Tcode.get(pos);
	}
	
	public int getNumInsts(){
		return Tcode.size();
	}
	
	
	
	@Override
	public Node getSrcNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getTargetNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Instruction> getInstructions() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String source;
	private String dest;
	private Vector<GlatInstruction> Tcode;
	private String label;
	private GlatMethod method;
	private int position;
	private static final long serialVersionUID = -1L;
	
	private Properties prop;
	
	
	/*###################################
	 *        PropTable Methods
	 ###################################*/
	
	/**
	 * 
	 */
	@Override
	public Object getPropValue(Object name) {
		return prop.get(name);
	}

	/**
	 * 
	 */
	@Override
	public Object setPropValue(Object name, Object value) {
		return prop.put(name, value);
	}

	/**
	 * 
	 */
	@Override
	public List<Object> getPropNames() {
		return new ArrayList<Object>(prop.keySet());
	}



}