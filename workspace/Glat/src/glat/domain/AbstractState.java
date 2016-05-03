package glat.domain;

import java.util.List;

import glat.program.instructions.expressions.terminals.Variable;

/**
 * 
 * @author Samir Genaim
 *
 */
public interface AbstractState {
	
	/**
	 * 
	 * @return
	 */
	public List<Variable> getVars();
	
	/**
	 * 
	 * @return
	 */
	public AbstractState copy();
	
	/**
	 * 
	 * @return
	 */
	default public String getDesc() {
		return getClass().getName();
	}
	
	/**
	 * 
	 * @param var
	 * @param value
	 * @return
	 */
	default public AbstractValue setValue(Variable var, AbstractValue value) {
		throw new UnsupportedOperationException(getDesc()+ " does not support setValue");
	}

	/**
	 * 
	 * @param var
	 * @return
	 */
	default public AbstractValue getValue(Variable var) {
		throw new UnsupportedOperationException(getDesc()+ " does not support getValue");
	}

}
