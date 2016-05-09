package glat.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import glat.domain.sign.SignAbstValue;
import glat.program.instructions.expressions.terminals.Variable;

public abstract class NonRelAbstractState implements AbstractState {

	/**
	 * 
	 */
	private List<Variable> vars;

	/**
	 * 
	 */
	private Map<Variable, AbstractValue> st;

	/**
	 * 
	 */
	protected NonRelAbstractState() {
	}

	public NonRelAbstractState(List<Variable> vars) {
		this.vars = new ArrayList<Variable>(vars);
		st = new HashMap<Variable, AbstractValue>();
		for ( Variable v : vars ) {
			st.put(v, bottomValue());
		}
	}

	@Override
	public List<Variable> getVars() {
		return vars;
	}

	/**
	 * 
	 * @param var
	 * @param value
	 * @return
	 */
	public AbstractValue setValue(Variable var, AbstractValue value) {
		return st.put(var, value);
	}

	/**
	 * 
	 * @param var
	 * @return
	 */
	public AbstractValue getValue(Variable var) {
		return st.get(var);
	}

	@Override
	public AbstractState copy() {
		NonRelAbstractState a = createInstance();
		a.st = new HashMap<>(this.st);
		a.vars = new ArrayList<Variable>(vars);
		return a;
	}

	@Override
	public String toString() {
		return st.toString();
	}

	protected abstract NonRelAbstractState createInstance();
	protected abstract AbstractValue bottomValue();

}
