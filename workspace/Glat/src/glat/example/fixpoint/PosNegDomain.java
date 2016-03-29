package glat.example.fixpoint;

import java.util.List;

import glat.fixpoint.AbstractDomain;
import glat.fixpoint.AbstractState;
import glat.program.Declaration;
import glat.program.Instruction;
import glat.program.instructions.expressions.terminals.Variable;

public class PosNegDomain implements AbstractDomain {
	
	public enum V {
		TOP,
		POS,
		CERO,
		NEG,
		NONE //BOT
	}

	@Override
	public AbstractState initVars(List<Declaration> vars) {
		PosNegState p = new PosNegState();
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractState empty() {
		return new PosNegState();
	}

	@Override
	public AbstractState abstractExec(Instruction i, AbstractState st) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractState extend(AbstractState s0, AbstractState st) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractState project(AbstractState s0, List<Variable> lv) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractState rename(AbstractState s0, List<Variable> actual, List<Variable> formal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractState lub(AbstractState s0, AbstractState s1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean le(AbstractState s0, AbstractState s1) {
		// TODO Auto-generated method stub
		return true;
	}

}
