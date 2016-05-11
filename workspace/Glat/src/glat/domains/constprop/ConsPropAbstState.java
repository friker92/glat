package glat.domains.constprop;

import java.util.List;

import glat.domains.nonrel.AbstractValue;
import glat.domains.nonrel.NonRelAbstractState;
import glat.program.instructions.expressions.terminals.Variable;

public class ConsPropAbstState extends NonRelAbstractState {

	protected ConsPropAbstState() {
	}
	
	public ConsPropAbstState(List<Variable> vars) {
		super(vars);
	}

	@Override
	public String getDesc() {
		return "Constants propagation abstarct state";
	}

	@Override
	protected NonRelAbstractState createInstance() {
		return new ConsPropAbstState();
	}

	@Override
	protected AbstractValue defaultValue(Variable v) {
		return new ConsPropBOT();
	}

}
