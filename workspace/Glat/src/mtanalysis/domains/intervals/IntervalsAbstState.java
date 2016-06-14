package mtanalysis.domains.intervals;

import java.util.List;

import glat.program.instructions.expressions.terminals.Variable;
import mtanalysis.domains.nonrel.AbstractValue;
import mtanalysis.domains.nonrel.NonRelAbstractState;

public class IntervalsAbstState extends NonRelAbstractState {

	protected IntervalsAbstState() {
	}

	public IntervalsAbstState(List<Variable> vars) {
		super(vars);
	}

	@Override
	public String getDesc() {
		return "Intervals Abstract State (" + getClass().getName() + ")";
	}

	@Override
	protected NonRelAbstractState createInstance() {
		return new IntervalsAbstState();
	}

	@Override
	protected AbstractValue defaultValue(Variable v) {
		return new IntervalsAbstValue();
	}
}
