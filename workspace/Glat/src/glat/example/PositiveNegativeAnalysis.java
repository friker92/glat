package glat.example;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jgrapht.WeightedGraph;

import glat.program.Declaration;
import glat.program.Instruction;
import glat.program.Method;
import glat.program.Program;
import glat.program.Transition;
import glat.program.instructions.Asignation;
import glat.program.instructions.Call;
import glat.program.instructions.Expression;
import glat.program.instructions.Return;
import glat.program.instructions.expressions.Terminal;
import glat.program.instructions.expressions.terminals.Variable;

public class PositiveNegativeAnalysis implements Analysis {
	public int MAX_LOOP = 5;
	public enum V {
		POS(1),
		CERO(0),
		NEG(-1),
		TOP(1),
		BOT(1);
		private int val;
	   V(int numVal) {
	        this.val = numVal;
	    }

	}

	public PositiveNegativeAnalysis(Program p){
		this.p = p;
		vars = new HashMap<String, V>();
		meth = new HashMap<String, V>();
	}
	@Override
	public void print() {
		System.out.println("This Analysis has NO guarantee");
		vars.forEach((s,v)-> System.out.println(""+s+" "+v));
		
	}

	@Override
	public boolean run() {
		List<Declaration> lgv = p.getGlobalVariables();
		Iterator<Declaration> itgv = lgv.iterator();
		while(itgv.hasNext()){
			Declaration d = itgv.next();
			if(d.getType().equals("int") || d.getType().equals("float"))
				vars.put(d.getEnv()+"_"+d.getName(), V.BOT);
		}
		Iterator<Method> itm = p.getMethods().iterator();
		while(itm.hasNext()){
			meth.put(itm.next().getLabel(), V.BOT);
		}
		Method m = p.getEntryMethod();
		method(m);
		
		return true;
	}
	
	private V method(Method m){
		if(meth.get(m.getLabel())!= V.BOT){ //already visit...
			return meth.get(m.getLabel()); // I know it is wrong!
		}
		Iterator<Declaration> itgv = m.getVariables().iterator();
		while(itgv.hasNext()){
			Declaration d = itgv.next();
			if(d.getType().equals("int") || d.getType().equals("float"))
				vars.put(d.getEnv()+"_"+d.getName(), V.BOT);
		}
		String entry = m.getEntryPoint();
		HashMap<String, Integer> edges = new HashMap<String, Integer>();
		WeightedGraph<String, Transition> cfg = m.getCFG();
		Iterator<Transition> itvs = cfg.edgeSet().iterator();
		while(itvs.hasNext()){
			edges.put(itvs.next().getLabel(), 0);
		}
		cfg(m,cfg,entry,edges);
		if(meth.get(m.getLabel())== V.BOT){ // I know I know...
			meth.put(m.getLabel(),V.TOP);
		}
		return meth.get(m.getLabel());
	}
	
	private void cfg(Method m,WeightedGraph<String, Transition> cfg,String vertex,HashMap<String, Integer> edges){
		Iterator<Transition> its = cfg.edgesOf(vertex).iterator();
		while(its.hasNext()){
			Transition tr = its.next();
			String label = tr.getLabel();
			int nloop = edges.get(label);
			if(!tr.getSource().equals(vertex) || nloop >= MAX_LOOP)
				continue;
			edges.put(label, nloop+1);
			Iterator<Instruction> itv = tr.getCode().iterator();
			while(itv.hasNext()){
				inst(m,itv.next());
			}
			cfg(m,cfg,tr.getDestination(),edges);
		}
	}
	
	private V inst(Method m,Instruction i){
		switch (i.getType()){
		case ASIGNATION:
			Asignation as = (Asignation) i;
			Variable d = as.getDest();
			V val = expr(m,as.getExpr());
			vars.put(d.getDeclaration().getEnv()+"_"+d.getDeclaration().getName(), val);
			break;
		case RETURN:
			V a = term(((Return)i).getVar());
			meth.put(m.getLabel(),a);
			break;
		case SYNCCALL:
		case ASYNCCALL:
			Call cl = (Call) i;
			return call(m,cl);
		case ASSERT:
		case ASSUME:
		case UNLOCK:
		case LOCK:
		case JOIN:
			break;
		}
		return V.BOT;
	}
	
	private V call(Method m,Call cl){
		List<Declaration> lp = m.getParameters();
		List<Variable> lv = cl.getArgs();
		Iterator<Declaration>  ip = lp.iterator();
		Iterator<Variable>  iv = lv.iterator();
		while(ip.hasNext() && iv.hasNext()){
			Declaration d = ip.next();
			Variable v = iv.next();
			vars.put(d.getEnv()+"_"+d.getName(), vars.get(v.getDeclaration().getEnv()+"_"+v.getDeclaration().getName()));
		}
		return method(cl.getMethod());
	}
	private V expr(Method m, Expression exp){
		switch (exp.getType()){
		case 0:
			return inst(m,exp.getInst());
		case 1:
			return term(exp.getT1());
		case 2:
			return operation(exp.getOp(),term(exp.getT1()),term(exp.getT2()));
		}
		return V.BOT;
	}
	
	private V operation(String op, V t1, V t2){
		if(t2 == null)
			return V.BOT;
		switch (op){
		case "+":
			return add(t1,t2);
		case "-":

			return sub(t1, t2);
		case "*":
			return mult(t1, t2);
		case "/":
			return div(t1, t2);
		}
		return V.BOT;
	}
	
	private V term(Terminal t){
		switch (t.getType()){
		case NUMBER:
			float n = Float.parseFloat(t.getValue());
			if(n > 0){
				return V.POS;
			}else if (n == 0){
				return V.CERO;
			}else{
				return V.NEG;
			}
		case TOP:
			return V.TOP;
		case VARIABLE:
			Variable v = (Variable) t;
			return vars.get(v.getDeclaration().getEnv()+"_"+v.getDeclaration().getName());
		}
		return V.BOT;
	}
	public V add(V e1,V e2){
		if (e1 == V.BOT || e2 == V.BOT)
			return V.BOT;
		else if (e1 == V.TOP || e2 == V.TOP)
			return V.TOP;
		else if( e1 == V.POS && e2 == V.POS)
			return V.POS;
		else if( e1 == V.NEG && e2 == V.NEG)
			return V.NEG;
		else if( e1== V.CERO )
			return e2;
		else if( e2 == V.CERO )
			return e1;
		else
			return V.BOT;
	}
	public V sub(V e1,V e2){
		if (e1 == V.BOT || e2 == V.BOT)
			return V.BOT;
		else if (e1 == V.TOP || e2 == V.TOP)
			return V.TOP;
		else if( e1 == e2)
			return V.BOT;
		else if( e1== V.CERO && e2 == V.POS)
			return V.NEG; 
		else if( e1== V.CERO && e2 == V.NEG)
			return V.POS; 
		else if( e2 == V.CERO )
			return e1;
		else
			return e1;
	}
	
	public V mult(V e1,V e2){
		if (e1 == V.BOT || e2 == V.BOT)
			return V.BOT;
		else if (e1 == V.CERO || e2 == V.CERO)
			return V.CERO;
		else if (e1 == V.TOP || e2 == V.TOP)
			return V.TOP;
		else if( e1 == e2)
			return V.POS;
		else
			return V.NEG;
	}
	public V div(V e1,V e2){
		if (e1 == V.BOT || e2 == V.BOT)
			return V.BOT;
		else if (e1 == V.TOP || e2 == V.TOP)
			return V.TOP;
		else if (e2 == V.CERO)
			return V.BOT;
		else if (e1 == V.CERO)
			return V.CERO;
		else if(e1 == e2)
			return V.POS;
		else 
			return V.NEG;
	}
	private Program p;
	private HashMap<String, V> vars;
	private HashMap<String, V> meth;
}
