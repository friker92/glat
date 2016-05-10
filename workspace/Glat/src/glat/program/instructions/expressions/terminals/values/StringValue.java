package glat.program.instructions.expressions.terminals.values;

import glat.program.instructions.expressions.TypeValue;
import glat.program.instructions.expressions.terminals.Value;

public class StringValue implements Value {
	// TODO : this class is never used, because we dont accept strings
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
	public TypeValue getType() {
		return TypeValue.fromString("string");
	}
	
	@Override
	public String toString(){
		return value.toString();
	}

}