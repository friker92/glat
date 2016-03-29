package glat.example.fixpoint;

import java.util.List;

import glat.fixpoint.AbstractDomain;
import glat.fixpoint.AbstractState;
import glat.program.Declaration;
import glat.program.Instruction;
import glat.program.instructions.expressions.terminals.Variable;

public class PosNegDomain implements AbstractDomain<PosNegState> {
	
	public enum V {
		TOP,
		POS,
		CERO,
		NEG,
		NONE //BOT
	}

	@Override
	public PosNegState initVars(List<Declaration> vars) {
		PosNegState p = new PosNegState();
		vars.forEach((d)->p.add(d.getLabel(), V.NONE));
		return p;
	}

	@Override
	public PosNegState empty() {
		return new PosNegState();
	}

	@Override
	public PosNegState abstractExec(Instruction i, PosNegState st) {
		st.add("s", V.CERO);
		return st;
	}

	@Override
	public PosNegState extend(PosNegState s0, PosNegState st) {
		// TODO Auto-generated method stub
		return s0;
	}

	@Override
	public PosNegState project(PosNegState s0, List<Variable> lv) {
		// TODO Auto-generated method stub
		return s0;
	}

	@Override
	public PosNegState rename(PosNegState s0, List<Variable> actual, List<Variable> formal) {
		// TODO Auto-generated method stub
		return s0;
	}

	@Override
	public PosNegState lub(PosNegState s0, PosNegState s1) {
		// TODO Auto-generated method stub
		return s0;
	}

	@Override
	public boolean le(PosNegState s0, PosNegState s1) {
		// TODO Auto-generated method stub
		return s0.size() <= s1.size();
	}

}
