package glat.program.instructions.expressions;

import java.util.ArrayList;
import java.util.List;

import glat.program.instructions.Expression;

public class CompoundExpr implements Terminal{
	
	public CompoundExpr(){
		operator = "";
		operands = new ArrayList<Expression>();
	}

	public String toString(){
		return "$op("+operator+","+operands+")";
	}
	
	public String getOperator(){
		return operator;
	}
	
	public List<Expression> getOperands(){
		return operands;
	}
	
	public Expression getOperand(int pos){
		return operands.get(pos);
	}
	
	public void setOperator(String op){
		operator = op;
	}
	
	public void addOperand(Expression e){
		operands.add(e);
	}
	
	private List<Expression> operands;
	private String operator;
	
	
	@Override
	public String getType() {
		throw new UnsupportedOperationException(getClass().getName()+" does not support getType");
	}
	 
}