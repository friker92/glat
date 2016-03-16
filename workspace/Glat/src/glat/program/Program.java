package glat.program;
import java.util.Vector;

import glat.program.instructions.Asignation;
import glat.program.instructions.Call;
import glat.program.instructions.TypeInst;

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
	
	public Method getEntryMethod(){
		return methods.get(0);
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
	
	private void addPrimitive(String s){
		if(!(s.equals("thread")||s.equals("lock")||s.equals("void")))
			primitive.add(s);
	}
	
	private Vector<Method> methods;
	HashMap<String,Method> hashMethod;
	private Set<String> primitive;
	private Vector<Declaration> declarations;
	public void checkCalls() {
		for(Method m : methods){
			Iterator<Transition> it = m.getCFG().edgeSet().iterator();
			while(it.hasNext()){
				Transition tr = it.next();
				Vector<Instruction> v = tr.getCode();
				for (Instruction i : v){
					if (i.getType() == TypeInst.SYNCCALL || i.getType() == TypeInst.ASYNCCALL){
						if(  hashMethod.containsKey(((Call)i).getName())    ){
							((Call)i).setMethod(hashMethod.get((((Call)i).getName())));
						}else
							throw new Error("Missing Method, please define: "+((Call)i).getName());
					}else if(i.getType() == TypeInst.ASIGNATION){
						Asignation as = (Asignation)i;
						if(as.getExpr().getType()==0){
							Call cal = (Call)as.getExpr().getInst();
							cal.setMethod(hashMethod.get(cal.getName()));
						}
					}
				}
			}
		}
		
	}
}
