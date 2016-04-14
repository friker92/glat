package glat.program;

import java.util.List;
import java.util.Vector;

public class MainMethod {

	public MainMethod(){
		lth = new Vector<ThreadLaunch>();
	}
	
	public void addThread(ThreadLaunch t){
		lth.add(t);
	}
	
	public List<ThreadLaunch> getThreads(){
		return lth;
	}
	
	
	private Vector<ThreadLaunch> lth;
}
