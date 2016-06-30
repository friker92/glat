package glat.program.instructions.expressions;

import glat.program.GlatProgram;
import glat.program.Program;
import glat.program.instructions.Expression;
import glat.program.instructions.expressions.terminals.Variable;

public class CompoundArithExpr implements Expression {

	public String toString() {
		return ""+op1+" " + operator + " " + op2;
	}

	public TypeArithOperator getOperator() {
		return operator;
	}

	public Terminal getOperandLeft(){
		return op1;
	}
	public Terminal getOperandRight(){
		return op2;
	}
	
	public void setOperator(String op) {
		operator = TypeArithOperator.fromString(op);
	}

	public void addOperandLeft(Terminal e) {
		op1 = e;
	}
	
	public void addOperandRight(Terminal e) {
		op2 = e;
	}

	private Terminal op1,op2;
	private TypeArithOperator operator;
	
	

}