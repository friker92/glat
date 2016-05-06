package glat.domain;

import java.util.List;

import glat.program.instructions.expressions.terminals.Variable;

public abstract class NonRelAbstractDomain implements AbstractDomain {
	
	@Override
	public AbstractState lub(AbstractState a, AbstractState b) {
		List<Variable> vars = a.getVars();

		NonRelAbstractState c = (NonRelAbstractState) bottom(vars);
		
		for (Variable v : vars) {
			c.setValue(v, a.getValue(v).lub(b.getValue(v)) );
		}

		return c;
	}

	@Override
	public boolean lte(AbstractState a, AbstractState b) {
		List<Variable> vars = a.getVars();

		for (Variable v : vars) {
			if (!a.getValue(v).lte(b.getValue(v))) {
				return false;
			}
		}

		return true;
	}


}
