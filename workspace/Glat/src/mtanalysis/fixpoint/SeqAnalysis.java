package mtanalysis.fixpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import glat.program.ControlFlowGraph;
import glat.program.GlatProgram;
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
import mtanalysis.stores.NodeAbstStateStore;
import mtanalysis.stores.Store;
import mtanalysis.strategies.IterationStrategy;
import mtanalysis.strategies.SimpleStrategy;

public class SeqAnalysis extends SimpleAnalysis {

	// Prog, Domain, STrategy
	public SeqAnalysis(Properties prop) throws Exception {
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
		Call i = (Call) launch.getInstruction(0);

		Method m = i.getMethodRef();

		Store<Node, AbstractState> store = prepareStore(p, i, def_st);

		Fixpoint fx = new SeqFixpoint(p, m.getControlFlowGraph(), store, domain, getStrategy());
		fx.start();
		result.put(i, fx.getResult());
	}

	@Override
	public Map<Object, Object> getResult() {
		return result;
	}
}
