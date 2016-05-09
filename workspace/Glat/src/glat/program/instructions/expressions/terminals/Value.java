package glat.program.instructions.expressions.terminals;

import glat.program.instructions.expressions.Terminal;

public interface Value extends Terminal {
	public Object	getValue();
	public int		getIntNumber();
	public float	getFloatNumber();
	public boolean	getBoolean();
}
