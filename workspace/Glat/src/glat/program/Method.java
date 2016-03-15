package glat.program;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

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
		cfg = new DefaultDirectedWeightedGraph<String, Transition>(
			    Transition.class);
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
	
	public void addTransition(Transition t){
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
	public List<Declaration> getVariables() {
		return vars;
	}
	public String getEntryPoint(){
		return entry;
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
	
	public WeightedGraph<String, Transition> getCFG(){
		return cfg;
	}
	
	public String toString(){
		return name;
	}
	private Vector<Declaration> vars;
	private Vector<Declaration> args;
	private String type;
	private String name;
	private String entry;
	private WeightedGraph<String, Transition> cfg;
}
