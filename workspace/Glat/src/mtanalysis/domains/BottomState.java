package mtanalysis.domains;

import java.util.List;

import glat.program.instructions.expressions.terminals.Variable;

public class BottomState implements AbstractState {

	private List<Variable> vars;

	public BottomState(List<Variable> vars) {
		this.vars = vars;
	}

	public BottomState() {
	}

	@Override
	public List<Variable> getVars() {
		return vars;
	}

	@Override
	public AbstractState copy() {
		return this;
	}

	@Override
	public boolean isBottom() {
		return true;
	}

	@Override
	public String getDesc() {
		return null;
	}

}
