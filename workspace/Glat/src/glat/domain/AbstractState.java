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
	public String getDesc();

}
