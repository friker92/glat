package glat.program.instructions.expressions;

import glat.program.instructions.Expression;

public class CompoundExpr implements Expression {

	public String toString() {
		return ""+op1+" " + operator + " " + op2;
	}

	public TypeOperator getOperator() {
		return operator;
	}

	public Terminal getOperandLeft(){
		return op1;
	}
	public Terminal getOperandRight(){
		return op2;
	}
	
	public void setOperator(String op) {
		operator = TypeOperator.fromString(op);
	}

	public void addOperandLeft(Terminal e) {
		op1 = e;
	}
	
	public void addOperandRight(Terminal e) {
		op2 = e;
	}

	private Terminal op1,op2;
	private TypeOperator operator;

}