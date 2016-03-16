package glat.program.instructions.expressions;

import glat.program.instructions.expressions.terminals.TypeTerm;

public interface Terminal {
	public TypeTerm getType();
	public String	getValue();
}
