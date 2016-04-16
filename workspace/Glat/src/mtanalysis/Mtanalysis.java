package mtanalysis;

import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import glat.parser.Glat;
import glat.program.GlatMethod;
import glat.program.GlatTransition;
import glat.program.Node;
import glat.program.Program;
import glat.program.Transition;
import glat.program.instructions.ThreadLaunch;

public class Mtanalysis {

	public Mtanalysis(){
		outInter = new HashMap<AbsState,Interference>();
		inInter = null;
		
	}
	public static void main(String[] args) {
		Glat g = new Glat();
		Mtanalysis mt = new Mtanalysis();
		try{
			Program p = g.parse(args);
			mt.analyse(p);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	private HashMap<AbsState,Interference> outInter;
	private HashMap<AbsState,Interference> inInter;
	public void analyse(Program p){
		List<ThreadLaunch> Ts = p.getEntryMethod().getThreads();
		HashMap<String,HashMap<Node, AbsState>> hashstates = new HashMap<String,HashMap<Node,AbsState>>();	
		while(!outInter.equals(inInter)){
			inInter = outInter;
			Ts.forEach((t)->{
				String label = t.getLabel();
				Node pc = t.getMethod().getControlFlowGraph().getInitNode();
				HashMap<Node, AbsState> states = hashstates.get(label); 
				
				if(states == null)
					states = new HashMap<Node, AbsState>();
				AnalyseThread(label,t.getMethod(),pc,states/*,p,states,inInter,outInter*/);
			});		
		}
	}
	public void AnalyseThread(String t, GlatMethod m, Node pc, HashMap<Node, AbsState> states){
		List<Transition> Trs = m.getControlFlowGraph().getOutTransitions(pc);
		Queue<GlatTransition> Q = new PriorityQueue<GlatTransition>();
		Trs.forEach((tr)-> Q.add((GlatTransition) tr));
		
		while(!Q.isEmpty()){
			GlatTransition tau = Q.poll();
			AbsState sigma2=null,sigma = states.get(tau.getSrcNode());
			boolean b;
			Interference I;
			if((boolean) tau.getPropValue("GlobalRead")){
				//?????? sigma2 = inInter.get(/*t,*/sigma);
				b = storeState(states,tau.getSrcNode(),sigma2);
				sigma = states.get(tau.getSrcNode());
			}
			//<sigma2,I> = exec(tau,sigma);
			//??outInter.put(/*t,*/sigma, I);
			b = storeState(states,tau.getTargetNode(),sigma2);
			if(b){
				m.getControlFlowGraph().getOutTransitions(tau.getTargetNode()).forEach((tr)-> Q.add((GlatTransition) tr));
			}
		}
		
		
	}
	private boolean storeState(HashMap<Node, AbsState> states, Node srcNode, AbsState sigma2) {
		AbsState s = states.get(srcNode);
		AbsState s3 = s.merge(sigma2);
		boolean b = !s3.equals(s);
		states.put(srcNode, s3);
		return b;
	}
	
}
