package mtanalysis.fixpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import glat.program.ControlFlowGraph;
import glat.program.GlatProgram;
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

public class SeqAnalysis implements Analysis {

	private Properties properties;
	private AbstractDomain domain;
	Map<Object, Object> result;

	public enum NameProp {
		STRATEGY, DOMAIN
	}

	// Prog, Domain, STrategy
	public SeqAnalysis(Properties prop) throws Exception {
		properties = new Properties(defaultProperties());
		for (Object k : prop.keySet()) {
			properties.put(k, prop.get(k));
		}
		domain = getDomain();
		result = new HashMap<Object, Object>();
	}

	public static Properties defaultProperties() {
		Properties prop = new Properties();
		prop.put(NameProp.DOMAIN, IntervalsAbstDomain.class);
		return prop;
	}

	private AbstractDomain getDomain() throws Exception {
		return (AbstractDomain) ((Class) properties.get(NameProp.DOMAIN)).newInstance();
	}

	private Store<Node, AbstractState> getStore() {
		return new NodeAbstStateStore();
	}

	@Override
	public void start(GlatProgram p) throws Exception {
		/*
		 * assume: void main(){ start m; trans m -> n { //init variables }
		 * 
		 * trans n -> o { // call threads }
		 */

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

	private Store<Node, AbstractState> prepareStore(GlatProgram program, Call call, AbstractState stateAtCall) {

		Store<Node, AbstractState> store = getStore();
		Method m = call.getMethodRef();

		List<Variable> vs = new ArrayList<Variable>(m.getVariables());
		vs.addAll(m.getParameters());
		vs.addAll(program.getGlobalVariables());

		AbstractState bt = domain.bottom(vs);
		vs = new ArrayList<Variable>(call.getArgs());
		vs.addAll(program.getGlobalVariables());
		AbstractState def = domain.project(stateAtCall, vs);// call.getArgs());
		def = domain.rename(def, call.getArgs(), m.getParameters());

		// TODO extend should receive and abstract state and a list of variable,
		// and should extend the state with the new variables set to top

		def = domain.extend(bt, def);

		for (Node n : m.getControlFlowGraph().getNodes()) {
			if (m.getInitNode().equals(n))
				store.setValue(n, def);
			else
				store.setValue(n, bt);
		}
		return store;
	}

	private Method getMain(GlatProgram p) {
		// TODO check that 'main' follow the required syntax.

		for (Method m : p.getMethods()) {
			if (m.getName().equals("main") && m.getParameters().size() == 0) {
				return m;
			}
		}
		throw new NoMainException("No MAIN");
	}

	private IterationStrategy getStrategy() {
		return new SimpleStrategy();
	}

	@Override
	public Map<Object, Object> getResult() {
		return result;
	}
}
