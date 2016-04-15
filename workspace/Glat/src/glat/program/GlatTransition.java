package glat.program;

import java.util.List;
import java.util.Vector;

import org.jgrapht.graph.*;

public class GlatTransition extends DefaultWeightedEdge implements Transition {
/*
 get code
 get origin
 get dest
*/
	public GlatTransition(Method m,int p,String s, String d){
		source = s;
		dest = d;
		label = ""+m.getLabel()+p+":"+s+"->"+d+"";
		method = m;
		position = p;
		Tcode = new Vector<Instruction>();
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
	
	public void addInstruction(Instruction i){
		i.setMethod(method);
		i.setTransition(this);
		if(Tcode.size()>0)
			Tcode.get(Tcode.size()-1).setNextInst(i);
		Tcode.add(i);
	}
	
	public Vector<Instruction> getCode(){
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
		for (Instruction i : Tcode){
			s+="\t\t\t\t"+i.toString()+"\n";
		}
		s+= "\t\t\t}";
		return s;
	}
	
	public Instruction getInst(int pos) {
		return Tcode.get(pos);
	}
	
	public int getNumInsts(){
		return Tcode.size();
	}
	
	private String source;
	private String dest;
	private Vector<Instruction> Tcode;
	private String label;
	private Method method;
	private int position;
	private static final long serialVersionUID = -1L;
	
	
	
	@Override
	public Object getPropValue(Object name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object setPropValue(Object name, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> getPropNames() {
		// TODO Auto-generated method stub
		return null;
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

}