package glat.program.instructions.expressions;

public enum TypeValue {
	INT("int"),FLOAT("float"),LOCK("lock"),THREAD("thread"),ANY("nondeterministic");
	
	private String symbol;

	TypeValue(String sym) {
		this.symbol = sym;
	}

	public String getSymbol() {
	    return this.symbol;
	  }
	
	public static TypeValue fromString(String text) {
		if (text != null) {
			for (TypeValue b : TypeValue.values()) {
				if (text.equalsIgnoreCase(b.symbol)) {
					return b;
				}
			}
		}
		return TypeValue.ANY;
	}
}
