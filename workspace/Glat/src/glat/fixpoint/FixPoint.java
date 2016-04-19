package glat.fixpoint;

import java.util.*;

import glat.program.GlatInstruction;
import glat.program.GlatMethod;
import glat.program.GlatProgram;
import glat.program.instructions.Call;
import glat.program.instructions.Return;
import glat.program.instructions.expressions.Terminal;
import glat.program.instructions.expressions.terminals.TypeTerm;
import glat.program.instructions.expressions.terminals.Variable;

public class FixPoint {

    private AbstractDomain domain;
    private Queue<Event> Q;
    private HashMap<String, AbstractState> table;
    private GlatProgram program;
    private boolean done;
    
    public FixPoint(AbstractDomain d, GlatProgram pr) {
    	domain = d;
    	program = pr;
    	done = false;
    }
    public String print(){
    	String str = "";
    	if(done){
    		Set<String> k = table.keySet();
    		for(String s : k){
    			str += s+ " \n";
    			str += table.get(s);
    			str +="\n";
    		}	
    	}else{
    		str += "Please run first: fixPoint()";
    	}
    	return str;
    }
    public void fixPoint(){
		//T = new Table();
    	table = new HashMap<String,AbstractState>();
    	Q = new LinkedList<Event>();
    	GlatMethod m = (GlatMethod) program.getMethods().get(0);//program.getEntryMethod();
    	Call c = new Call(m.getName(),"sync");
    	c.setMethodRef(m);
		//Q = { call( main, {} ) }
		Q.add( new Event( TypeEvent.CALL, c, domain.initVars(program.getGlobalVariables()) ) );
		while ( !Q.isEmpty() ) {

	    	System.out.println(Q.toString());
		    Event x = Q.poll();
		    execute(x);
		}
		done = true;
    }
	
    public void execute(Event x){
		AbstractState s0,s1,s;
		GlatInstruction i;
		GlatMethod m;
		List<GlatInstruction> li;
		List<Variable> lv,lv2;
		switch (x.getType()) {
	
		case CALL://call(c, s) : // state is over the param of m, m is MethodRef of call c
		    i = x.getInst();
		    s = x.getState();
		    m = ((Call)i).getMethodRef();
		    s0 = domain.extend( s, domain.initVars( m.getVariables() ) ); // add local vars
		    li = m.getFirstInsts();
		    li.forEach((inst)->Q.add( new Event(TypeEvent.EXEC,inst, s0) ));
		    break;
		case EXEC://exec(i,s) : 
		    i = x.getInst();
		    s = x.getState();
		    switch(i.getType()){
		    case RETURN: // exec(i, s) // i is a return
				//suppose i is a return from m
				m = i.getMethod();
				lv = m.getParameters();
				Terminal t = ((Return)i).getVar();
				if (t != null && t.isVar())
					lv.add((Variable)t);
				s0 = domain.project(s, lv);//m.retvar()+m.formalParm());
				
				
				
				
				for(Call cl : m.getCallPoints()){

					Iterator<GlatInstruction> ii= cl.getNextInsts().iterator();
				    while(ii.hasNext())
				    	decideHandleRet(ii.next(),cl,(Return)i,s0);//Q.add( new Event(TypeEvent.EXEC,inst, s2) ));
						
					//the follows a call to m do:
				    }
				break;
		    case ASYNCCALL:
		    case SYNCCALL: //exec(i, s) // i is a call
				i = x.getInst();
				s = x.getState();
				// suppose i is a call to m
				m = ((Call)i).getMethodRef();
				lv = m.getParameters();
				lv2 = ((Call)i).getArgs();
				s0 = domain.project( s, lv2);//m.actualParams() );
				s1 = domain.rename( s0, lv2, lv );// m.actualParams, m.formalParams);
				decideStoreCall((Call)i,s1);
				break;
		    default:
				i = x.getInst();
				s = x.getState();
				// i is not a call and not exit
				s0 = domain.abstractExec(i, s);
				storeInTable(i,s0);
				break;
		    }
		}
    }

    public void storeInTable(GlatInstruction i, AbstractState s) {
		AbstractState s0,s1,smid;
		List<GlatInstruction> li;
		s0 = table.get(i.getLabel());
		smid = (s0!=null)?s0:domain.empty();
		s1 = domain.lub(s,smid);
		if ( !domain.le(s1,smid) ) {
		    table.put(i.getLabel(),s1);
		    li = i.getNextInsts();
		    li.forEach((inst)->Q.add( new Event(TypeEvent.EXEC,inst, s1) ));
		}
    }

    public void decideHandleRet(GlatInstruction inst,Call cl, Return r, AbstractState s) {
		AbstractState s0, s1,s2,smid;
		Vector<Variable> lv,lv2;
		Variable retPoint;
		GlatMethod m;
		m = cl.getMethodRef();
		lv = (Vector)m.getParameters();
		lv2 = (Vector)cl.getArgs();
		retPoint = cl.getReturn();
		Terminal t = r.getVar();
		
		if (t != null && t.isVar() && retPoint != null){
			lv.add((Variable)t);
			lv2.add(retPoint);
		}
		s0 = domain.rename(s, lv,lv2);//m.formalParam()+m.retvar(), m.actualParam()+m.retPoint());
		s1 = table.get(m.getLabel());
		smid = (s1!=null)?s1:domain.empty();
		s2 = domain.extend(smid, s0);
		List<GlatInstruction> li = cl.getNextInsts();
		if ( !domain.le(s2,smid) ) {
		    table.put( m.getLabel(), s2 );
			Q.add( new Event(TypeEvent.EXEC,inst, s2) );
		    //Q.add ( new Event( TypeEvent.EXEC, cl, s2 ) );
		}
    }

    public void decideStoreCall(Call c, AbstractState s) {
		AbstractState s0, s1,smid;
		GlatMethod m = c.getMethodRef();
		s0 = table.get(m.getLabel());
		smid = (s0!=null)?s0:domain.empty();
		s1 = domain.lub(s,smid);
		
		if ( !domain.le(s1,smid) ) {
		    table.put(m.getLabel(),s1);
		    Q.add ( new Event( TypeEvent.CALL, c, s1 ) );
		    
		}
	
		
    }
}
