package mtanalysis.domains.nonrel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import glat.program.instructions.expressions.terminals.Variable;
import mtanalysis.domains.AbstractState;

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
		for (Variable v : vars) {
			st.put(v, defaultValue(v));
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

	protected abstract AbstractValue defaultValue(Variable v);

	public void extend(NonRelAbstractState stt) {
		stt.st.forEach((v, s) -> setValue(v, s));
	}

	public void rename(Variable s1, Variable s2) {
		setValue(s2, getValue(s1));
		vars.add(s2);
		st.remove(s1);
		vars.remove(s1);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof NonRelAbstractState){
			NonRelAbstractState nrobj = (NonRelAbstractState) obj;
			if(this.vars.size() != nrobj.vars.size())
				return false;
			for(Variable v: nrobj.vars){
				if(!this.st.get(v).equals(nrobj.st.get(v)))
					return false;
			}
			return true;
		}
		return super.equals(obj);
	}
}
