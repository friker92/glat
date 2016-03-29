package glat.fixpoint;

import java.util.List;

import glat.program.Declaration;
import glat.program.Instruction;
import glat.program.instructions.expressions.terminals.Variable;

public interface AbstractDomain<T extends AbstractState> {
	
	public T initVars(List<Declaration> vars);
	public T empty();
	
	public T abstractExec(Instruction i, T st);
	
	public T extend(T s0, T st);
	
	public T project(T s0, List<Variable> lv);
	
	public T rename(T s0, List<Variable> actual, List<Variable> formal);
	
	public T lub(T s0,T s1);
	
	public boolean le(T s0,T s1);
}
