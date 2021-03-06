package glat.program;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import glat.program.instructions.Call;
import glat.program.instructions.ThreadLaunch;
import glat.program.instructions.TypeInst;
import glat.program.instructions.expressions.TypeValue;
import glat.program.instructions.expressions.terminals.Variable;


/**
 * GlatProgram does not need any thing to be builded. 
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
public class GlatProgram extends GlatClass implements Program{

	public GlatProgram(){
		primitive = new HashSet<TypeValue>();
		for(TypeValue t : TypeValue.values())
			primitive.add(t);
		globDecl = new Vector<Variable>();
		methods = new HashMap<String,GlatMethod>();
		initInstr = new Vector<GlatInstruction>();
	}
	
	/*##############################
	 *        Access Methods       *
	 ##############################*/
	
	public List<Method> getMethods(){
		return new ArrayList<Method>(methods.values());
	}

	public List<TypeValue> getPrimitiveTypes(){
		return new ArrayList<TypeValue>(primitive);
	}
	
	public List<Variable> getGlobalVariables(){
		return globDecl;
	}
	
	public MainMethod getEntryMethod(){
		return mainmethod;
	}
	
	public boolean isGlobalVariable(String v){
		return null != getGlobalVariable(v);
	}
	
	public boolean isGlobalVariable(Variable v){
		return globDecl.contains(v);
	}
	
	@Override
	public Variable getGlobalVariable(String v) {
		Variable d;
		Iterator<Variable> it = globDecl.iterator();
		while(it.hasNext()){
			d = it.next();
			if (d.getName().equals(v))
				return d;
		}
		return null;
	}
	
	@Override
	public String getLabel() {
		return "Glat_Program";
	}
	
	/*##############################
	 *        Build Methods        *
	 ##############################*/
	
	public void addMainMethod(MainMethod mm){
		if(mainmethod != null)
			throw new Error("Error: Main already exits");
		mainmethod = mm;
	}
	public void addMethod(GlatMethod m){
		if(methods.containsKey(m.getName()))
			throw new Error("Error: "+m.getName()+" method already exits");
		methods.put(m.getName(), m);
	}
	
	public void addVar(Variable v){
		globDecl.add(v);
	}
	public void addInitInstr(GlatInstruction i){
		initInstr.add(i);
	}
	
	
	public void checkProgram(){
		//checkThreads();
		checkCalls();
	}
	
	public void checkThreads(){
		List<ThreadLaunch> Ts = mainmethod.getThreads();
		Ts.forEach((t)->{
			Call c = t.getCall();
			if(  methods.containsKey(c.getName()) ){
				GlatMethod tmp = methods.get((c.getName()));
				tmp.addCallPoint(c);
				c.setMethodRef(tmp);
			}else{
				throw new Error("Missing Method, please define: "+c.getName());
			}
		});
	}
	
	public void checkCalls() {
		for(GlatMethod m : methods.values()){
			m.getControlFlowGraph().getTransitions().forEach((tr)->{
				((GlatTransition) tr).getInstructions().forEach((i)-> {
					if (i.getType() == TypeInst.SYNCCALL || i.getType() == TypeInst.ASYNCCALL){
						if(  methods.containsKey(((Call)i).getName()) ){
							GlatMethod tmp = methods.get((((Call)i).getName()));
							tmp.addCallPoint((Call)i);
							((Call)i).setMethodRef(tmp);
						}else{
							throw new Error("Missing Method, please define: "+((Call)i).getName());
						}
					}
				});
				
			});
		}
	}
	
	/*##############################
	 *     Internal Attributes     *
	 ##############################*/
	
	private MainMethod mainmethod;
	private HashMap<String,GlatMethod> methods;
	private Set<TypeValue> primitive;
	private Vector<Variable> globDecl;
	private Vector<GlatInstruction> initInstr;


}
