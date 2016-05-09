package glat.domains;

import java.util.List;

import glat.program.Instruction;
import glat.program.instructions.expressions.terminals.Variable;

/**
 * 
 *
 */
public interface AbstractDomain {

	/**
	 * 
	 * @param vars
	 * @return
	 */
	public AbstractState top(List<Variable> vars);

	/**
	 * 
	 * @param vars
	 * @return
	 */
	public AbstractState bottom(List<Variable> vars);

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public AbstractState lub(AbstractState a, AbstractState b);

	/**
	 * Returns {@code true} if {@code a} is smaller than or equal to {@code b}
	 * with respect to the order of the corresponding domain.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean lte(AbstractState a, AbstractState b);

	/**
	 * 
	 * @param intsr
	 * @param a
	 * @return
	 */
	public AbstractState exec(Instruction intsr, AbstractState a);
}
