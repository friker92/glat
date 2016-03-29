package glat.program.instructions;

import glat.program.Instruction;
import glat.program.instructions.expressions.Terminal;

public class Expression {
	
	public Expression(Terminal e){
		e1=e;
		type = 1;
	}
	public Expression(String o,Terminal e,Terminal f){
		if (o.equals("-1")){
			e1=e;
			type=1;
		}else{
			e1 = e;
			op = o;
			e2 = f;
			type = 2;
		}
	}
	public String toString(){
		switch (type){
		case 1:
			return ""+e1.toString();
		case 2:
			return ""+e1.toString()+" "+op+" "+e2.toString();
		}
		return "";
	}
	
	public int getType(){
		return type;
	}
	public String getOp(){
		return op;
	}
	public Terminal getT1(){
		return e1;
	}
	public Terminal getT2(){
		return e2;
	}
	private Terminal e1,e2;
	private String op;
	private int type;//1 = unary, 2 = binary 
}