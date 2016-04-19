package glat.program.instructions.expressions.terminals;

import glat.parser.Token;
import glat.program.GlatClass;
import glat.program.Method;
import glat.program.instructions.expressions.Terminal;

public class Variable extends GlatClass implements Terminal {

	public Variable(String type, Token s){
		name = s.image;
		this.type = type;
	}
	
	public String getName(){
		return name;
	}
	
	public String toString(){
		return name;
	}
	
	@Override
	public String getLabel() {
		return name;
	}
	
	@Override
	public String getType(){
		return type;
	}
	
	@Override
	public boolean isVar() {return true;}

	private String name;
	private String type;

	
	
}
