package glat.program.instructions.expressions.terminals;

import glat.Token;
import glat.program.instructions.expressions.Terminal;

public class TopValue implements Terminal {

	public TopValue(){}
	public TopValue(Token t){}
	
	@Override
	public TermType getType() {
		return TermType.TOP;
	}

	@Override
	public String getValue() {
		return "*";
	}
	
	public String toString(){
		return "*";
	}

}
