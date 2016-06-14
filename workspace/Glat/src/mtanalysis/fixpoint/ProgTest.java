package mtanalysis.fixpoint;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.jgrapht.alg.cycle.*;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DirectedSubgraph;
import org.jgrapht.graph.EdgeSetFactory;
import org.jgrapht.EdgeFactory;
import org.jgrapht.alg.*;

import glat.parser.Glat;
import glat.parser.ParseException;
import glat.program.ControlFlowGraph;
import glat.program.GlatNode;
import glat.program.GlatProgram;
import glat.program.GlatTransition;
import glat.program.Instruction;
import glat.program.Method;
import glat.program.Node;
import glat.program.Transition;
import glat.program.instructions.Call;
import glat.program.instructions.expressions.terminals.Variable;
import mtanalysis.domains.AbstractDomain;
import mtanalysis.domains.AbstractState;
import mtanalysis.domains.intervals.IntervalsAbstDomain;
import mtanalysis.stores.SimpleStore;
import mtanalysis.stores.Store;

public class ProgTest {

	private static String basePath = System.getProperty("user.home") + "/Systems/glat/workspace/Glat";

	public static void main(String[] args) throws FileNotFoundException, ParseException {
		Glat g = new Glat();
		GlatProgram p = g.parse(new String[] { basePath + "/examples/example4" });

		// analyse(p, new SignAbstDomain());
		// analyse(p, new ConstPropDomain());
		analyse(p, new IntervalsAbstDomain());

	}

	public static void analyse(GlatProgram p, AbstractDomain d) {
		/* assume:
		 * void main(){
		 * start m;
		 * trans m -> n {
		 * 	 //init variables
		 * }
		 * 
		 * trans n -> o {
		 * 	// call threads
		 * }
		 */
		
		Method main = null;
		for( Method m : p.getMethods()){
			if(m.getName().equals("main") && m.getParameters().size()==0){
				main = m;
			}
		}
		if (main == null){
			// TODO : Exception main does not exists
			throw new NullPointerException();
		}
		ControlFlowGraph main_cfg = main.getControlFlowGraph();
		List<Variable> vs = new ArrayList<Variable>(main.getVariables());
		vs.addAll(p.getGlobalVariables());
		
		AbstractState a = d.defaultState(vs);
		
		
		// exec init transition
		Transition init = main_cfg.getOutTransitions(main.getInitNode()).get(0);
		AbstractState def_st = d.exec(init,a);
		System.out.println("init: "+def_st);
		// launch "threads"
		Transition launch = main_cfg.getOutTransitions(init.getTargetNode()).get(0);

		Call c;
		Method m;
		ControlFlowGraph cfg;
		Store table;
		for(Instruction i : launch.getInstructions()){
			
			switch(i.getType()){
			case ASYNCCALL:
			case SYNCCALL:
				c = (Call)i;
				m = c.getMethodRef();
				System.out.println("launch: "+ m.getLabel());
				
				vs = new ArrayList<Variable>(m.getVariables());
				vs.addAll(m.getParameters());
				vs.addAll(p.getGlobalVariables());
				AbstractState bt = d.bottom(vs);
				AbstractState def = d.project(def_st,c.getArgs());
				def = d.rename(def, c.getArgs(), m.getParameters());
				def = d.extend(bt, def);
				
				cfg = m.getControlFlowGraph();
				table = new SimpleStore(d);
				
				for (Node n : cfg.getNodes()) {
					if(m.getInitNode().equals(n))
						table.set(n, def);
					else
						table.set(n, bt);
				}

				Queue<Node> q = new PriorityQueue<Node>(new Comparator<Node>() {
					@Override
					public int compare(Node o1, Node o2) {
						return 0;
					}
				});

				// fixpoint
				
				Connect.askConnectivity(cfg);

		        q.add(m.getInitNode());

				while (!q.isEmpty()) {

					Node n = q.poll();

					AbstractState currState = table.get(n);

					List<AbstractState> lst = new ArrayList<AbstractState>();

					AbstractState st = currState;
					for (Transition t : cfg.getInTransitions(n)) {
						st = d.exec(t, table.get(t.getSrcNode()));
						lst.add(st);
					}
					lst.add(currState);
					
					st = d.lub(lst);

					if (table.modify(n, st)) {
						cfg.getOutTransitions(n).forEach((t) -> q.add(t.getTargetNode()));
					}
					
					System.out.println(n + " ->\t" + table);
				}
				System.out.println("\t" + table);
			

			default:
				break;
			}
		}			
	}	
}
