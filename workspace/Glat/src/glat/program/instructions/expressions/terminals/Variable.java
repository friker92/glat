package glat.program.instructions.expressions.terminals;

import glat.parser.Token;
import glat.program.GlatClass;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Variable other = (Variable) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	private String name;
	private String type;
	
}
