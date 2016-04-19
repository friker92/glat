package glat.program.instructions.expressions.terminals;

import glat.program.instructions.expressions.Terminal;

public interface Values extends Terminal {
	public String	getValue();
	public int		getIntNumber();
	public float	getFloatNumber();
	public boolean	getBoolean();
}
