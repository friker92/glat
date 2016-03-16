package glat.program.instructions.expressions.terminals;

import glat.program.instructions.expressions.Terminal;

public class Number implements Terminal{

	public Number(String s){
		value = s;
	}
	
	@Override
	public TypeTerm getType() {
		return TypeTerm.NUMBER;
	}

	@Override
	public String getValue() {
		return value;
	}
	
	public String toString(){
		return value;
	}
	
	private String value;

}