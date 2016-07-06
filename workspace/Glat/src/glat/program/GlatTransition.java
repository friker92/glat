package glat.program;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

public class GlatTransition extends GlatClass implements Transition {
// TODO: verify that I dont need to extend DirectEdge
	public GlatTransition(GlatMethod m, int p, GlatNode s, GlatNode d){
		source = s;
		dest = d;
		label = ""+m.getLabel()+p+":"+s+"->"+d+"";
		method = m;
		position = p;
		Tcode = new Vector<GlatInstruction>();
	}
	
	/*##############################
	 *        Access Methods       *
	 ##############################*/

	@Override
	public String getLabel(){
		return label;
	}
	
	@Override
	public String toString(){
		String s = ""+source+" -> "+dest;/*+" : { \n";
		for (GlatInstruction i : Tcode){
			s+="\t\t\t\t"+i.toString()+"\n";
		}
		s+= "\t\t\t}";*/
		return s;
	}
	
	@Override
	public Node getSrcNode() {
		return source;
	}

	@Override
	public Node getTargetNode() {
		return dest;
	}

	@Override
	public List<Instruction> getInstructions() {
		List<Instruction> ins = new ArrayList<Instruction>();
		Tcode.forEach((gi)->ins.add((Instruction)gi));
		return ins;
	}
	
	public int getNumInstructions(){
		return Tcode.size();
	}
	
	public Instruction getInstruction(int pos) {
		return Tcode.get(pos);
	}

	public int getPosition(){
		return position;
	}
	
	/*##############################
	 *        Build Methods        *
	 ##############################*/
	
	public void addInstruction(GlatInstruction i){
		i.setMethod(method);
		i.setTransition(this);
		Boolean previous = (Boolean) getPropValue("isWriteGlobal");
		if(null != i.getPropValue("isWriteGlobal"))
			setPropValue("isWriteGlobal", (previous==null)?i.getPropValue("isWriteGlobal"):(previous|| (boolean)i.getPropValue("isWriteGlobal")));
		else
			setPropValue("isWriteGlobal", (previous==null)?false:previous);
		
		
		previous = (Boolean) getPropValue("isReadGlobal");
		if(null != i.getPropValue("isReadGlobal"))
			setPropValue("isReadGlobal", (previous==null)?i.getPropValue("isReadGlobal"):(previous|| (boolean)i.getPropValue("isReadGlobal")));
		else
			setPropValue("isReadGlobal", (previous==null)?false:previous);
		if(Tcode.size()>0)
			Tcode.get(Tcode.size()-1).setNextInst(i);
		Tcode.add(i);
	}

	/*##############################
	 *     Internal Attributes     *
	 ##############################*/
	
	/*public boolean equals(Object obj){
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
	}*/	

	private GlatNode source;
	private GlatNode dest;
	private Vector<GlatInstruction> Tcode;
	private String label;
	private GlatMethod method;
	private int position;
	
}