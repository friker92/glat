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
		this.vars = new Vector<Variable>();
		this.args = new Vector<Variable>();
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
	public List<Variable> getParameters(){
		return args;
	}
	@Override
	public List<Variable> getVariables() {
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
	
	public Variable getVariable(String v){
		Variable d;
		Iterator<Variable> it = args.iterator();
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
	
	public void addParameter(Variable v){
		args.add(v);
	}
	
	public void addVar(Variable v){
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
	
	private Vector<Variable> vars;
	private Vector<Variable> args;
	private Vector<Call> callpoints;
	private String type;
	private String name;
	private GlatCFG cfg;

}
