package glat.domain.intervals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import glat.domain.AbstractState;
import glat.program.instructions.expressions.terminals.Variable;

public class IntervalsAbstState implements AbstractState {

	
	private List<Variable> vars;
	private Map<Variable,IntervalsAbstValue> st;
	
	private IntervalsAbstState() {
	}
	
	public IntervalsAbstState(List<Variable> vars) {
		this.vars = vars;
		this.st = new HashMap<Variable,IntervalsAbstValue>();
	}
	
	@Override
	public List<Variable> getVars() {
		return vars;
	}

	@Override
	public AbstractState copy() {
		IntervalsAbstState a = new IntervalsAbstState();
		a.st = new HashMap<>(this.st);
		a.vars = new ArrayList<Variable>(vars);
		return a;
	}

}
