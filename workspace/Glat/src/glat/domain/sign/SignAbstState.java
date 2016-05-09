package glat.domain.sign;

import java.util.List;

import glat.domain.nonrel.AbstractValue;
import glat.domain.nonrel.NonRelAbstractState;
import glat.program.instructions.expressions.terminals.Variable;

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
	protected AbstractValue bottomValue() {
		return SignAbstValue.BOT;
	}

}
