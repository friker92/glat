package glat.program.instructions.expressions.terminals.values;

import glat.program.instructions.expressions.terminals.Values;

public class StringValue implements Values {

	private String value;
	
	public StringValue(String s){
		value = s;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public int getIntNumber() {
		return value.length();
	}


	@Override
	public float getFloatNumber() {
		return value.length();
	}


	@Override
	public boolean getBoolean() {
		return value.length() != 0;
	}

	@Override
	public String getType() {
		return "string";
	}
	
	@Override
	public String toString(){
		return value.toString();
	}

}