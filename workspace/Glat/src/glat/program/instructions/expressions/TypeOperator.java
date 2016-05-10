package glat.program.instructions.expressions;

public enum TypeOperator {
	// Arith
	ADD("+"), SUB("-"), DIV("/"), MUL("*"),
	// bool
	LT("<"), LTE("<="), GT(">"), GTE(">="), EQ("=="), NEQ("!=");

	private String symbol;

	TypeOperator(String sym) {
		this.symbol = sym;
	}

	public String getSymbol() {
	    return this.symbol;
	  }
	
	public static TypeOperator fromString(String text) {
		if (text != null) {
			for (TypeOperator b : TypeOperator.values()) {
				if (text.equalsIgnoreCase(b.symbol)) {
					return b;
				}
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return this.symbol;
	}
}
