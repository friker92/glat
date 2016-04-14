package glat.program;
import java.util.Vector;

import glat.program.instructions.Call;
import glat.program.instructions.TypeInst;
import glat.program.instructions.expressions.terminals.Variable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Program {
	/*
	 Get the list of methods
	 List of global variables
	 Get the entry method
	 Get list of used primitive types
	*/
	public Program(){
		methods = new Vector<Method>();
		primitive = new HashSet<String>();
		declarations = new Vector<Declaration>();
		hashMethod = new HashMap<String,Method>();
		initInstr = new Vector<Instruction>();
		entry = "main";
	}
	public List<Method> getMethods(){
		return methods;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<String> getPrimitiveTypes(){
		return new Vector(Arrays.asList(primitive.toArray()));
	}
	
	public List<Declaration> getGlobalVariables(){
		return declarations;
	}
	public List<Variable> getGlobalVariablesAsVar() {
		Vector<Variable> p = new Vector<Variable>(declarations.size());
		for (Declaration d : declarations){
			p.add(new Variable(d));
		}
		return p;
	}
	
	public Method getEntryMethod(){
		if(hashMethod.containsKey(entry))
			return hashMethod.get(entry);
		throw new Error("Main is undefine.");

		
	}
	
	public Declaration getVariable(String v){
		Declaration d;
		Iterator<Declaration> it = declarations.iterator();
		while(it.hasNext()){
			d = it.next();
			if (d.getName().equals(v))
				return d;
		}
		return null;
	}

	public void addMethod(Method m){
		methods.add(m);
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
		hashMethod.put(m.getName(), m);
	}
	
	public void addDeclaration(Declaration v){
		addPrimitive(v.getType());
		declarations.add(v);
	}
	public void addInitInstr(Instruction i){
		initInstr.add(i);
	}
	
	private void addPrimitive(String s){
		if(!(s.equals("thread")||s.equals("lock")||s.equals("void")))
			primitive.add(s);
	}
	
	private Vector<Method> methods;
	HashMap<String,Method> hashMethod;
	private Set<String> primitive;
	private Vector<Declaration> declarations;
	private String entry;
	private Vector<Instruction> initInstr;
	
	public void checkCalls() {
		if(  !hashMethod.containsKey(entry) ){
			System.out.println(hashMethod);
			throw new Error("Missing Method, please define: "+entry);
		}
		for(Method m : methods){
			Iterator<Transition> it = m.getCFG().edgeSet().iterator();
			while(it.hasNext()){
				Transition tr = it.next();
				Vector<Instruction> v = tr.getCode();
				for (Instruction i : v){
					if (i.getType() == TypeInst.SYNCCALL || i.getType() == TypeInst.ASYNCCALL){
						if(  hashMethod.containsKey(((Call)i).getName()) ){
							Method tmp = hashMethod.get((((Call)i).getName()));
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

}
