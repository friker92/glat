package glat.program.instructions;

import glat.program.GlatMethod;
import glat.program.instructions.expressions.terminals.*;
import glat.program.instructions.expressions.terminals.values.IntNumber;

public class ThreadLaunch {

	public ThreadLaunch(String id, IntNumber n, Call cl){
		name = id;
			inf = false;
			num = n;
		c = cl;
	}
	
	public ThreadLaunch(String id, Call cl){
		name = id;
			inf = true;
			num = null;
		c = cl;
	}
	
	public String getLabel(){
		return "thread "+name+" "+((inf)?"*":num)+" "+c.getLabel();
	}
	
	public GlatMethod getMethod(){
		return c.getMethodRef();
	}
	
	public Call getCall(){
		return c;
	}
	
	private String name;
	private boolean inf;
	private IntNumber num;
	private Call c;
	
}
