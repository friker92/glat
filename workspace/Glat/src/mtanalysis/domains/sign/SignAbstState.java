package mtanalysis.domains.sign;

import java.util.List;

import glat.program.instructions.expressions.terminals.Variable;
import mtanalysis.domains.nonrel.AbstractValue;
import mtanalysis.domains.nonrel.NonRelAbstractState;

/**
 * 
 *
 */
public class SignAbstState extends NonRelAbstractState {

	/**
	 * 
	 */
	protected SignAbstState() {
	}

	/**
	 * 
	 * @param vars
	 */
	public SignAbstState(List<Variable> vars) {
		super(vars);
	}


	@Override
	public String getDesc() {
		return "Sign Abstract State ("+getClass().getName()+")";
	}

	@Override
	protected NonRelAbstractState createInstance() {
		return new SignAbstState();
	}

	@Override
	protected AbstractValue defaultValue(Variable v) {
		return SignAbstValue.ZERO;
	}

}
