package glat.domain.sign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import glat.domain.AbstractState;
import glat.domain.AbstractValue;
import glat.program.instructions.expressions.terminals.Variable;

/**
 * 
 *
 */
public class SignAbstState implements AbstractState {

	/**
	 * 
	 */
	private List<Variable> vars;

	/**
	 * 
	 */
	private Map<Variable, SignAbstValue> st;

	/**
	 * 
	 */
	private SignAbstState() {
	}

	/**
	 * 
	 * @param vars
	 */
	public SignAbstState(List<Variable> vars) {
		this.vars = new ArrayList<Variable>(vars);
		st = new HashMap<Variable, SignAbstValue>();
		initialize();
	}

	/**
	 * 
	 */
	private void initialize() {
		for (Variable v : vars) {
			st.put(v, SignAbstValue.BOT);
		}
	}

	/**
	 * 
	 * @param var
	 * @param value
	 * @return
	 */
	@Override
	public SignAbstValue setValue(Variable var, AbstractValue value) {
		return st.put(var, (SignAbstValue)value);
	}

	/**
	 * 
	 * @param var
	 * @return
	 */
	@Override
	public SignAbstValue getValue(Variable var) {
		return st.get(var);
	}

	@Override
	public List<Variable> getVars() {
		return vars;
	}

	@Override
	public AbstractState copy() {
		SignAbstState a = new SignAbstState();
		a.st = new HashMap<>(this.st);
		a.vars = new ArrayList<Variable>(vars);
		return a;
	}

	@Override
	public String getDesc() {
		return "Sign Abstract State ("+getClass().getName()+")";
	}

	@Override
	public String toString() {
		return st.toString();
	}
}
