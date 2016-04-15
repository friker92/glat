package glat.program.instructions.expressions.terminals;

import glat.parser.Token;
import glat.program.Declaration;
import glat.program.instructions.expressions.Terminal;

public class Variable implements Terminal{

	public Variable(Token s){
		name = s.image;
	}
	
	public Variable(Declaration d){
		decl = d;
		name = d.getName();
	}

	@Override
	public TypeTerm getType() {
		return TypeTerm.VARIABLE;
	}

	@Override
	public String getValue() {
		return name;
	}
	
	public Declaration getDeclaration(){
		return decl;
	}
	public void setDeclaration(Declaration d){
		decl = d;
	}
	
	public String toString(){
		return name;
	}
	
	private Declaration decl;
	private String name;
	public String getLabel() {
		return decl.getLabel();
	}

}
