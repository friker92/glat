package glat.program.instructions.expressions.terminals.values;

import glat.program.instructions.expressions.terminals.Values;

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
	public String getType() {
		return "int";
	}

	@Override
	public String toString(){
		return value.toString();
	}
	
}