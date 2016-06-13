package glat.program.instructions.expressions;

public enum TypeBoolOperator {
	// Arith
	//ADD("+"), SUB("-"), DIV("/"), MUL("*"),
	// bool
	LT("<"), LTE("<="), GT(">"), GTE(">="), EQ("=="), NEQ("!=");

	private String symbol;

	TypeBoolOperator(String sym) {
		this.symbol = sym;
	}

	public String getSymbol() {
	    return this.symbol;
	  }
	
	public static TypeBoolOperator fromString(String text) {
		if (text != null) {
			for (TypeBoolOperator b : TypeBoolOperator.values()) {
				if (text.equalsIgnoreCase(b.symbol)) {
					return b;
				}
			}
		}
		return null;
	}
	
	public TypeBoolOperator flip(){
		switch(this){
		case EQ:
			return EQ;
		case GT:
			return LT;
		case GTE:
			return LTE;
		case LT:
			return GT;
		case LTE:
			return GTE;
		case NEQ:
			return NEQ;
		default:
			return null;
		}
	}
	
	public TypeBoolOperator neg(){
		switch(this){
		case EQ:
			return NEQ;
		case GT:
			return LTE;
		case GTE:
			return LT;
		case LT:
			return GTE;
		case LTE:
			return GT;
		case NEQ:
			return EQ;
		default:
			return null;
		}
	}
	
	@Override
	public String toString() {
		return this.symbol;
	}
}
