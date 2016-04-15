package glat.program.instructions;

import glat.program.instructions.expressions.terminals.*;
import glat.program.instructions.expressions.terminals.Number;

public class ThreadLaunch {

	public ThreadLaunch(String id, Number n, Call cl){
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
	
	private String name;
	private boolean inf;
	private Number num;
	private Call c;
	
}
