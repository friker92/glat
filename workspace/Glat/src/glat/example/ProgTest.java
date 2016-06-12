package glat.example;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import glat.domains.AbstractDomain;
import glat.domains.AbstractState;
import glat.domains.constprop.ConstPropDomain;
import glat.domains.intervals.IntervalsAbstDomain;
import glat.domains.sign.SignAbstDomain;
import glat.parser.Glat;
import glat.parser.ParseException;
import glat.program.ControlFlowGraph;
import glat.program.GlatProgram;
import glat.program.Instruction;
import glat.program.Method;
import glat.program.Node;
import glat.program.Transition;
import glat.program.instructions.expressions.terminals.Variable;

public class ProgTest {

	private static String basePath = System.getProperty("user.home")+"/Systems/glat/workspace/Glat";

	public static void main(String[] args) throws FileNotFoundException, ParseException {
		Glat g = new Glat();
		GlatProgram p = g.parse(new String[] { basePath + "/examples/example2" });

	//analyse(p, new SignAbstDomain());
		analyse(p, new ConstPropDomain());
		//analyse(p, new IntervalsAbstDomain());

	}

	public static void analyse(GlatProgram p, AbstractDomain d) {

		
		Store table = new SimpleStore(d);
		
		Method m = p.getMethods().get(1);
		ControlFlowGraph cfg = m.getControlFlowGraph();
		List<Variable> vs = new ArrayList<Variable>(m.getVariables());
		vs.addAll(p.getGlobalVariables());

		AbstractState a = d.defaultState(vs);

		for (Node n : cfg.getNodes()) {
			table.set(n, a);
		}

		Queue<Node> q = new PriorityQueue<Node>(new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				return 0;
			}
		});
		
		// fixpoint
		
		q.add(m.getInitNode());

		while (!q.isEmpty()) {
			System.out.println(table);

			Node n = q.poll();
			AbstractState currState = table.get(n);

			for (Transition t : cfg.getOutTransitions(n)) {

				AbstractState st = currState;
				Node dest = t.getTargetNode();

				for (Instruction i : t.getInstructions()) {
					st = d.exec(i, st);
				}

				
				if ( table.modify(dest, st) ) {
					q.add(t.getTargetNode());
				}
			}
		}

		System.out.println(table);

	}

}
