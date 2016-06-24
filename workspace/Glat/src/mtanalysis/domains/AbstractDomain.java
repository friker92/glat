package mtanalysis.domains;

import java.util.ArrayList;
import java.util.List;

import glat.program.Instruction;
import glat.program.Transition;
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
	 * @param vars
	 * @return
	 */
	public AbstractState defaultState(List<Variable> vars);

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public AbstractState lub(AbstractState a, AbstractState b);

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public default AbstractState lub(List<AbstractState> as) {
		if (as.isEmpty())
			return bottom(new ArrayList<Variable>());
		AbstractState st = as.get(0);
		for (AbstractState s : as) {
			st = lub(st, s);
		}
		return st;
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public AbstractState glb(AbstractState a, AbstractState b);

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public default AbstractState glb(List<AbstractState> as) {
		if (as.isEmpty())
			return bottom(new ArrayList<Variable>());
		AbstractState st = as.get(0);
		for (AbstractState s : as) {
			if(st.equals(BottomState.getInstance()))
				return BottomState.getInstance();
			st = glb(st, s);
		}
		return st;
	}

	/**
	 * 
	 * Assume a is smaller than or equal to b
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public AbstractState widen(AbstractState a, AbstractState b);

	/**
	 * 
	 * @return
	 */
	public boolean hasInfiniteAscendingChains();

	/**
	 * 
	 * @return
	 */
	public boolean hasInfiniteDescendingChains();

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
	 * @param instr
	 * @param a
	 * @return
	 */
	public AbstractState exec(Instruction instr, AbstractState a);

	/**
	 * 
	 * @param trans
	 * @param a
	 * @return
	 */
	public default AbstractState exec(Transition t, AbstractState currst) {
		AbstractState st = currst;
		for (Instruction i : t.getInstructions()) {
			st = exec(i, st);
		}
		return st;
	}

	public AbstractState extend(AbstractState s0, AbstractState st);

	public AbstractState project(AbstractState s0, List<Variable> lv);

	public AbstractState rename(AbstractState s0, List<Variable> actual, List<Variable> formal);
}
