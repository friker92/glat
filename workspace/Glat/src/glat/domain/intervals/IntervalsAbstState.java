package glat.domain.intervals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import glat.domain.AbstractState;
import glat.domain.nonrel.AbstractValue;
import glat.domain.nonrel.NonRelAbstractState;
import glat.domain.sign.SignAbstValue;
import glat.program.instructions.expressions.terminals.Variable;

public class IntervalsAbstState extends NonRelAbstractState {

	protected IntervalsAbstState() {
	}
	
	public IntervalsAbstState(List<Variable> vars) {
		super(vars);
	}



	@Override
	public String getDesc() {
		return "Intervals Abstract State ("+getClass().getName()+")";
	}
	
	@Override
	protected NonRelAbstractState createInstance() {
		return new IntervalsAbstState();
	}

	@Override
	protected AbstractValue bottomValue() {
		return new IntervalsAbstValue();
	}
	
	
}
