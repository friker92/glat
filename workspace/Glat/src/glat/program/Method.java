package glat.program;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import glat.program.instructions.Call;
import glat.program.instructions.expressions.terminals.Variable;

public class Method {
/*
 Get name
 Get list of parameters
 Get return type
 Get CFG
*/
	public Method(String type, String name){
		this.type = type;
		this.name = name;
		this.vars = new Vector<Declaration>();
		this.args = new Vector<Declaration>();
		this.callpoints = new Vector<Call>();
		cfg = new DefaultDirectedWeightedGraph<String, GlatTransition>(
			    GlatTransition.class);
	}
	public String getLabel(){
		return type+"_"+name;
	}
	public void addParameter(Declaration v){
		args.add(v);
	}
	
	public void addDeclaration(Declaration v){
		vars.add(v);
	}
	
	public void addEntryPoint(String s){
		entry = s;
	}
	public void addCallPoint(Call i){
		callpoints.add(i);
	}
	
	public void addTransition(GlatTransition t){
		cfg.addVertex(t.getSource());
		cfg.addVertex(t.getDestination());
		cfg.addEdge(t.getSource(), t.getDestination(),t);
	}
	
	public String getName(){
		return name;
	}
	
	public String getReturnType(){
		return type;
	}
	public List<Declaration> getParameters(){
		return args;
	}
	public List<Variable> getParametersAsVar(){
		Vector<Variable> p = new Vector<Variable>(args.size());
		for (Declaration d : args){
			p.add(new Variable(d));
		}
		return p;
	}
	public List<Declaration> getVariables() {
		return vars;
	}
	public List<Call> getCallPoints(){
		return callpoints;
	}
	public String getEntryPoint(){
		return entry;
	}
	
	public List<Instruction> getFirstInsts(){
		return getFirstInstsFrom(entry);
	}
	public List<Instruction> getFirstInstsFrom(String node){
		List<Instruction> insts = new Vector<Instruction>();
		Iterator<GlatTransition> it = cfg.edgesOf(node).iterator();
		GlatTransition tr;
		while(it.hasNext()){
			tr = it.next();
			if(tr.getSource().equals(node) && tr.getNumInsts() > 0)
				insts.add(tr.getInst(0));
		}
		return insts;
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
	
	public WeightedGraph<String, GlatTransition> getCFG(){
		return cfg;
	}
	
	public String toString(){
		return name;
	}
	private Vector<Declaration> vars;
	private Vector<Declaration> args;
	private Vector<Call> callpoints;
	private String type;
	private String name;
	private String entry;
	private WeightedGraph<String, GlatTransition> cfg;
}
