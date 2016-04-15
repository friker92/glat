package glat.fixpoint;

import java.util.List;

import glat.program.Declaration;
import glat.program.GlatInstruction;
import glat.program.instructions.expressions.terminals.Variable;

public interface AbstractDomain {
	
	public AbstractState initVars(List<Declaration> vars);
	public AbstractState empty();
	
	public AbstractState abstractExec(GlatInstruction i, AbstractState st);
	
	public AbstractState extend(AbstractState s0, AbstractState st);
	
	public AbstractState project(AbstractState s0, List<Variable> lv);
	
	public AbstractState rename(AbstractState s0, List<Variable> actual, List<Variable> formal);
	
	//least upper bound
	public AbstractState lub(AbstractState s0, AbstractState s1);
	
	public boolean le(AbstractState s0, AbstractState s1);
}
