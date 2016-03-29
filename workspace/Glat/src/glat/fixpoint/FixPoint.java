package glat.fixpoint;

import java.util.*;

import glat.program.Instruction;
import glat.program.Method;
import glat.program.Program;
import glat.program.instructions.Call;
import glat.program.instructions.Return;
import glat.program.instructions.expressions.Terminal;
import glat.program.instructions.expressions.terminals.TypeTerm;
import glat.program.instructions.expressions.terminals.Variable;

public class FixPoint {

    private AbstractDomain domain;
    private Queue<Event> Q;
    private HashMap<String, AbstractState> table;
    private Program program;
    
    public FixPoint(AbstractDomain d, Program pr) {
    	domain = d;
    	program = pr;
    }

    public void fixPoint(){
		//T = new Table();
    	table = new HashMap<String,AbstractState>();
    	Method m = program.getEntryMethod();
    	Call c = new Call(m.getName(),"sync");
    	c.setMethodRef(m);
		//Q = { call( main, {} ) }
		Q.add( new Event( TypeEvent.CALL, c, domain.empty() ) );
		while ( !Q.isEmpty() ) {
		    Event x = Q.poll();
		    execute(x);
		}
    }
	
    public void execute(Event x){
		AbstractState s0,s1,s;
		Instruction i;
		Method m;
		List<Instruction> li;
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
				lv = m.getParametersAsVar();
				Terminal t = ((Return)i).getVar();
				if (t != null && t.getType() == TypeTerm.VARIABLE)
					lv.add((Variable)t);
				s0 = domain.project(s, lv);//m.retvar()+m.formalParm());
				for(Call cl : m.getCallPoints()){
				    //the follows a call to m do:
				    decideHandleRet(cl,(Return)i,s0);
				}
				break;
		    case ASYNCCALL:
		    case SYNCCALL: //exec(i, s) // i is a call
				i = x.getInst();
				s = x.getState();
				// suppose i is a call to m
				m = ((Call)i).getMethodRef();
				lv = m.getParametersAsVar();
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

    public void storeInTable(Instruction i, AbstractState s) {
		AbstractState s0,s1;
		List<Instruction> li;
		s0 = table.get(i.getLabel());
		s1 = domain.lub(s,s0);
		if ( !domain.le(s1,s0) ) {
		    table.put(i.getLabel(),s1);
		    li = i.getNextInsts();
		    li.forEach((inst)->Q.add( new Event(TypeEvent.EXEC,inst, s1) ));
		}
    }

    public void decideHandleRet(Call cl, Return r, AbstractState s) {
		AbstractState s0, s1,s2;
		List<Variable> lv,lv2;
		Variable retPoint;
		Method m;
		m = cl.getMethodRef();
		lv = m.getParametersAsVar();
		lv2 = cl.getArgs();
		retPoint = cl.getReturn();
		Terminal t = r.getVar();
		if (t != null && t.getType() == TypeTerm.VARIABLE && retPoint != null){
			lv.add((Variable)t);
			lv2.add(retPoint);
		}
		s0 = domain.rename(s, lv,lv2);//m.formalParam()+m.retvar(), m.actualParam()+m.retPoint());
		s1 = table.get(m.getLabel());
		s2 = domain.extend(s1, s0);
		if ( !domain.le(s2,s1) ) {
		    table.put( m.getLabel(), s2 );
		    Q.add ( new Event( TypeEvent.EXEC, cl, s2 ) );
		}
    }

    public void decideStoreCall(Call c, AbstractState s) {
		AbstractState s0, s1;
		Method m = c.getMethodRef();
		s0 = table.get(m.getLabel());
		s1 = domain.lub(s,s0);
		if ( !domain.le(s1,s0) ) {
		    table.put(m.getLabel(),s1);
		    Q.add ( new Event( TypeEvent.CALL, c, s1 ) );
		}
    }
}
