package glat.program.instructions.expressions.terminals;

import glat.Token;
import glat.program.Declaration;
import glat.program.instructions.expressions.Terminal;

public class Variable implements Terminal{

	public Variable(Declaration d, Token s){
		decl = d;
		value = s.image;
	}
	
	@Override
	public TypeTerm getType() {
		return TypeTerm.VARIABLE;
	}

	@Override
	public String getValue() {
		return value;
	}
	
	public Declaration getDeclaration(){
		return decl;
	}
	
	public String toString(){
		return value;
	}
	
	private Declaration decl;
	private String value;

}
