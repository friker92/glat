package glat.domain;

import java.util.List;

import glat.program.instructions.expressions.terminals.Variable;

public abstract class NonRelAbstractDomain implements AbstractDomain {

	@Override
	public AbstractState lub(AbstractState a, AbstractState b) {
		NonRelAbstractState nonRel_a = (NonRelAbstractState) a;
		NonRelAbstractState nonRel_b = (NonRelAbstractState) b;

		List<Variable> vars = a.getVars();

		NonRelAbstractState c = (NonRelAbstractState) bottom(vars);
		
		for (Variable v : vars) {
			c.setValue(v, nonRel_a.getValue(v).lub(nonRel_b.getValue(v)) );
		}

		return c;
	}

	@Override
	public boolean lte(AbstractState a, AbstractState b) {
		NonRelAbstractState nonRel_a = (NonRelAbstractState) a;
		NonRelAbstractState nonRel_b = (NonRelAbstractState) b;
		
		List<Variable> vars = a.getVars();

		for (Variable v : vars) {
			if (!nonRel_a.getValue(v).lte(nonRel_b.getValue(v))) {
				return false;
			}
		}

		return true;
	}


}
