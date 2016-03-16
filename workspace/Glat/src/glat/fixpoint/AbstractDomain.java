package glat.fixpoint;

import java.util.List;

import glat.program.Declaration;
import glat.program.Instruction;

public interface AbstractDomain {
	
	public AbstractState initVars(List<Declaration> vars);
	public AbstractState empty();
	
	public AbstractState abstractExec(Instruction i, AbstractState st);
	
	public AbstractState extend(AbstractState s0, AbstractState st);
	
	public AbstractState project(AbstractState s0, AbstractState st);
	
	public AbstractState rename(AbstractState s0, AbstractState st,AbstractState st1);
	
	public AbstractState lub(AbstractState s0,AbstractState s1);
	
	public boolean le(AbstractState s0,AbstractState s1);
}
