package glat.domain.intervals;

import java.util.List;

import glat.domain.AbstractDomain;
import glat.domain.AbstractState;
import glat.program.Instruction;
import glat.program.instructions.expressions.terminals.Variable;

public class IntervalsAbstDomain implements AbstractDomain {

	@Override
	public AbstractState bottom(List<Variable> vars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractState lub(AbstractState a, AbstractState b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean lte(AbstractState a, AbstractState b) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AbstractState exec(Instruction intsr, AbstractState a) {
		// TODO Auto-generated method stub
		return null;
	}

}
