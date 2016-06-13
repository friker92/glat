package glat.program.instructions.expressions;

public enum TypeArithOperator {
	// Arith
	ADD("+"), SUB("-"), DIV("/"), MUL("*");//,
	// bool
	//LT("<"), LTE("<="), GT(">"), GTE(">="), EQ("=="), NEQ("!=");

	private String symbol;

	TypeArithOperator(String sym) {
		this.symbol = sym;
	}

	public String getSymbol() {
	    return this.symbol;
	  }
	
	public static TypeArithOperator fromString(String text) {
		if (text != null) {
			for (TypeArithOperator b : TypeArithOperator.values()) {
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
