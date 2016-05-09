package glat.domains.intervals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import glat.domains.AbstractState;
import glat.domains.nonrel.AbstractValue;
import glat.domains.nonrel.NonRelAbstractState;
import glat.domains.sign.SignAbstValue;
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
