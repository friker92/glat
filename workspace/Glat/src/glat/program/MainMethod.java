package glat.program;

import java.util.List;
import java.util.Vector;

public class MainMethod {

	public MainMethod(){
		lth = new Vector<ThreadLaunch>();
	}
	
	
	public List<ThreadLaunch> getThreads(){
		return lth;
	}
	
	
	
	public void addThread(ThreadLaunch t){
		lth.add(t);
	}
	
	public void addDeclaration(Declaration dl) {
		// TODO Auto-generated method stub
		
	}

	public void addInitInstr(Instruction i) {
		// TODO Auto-generated method stub
		
	}
	
	private Vector<ThreadLaunch> lth;
}
