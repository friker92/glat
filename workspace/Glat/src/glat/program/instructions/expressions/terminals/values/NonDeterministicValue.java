package glat.program.instructions.expressions.terminals.values;

import glat.parser.Token;
import glat.program.instructions.expressions.terminals.Values;

public class NonDeterministicValue implements Values {

	public NonDeterministicValue(){}
	public NonDeterministicValue(Token t){}
	
	@Override
	public boolean isVar() {return false;}
	
	@Override
	public String getValue() {
		return "*";
	}
	
	@Override
	public int getIntNumber() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	public float getFloatNumber() {
		return Float.MAX_VALUE;
	}
	
	@Override
	public boolean getBoolean() {
		return false;
	}
	@Override
	public String getType() {
		return "nondeterministic";
	}
	
}
