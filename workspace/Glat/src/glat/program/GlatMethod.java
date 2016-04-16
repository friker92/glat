package glat.program;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import glat.program.instructions.Call;
import glat.program.instructions.expressions.terminals.Variable;

public class GlatMethod extends GlatClass implements Method{

	public GlatMethod(String type, String name){
		this.type = type;
		this.name = name;
		this.vars = new Vector<Declaration>();
		this.args = new Vector<Declaration>();
		this.callpoints = new Vector<Call>();
		
	}
	
	/*##############################
	 *        Access Methods       *
	 ##############################*/
	
	@Override
	public Node getInitNode() {
		return cfg.getInitNode();
	}
	@Override
	public ControlFlowGraph getControlFlowGraph() {
		return cfg;
	}
	@Override
	public String getLabel(){
		return type+"_"+name;
	}
	@Override
	public String getName(){
		return name;
	}
	@Override
	public String getReturnType(){
		return type;
	}
	@Override
	public List<Declaration> getParameters(){
		return args;
	}
	@Override
	public List<Declaration> getVariables() {
		return vars;
	}
	@Override
	public List<Call> getCallPoints(){
		return callpoints;
	}
	@Override
	public String toString(){
		return name;
	}
	
	public Declaration getVariable(String v){
		Declaration d;
		Iterator<Declaration> it = args.iterator();
		while(it.hasNext()){
			d = it.next();
			if (d.getName().equals(v))
				return d;
		}
		it = vars.iterator();
		while(it.hasNext()){
			d = it.next();
			if (d.getName().equals(v))
				return d;
		}
		return null;
	}
	
	public List<Variable> getParametersAsVar(){
		Vector<Variable> p = new Vector<Variable>(args.size());
		for (Declaration d : args){
			p.add(new Variable(d));
		}
		return p;
	}
	
	
	public List<GlatInstruction> getFirstInsts(){
		return getFirstInstsFrom(cfg.getInitNode());
	}
	public List<GlatInstruction> getFirstInstsFrom(Node node){
		List<GlatInstruction> insts = new Vector<GlatInstruction>();
		cfg.getOutTransitions(node).forEach((tr)-> {
			if(tr.getNumInstructions()>0)
				insts.add((GlatInstruction) tr.getInstruction(0));
			
		});
		return insts;
	}
	
	/*##############################
	 *        Build Methods        *
	 ##############################*/
	
	public void addParameter(Declaration v){
		args.add(v);
	}
	
	public void addDeclaration(Declaration v){
		vars.add(v);
	}
	
	public void addCFG(GlatCFG c){
		cfg = c;
	}
	public void addCallPoint(Call i){
		callpoints.add(i);
	}
	
	/*##############################
	 *     Internal Attributes     *
	 ##############################*/
	
	private Vector<Declaration> vars;
	private Vector<Declaration> args;
	private Vector<Call> callpoints;
	private String type;
	private String name;
	private GlatCFG cfg;

}
