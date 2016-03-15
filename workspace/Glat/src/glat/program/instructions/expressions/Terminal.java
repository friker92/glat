package glat.program.instructions.expressions;

import glat.program.instructions.expressions.terminals.TermType;

public interface Terminal {
	public TermType getType();
	public String	getValue();
}
