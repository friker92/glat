package mtanalysis.fixpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import glat.program.ControlFlowGraph;
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
import mtanalysis.exceptions.NoMainException;
import mtanalysis.interferences.Interference;
import mtanalysis.interferences.InterferenceSet;
import mtanalysis.stores.NodeAbstStateStore;
import mtanalysis.stores.Store;
import mtanalysis.strategies.IterationStrategy;
import mtanalysis.strategies.SimpleStrategy;

public class ThSimpleAnalysis extends SimpleAnalysis {

	private Fixpoint fx;

	public enum NameProp {
		STRATEGY, DOMAIN
	}

	// Prog, Domain, STrategy
	public ThSimpleAnalysis(Properties prop) throws Exception {
		super(prop);
	}
	
	@Override
	public void start(GlatProgram p) throws Exception {

		Method main = getMain(p);

		ControlFlowGraph main_cfg = main.getControlFlowGraph();
		List<Variable> vs = new ArrayList<Variable>(main.getVariables());
		vs.addAll(p.getGlobalVariables());

		AbstractState a = domain.defaultState(vs);

		// exec init transition
		Transition init = main_cfg.getOutTransitions(main.getInitNode()).get(0);
		AbstractState def_st = domain.exec(init, a);

		// launch "threads"
		Transition launch = main_cfg.getOutTransitions(init.getTargetNode()).get(0);
		
		Vector<ThreadInfo> vth = new Vector<ThreadInfo>();
		int count = 0;
		
		for (Instruction i : launch.getInstructions()) {
			Call c = (Call)i;
			Store<Node, AbstractState> st = prepareStore(p,c,def_st);
			ThreadInfo th = new ThreadInfo("th"+count,st,c);
			vth.add(th);
			count++;
		}

		fx = new ThSimpleFixpoint(p, vth, domain, getStrategy());
		fx.start();
		//result = fx.getResult();
	}
	
	@Override
	public Map<Object, Object> getResult() {
		System.out.println("Result: ");
		fx.getResult();
		
		return null;
	}
}
