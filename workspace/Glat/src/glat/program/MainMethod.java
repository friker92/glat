package glat.program;

import java.util.List;
import java.util.Vector;

import glat.program.instructions.Call;
import glat.program.instructions.ThreadLaunch;

public class MainMethod extends GlatClass{

	public MainMethod(){
		lth = new Vector<ThreadLaunch>();
	}
	
	/*##############################
	 *        Access Methods       *
	 ##############################*/
	
	public List<ThreadLaunch> getThreads(){
		return lth;
	}
	
	public List<Declaration> getVariables(){
		return vars;
	}
	
	public List<GlatInstruction> getInitInstr(){
		return instr;
	}
	
	@Override
	public String getLabel() {
		return "void_main";
	}
	
	/*##############################
	 *        Build Methods        *
	 ##############################*/
	
	public void addThread(ThreadLaunch t){
		lth.add(t);
	}
	
	public void addDeclaration(Declaration dl) {
		vars.add(dl);
		
	}

	public void addInitInstr(GlatInstruction i) {
		instr.add(i);
	}
	
	/*##############################
	 *     Internal Attributes     *
	 ##############################*/
	
	private Vector<ThreadLaunch> lth;
	private Vector<Declaration> vars;
	private Vector<GlatInstruction> instr;
}
