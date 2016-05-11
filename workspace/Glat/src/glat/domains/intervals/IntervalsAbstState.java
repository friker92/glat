package glat.domains.intervals;

import java.util.List;
import glat.domains.nonrel.AbstractValue;
import glat.domains.nonrel.NonRelAbstractState;
import glat.program.instructions.expressions.terminals.Variable;

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
