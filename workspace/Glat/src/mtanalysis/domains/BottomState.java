package mtanalysis.domains;

import java.util.List;

import glat.program.instructions.expressions.terminals.Variable;

public class BottomState implements AbstractState {

	private static BottomState instance = null;
	
	private List<Variable> vars;

	protected BottomState(List<Variable> vars) {
		this.vars = vars;
	}

	protected BottomState() {
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

	public static BottomState getInstance() {
		if (instance == null) {
			instance = new BottomState();
		}
		return instance;
	}

}
