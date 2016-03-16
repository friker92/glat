package glat.fixpoint;

import java.util.HashMap;
import java.util.Queue;
import glat.program.Instruction;
import glat.program.Method;
import glat.program.Program;
import glat.program.instructions.Asignation;
import glat.program.instructions.Call;
import glat.program.instructions.Join;

public class Fixpoint {

	private AbstractDomain domain;
	private Queue<Event> Q;
	private HashMap<String, AbstractState> table;
	private Program program;
	public Fixpoint(AbstractDomain d, Program pr) {
		domain = d;
		program = pr;
	}

	public void fixPoint(){
		//T = new Table();

		//Q = { call( main, {} ) }
		Q.add( new Event( TypeEvent.CALL, program.getEntryMethod(), domain.empty() ) );
		while ( !Q.isEmpty() ) {
		      
		  Event x = Q.poll();
		  execute(x);
		}
	}
	
	public void execute(Event x){
		AbstractState s0,s1,s,state;
		Instruction i;
		Method m;
		  switch (x.getType()) {

		   case CALL://call(m, s) : // state is over the param of m
			   i = x.getInst();
			   s = x.getState();
			   s0 = domain.extend( s, domain.initVars( m.getVariables() ) ); // add local vars
			   i = m.inst(0);
			   Q.add( new Event(TypeEvent.EXEC,i, s0) );
			   break;
		   case EXEC://exec(i,s) : 
			   i = x.getInst();
			   s = x.getState();
			   switch(i.getType()){
			   case RETURN: // exec(i, s) // i is a return
				   //suppose i is a return from m
				   s0 = domain.project(s, m.retvar()+m.formalParm())
				   for(Instruction inst : m.getCallPoints()){
					   //the follows a call to m do:
					   decidehandleRet(inst,m,s0);
				   }
				   break;
			   case ASYNCCALL:
			   case SYNCCALL: //exec(i, s) // i is a call
				   i = x.getInst();
				   s = x.getState();
				   // suppose i is a call to m
				   m = ((Call)i).getMethod();
				   s0 = domain.project( s, m.actulaParams() );
				   s1 = domain.rename( s0, m.actulaParams(), m.getFormalParam() );
				   decideStoreCall(m,s1);
				   break;
			   case ASIGNATION:
				   i = x.getInst();
				   s = x.getState();
				   Asignation as = (Asignation)i;
				   if(as.getExpr().getType()==0){
					   m = ((Call) as.getExpr().getInst()).getMethod();
					   s0 = domain.project( s, m.actulaParams() );
					   s1 = domain.rename( s0, m.actulaParams(), m.getFormalParam() );
					   decideStoreCall(m,s1);
				   }else{
					   // i is not a call and not exit
					   s0 = domain.abstractExec(i, s);
					   storeintable(i,s0);
				   }
				   break;
			   default:
				   i = x.getInst();
				   s = x.getState();
				   // i is not a call and not exit
				   s0 = domain.abstractExec(i, s);
				   storeintable(i,s0);
				   break;
				 
			   }
		  }
	}

	public void storeintable(Instruction i, AbstractState s) {
			AbstractState s0,s1;
		  s0 = table.get(i);
		  s1 = domain.lub(s,s0);
		  if ( !domain.le(s1,s0) ) {
		    table.put(i.getLabel(),s1);
		    Q.add ( new Event( TypeEvent.EXEC, i.next(), s1 ) );
		  }
		}

	public void decidehandleRet(Instruction inst,Method m,AbstractState s) {
			AbstractState s0, s1,s2,s3;
		  s0 = domain.rename(s, m.formalParam(), m.actualParam());
		  s1 = domain.rename(s0, m.retvar());
		  s2 = table.get(inst);
		  s3 = domain.extend(s2, s1);
		  if ( !domain.le(s3,s2) ) {
		    table.put( inst.getLabel(), s3 );
		    Q.add ( new Event( TypeEvent.EXEC, inst, s3 ) );
		}
	}

	public void decideStoreCall(Method m, AbstractState s) {
		AbstractState s0, s1;  
		s0 = table.get(m);
		s1 = domain.lub(s,s0);
		if ( !domain.le(s1,s0) ) {
			table.put(m.getLabel(),s1);
		    Q.add ( new Event( TypeEvent.CALL, m, s1 ) );
		}
	}
}
