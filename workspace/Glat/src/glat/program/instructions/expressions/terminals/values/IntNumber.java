package glat.program.instructions.expressions.terminals.values;

import glat.program.instructions.expressions.TypeValue;

public class IntNumber implements NumberValue {

	private Integer value;
	
	public IntNumber(String s){
		value = Integer.parseInt(s);
	}

	@Override
	public Integer getValue() {
		return value;
	}

	@Override
	public int getIntNumber() {
		return value;
	}


	@Override
	public float getFloatNumber() {
		return value;
	}


	@Override
	public boolean getBoolean() {
		return value != 0;
	}

	@Override
	public TypeValue getType() {
		return TypeValue.fromString("int");
	}

	@Override
	public String toString(){
		return value.toString();
	}
	
}