package glat.program.instructions.expressions.terminals.values;

import glat.program.instructions.expressions.TypeValue;
import glat.program.instructions.expressions.terminals.Value;

public class BoolValue implements Value {
	// TODO : this class is never used, because we dont accept booleans

	private Boolean value;
	
	public BoolValue(String s){
		value = Boolean.parseBoolean(s);
	}

	@Override
	public Boolean getValue() {
		return value;
	}


	@Override
	public int getIntNumber() {
		return (value)?1:0;
	}


	@Override
	public float getFloatNumber() {
		return (value)?1.0F:0.0F;
	}


	@Override
	public boolean getBoolean() {
		return value;
	}

	@Override
	public TypeValue getType() {
		return TypeValue.fromString("bool");
	}
	
	@Override
	public String toString(){
		return value.toString();
	}

}