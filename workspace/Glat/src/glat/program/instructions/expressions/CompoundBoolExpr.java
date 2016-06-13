package glat.program.instructions.expressions;

import glat.program.instructions.Expression;

public class CompoundBoolExpr implements Expression {

	public String toString() {
		return ""+op1+" " + operator + " " + op2;
	}

	public TypeBoolOperator getOperator() {
		return operator;
	}

	public Terminal getOperandLeft(){
		return op1;
	}
	public Terminal getOperandRight(){
		return op2;
	}
	
	public void setOperator(String op) {
		operator = TypeBoolOperator.fromString(op);
	}

	public void addOperandLeft(Terminal e) {
		op1 = e;
	}
	
	public void addOperandRight(Terminal e) {
		op2 = e;
	}

	private Terminal op1,op2;
	private TypeBoolOperator operator;

}