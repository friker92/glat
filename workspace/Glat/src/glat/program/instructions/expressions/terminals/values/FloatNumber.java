package glat.program.instructions.expressions.terminals.values;

import glat.program.instructions.expressions.terminals.Values;

public class FloatNumber implements Values {

	private Float value;
	
	public FloatNumber(String s){
		value = Float.parseFloat(s);
	}
	
	@Override
	public boolean isVar() {return false;}

	@Override
	public String getValue() {
		return value.toString();
	}

	@Override
	public int getIntNumber() {
		return value.intValue();
	}

	@Override
	public float getFloatNumber() {
		return value;
	}

	@Override
	public boolean getBoolean() {
		return value != 0.0F;
	}

	@Override
	public String getType() {
		return "float";
	}

}