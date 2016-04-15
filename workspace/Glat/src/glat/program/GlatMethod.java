package glat.program;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import glat.program.instructions.Call;
import glat.program.instructions.expressions.terminals.Variable;

public class GlatMethod extends GlatClass implements Method{
/*
 Get name
 Get list of parameters
 Get return type
 Get CFG
*/
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
		return getFirstInstsFrom(entry);
	}
	public List<GlatInstruction> getFirstInstsFrom(String node){
		List<GlatInstruction> insts = new Vector<GlatInstruction>();
		/*Iterator<GlatTransition> it = cfg.edgesOf(node).iterator();
		GlatTransition tr;
		while(it.hasNext()){
			tr = it.next();
			if(tr.getSource().equals(node) && tr.getNumInsts() > 0)
				insts.add(tr.getInst(0));
		}*/
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
	
	public void addEntryPoint(String s){
		cfg = new GlatCFG(new GlatNode(entry));
		entry = s;
	}
	public void addCallPoint(Call i){
		callpoints.add(i);
	}
	
	public void addTransition(GlatTransition t){
		//TODO: when CFG is done
	//	cfg.addVertex(t.getSource());
	//	cfg.addVertex(t.getDestination());
	//	cfg.addEdge(t.getSource(), t.getDestination(),t);
	}
	
	/*##############################
	 *     Internal Attributes     *
	 ##############################*/
	
	private Vector<Declaration> vars;
	private Vector<Declaration> args;
	private Vector<Call> callpoints;
	private String type;
	private String name;
	private String entry;
	private GlatCFG cfg;

}
