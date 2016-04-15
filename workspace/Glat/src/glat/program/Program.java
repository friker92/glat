package glat.program;
import java.util.Vector;

import glat.program.instructions.Call;
import glat.program.instructions.TypeInst;
import glat.program.instructions.expressions.terminals.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Program does not need any thing to be builded. 
 * A program has properties like each element of this structure.
 * You can set this properties or ask for it.
 * You can add:
 * - Global Variables.
 * - Initialization instructions of global variables.
 * - Main "method", which is especial.
 * - Methods.
 * You can ask for:
 * - Global Variables.
 * - Initialization instructions of global variables.
 * - Main "method".
 * - Other Methods.
 * - Non-Primitive Types
 */
public class Program extends GlatClass{

	
	public Program(){
		primitive = new HashSet<String>();
		globDecl = new Vector<Declaration>();
		methods = new HashMap<String,Method>();
		initInstr = new Vector<Instruction>();
	}
	
	/*##############################
	 *        Access Methods       *
	 ##############################*/
	
	
	public List<Method> getMethods(){
		return new ArrayList<Method>(methods.values());
	}

	public List<String> getPrimitiveTypes(){
		return new ArrayList<String>(primitive);
	}
	
	public List<Declaration> getGlobalVariables(){
		return globDecl;
	}
	public List<Variable> getGlobalVariablesAsVar() {
		Vector<Variable> p = new Vector<Variable>(globDecl.size());
		for (Declaration d : globDecl){
			p.add(new Variable(d));
		}
		return p;
	}
	
	public MainMethod getEntryMethod(){
		return mainmethod;
		//throw new Error("Main is undefine.");
	}
	
	public Declaration getVariable(String v){
		Declaration d;
		Iterator<Declaration> it = globDecl.iterator();
		while(it.hasNext()){
			d = it.next();
			if (d.getName().equals(v))
				return d;
		}
		return null;
	}
	
	
	
	/*##############################
	 *        Build Methods        *
	 ##############################*/
	public void addMainMethod(MainMethod mm){
		if(mainmethod != null)
			throw new Error("Error: Main already exits");
		mainmethod = mm;
	}
	public void addMethod(Method m){
		if(methods.containsKey(m.getName()))
			throw new Error("Error: "+m.getName()+" method already exits");
		addPrimitive(m.getReturnType());
		Declaration v;
		Iterator<Declaration> it = m.getParameters().iterator();
		while (it.hasNext()){
			v = it.next();
			addPrimitive(v.getType());
		}
		it = m.getVariables().iterator();
		while (it.hasNext()){
			v = it.next();
			addPrimitive(v.getType());
		}
		methods.put(m.getName(), m);
	}
	
	public void addDeclaration(Declaration v){
		addPrimitive(v.getType());
		globDecl.add(v);
	}
	public void addInitInstr(Instruction i){
		initInstr.add(i);
	}
	
	private void addPrimitive(String s){
		if(!(s.equals("thread")||s.equals("lock")||s.equals("void")))
			primitive.add(s);
	}
	
	public void checkProgram(){
		checkCalls();
	}
	public void checkCalls() {
		for(Method m : methods.values()){
			Iterator<GlatTransition> it = m.getCFG().edgeSet().iterator();
			while(it.hasNext()){
				GlatTransition tr = it.next();
				Vector<Instruction> v = tr.getCode();
				for (Instruction i : v){
					if (i.getType() == TypeInst.SYNCCALL || i.getType() == TypeInst.ASYNCCALL){
						if(  methods.containsKey(((Call)i).getName()) ){
							Method tmp = methods.get((((Call)i).getName()));
							tmp.addCallPoint((Call)i);
							((Call)i).setMethodRef(tmp);
						}else{
							throw new Error("Missing Method, please define: "+((Call)i).getName());
						}
					}
				}
			}
		}
	}
	
	


	
	/*##############################
	 *     Internal Attributes     *
	 ##############################*/
	
	private MainMethod mainmethod;
	private HashMap<String,Method> methods;
	private Set<String> primitive;
	private Vector<Declaration> globDecl;
	private Vector<Instruction> initInstr;
	


}
