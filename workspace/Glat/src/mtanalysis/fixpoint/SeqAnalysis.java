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
import glat.program.Transition;
import glat.program.instructions.Call;
import glat.program.instructions.expressions.terminals.Variable;
import mtanalysis.domains.AbstractDomain;
import mtanalysis.domains.AbstractState;
import mtanalysis.domains.intervals.IntervalsAbstDomain;
import mtanalysis.exceptions.NoMainException;
import mtanalysis.stores.SimpleStore;
import mtanalysis.stores.Store;
import mtanalysis.strategies.IterationStrategy;
import mtanalysis.strategies.SimpleStrategy;

public class SeqAnalysis implements Analysis {

	private Properties properties;
	private AbstractDomain domain;
	Map<Object, Object> result;

	public enum NameProp {
		STORE, STRATEGY, DOMAIN
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
		prop.put(NameProp.STORE, SimpleStore.class);
		prop.put(NameProp.STRATEGY, SimpleStrategy.class);
		prop.put(NameProp.DOMAIN, IntervalsAbstDomain.class);
		return prop;
	}

	private AbstractDomain getDomain() throws Exception {
		return (AbstractDomain) ((Class) properties.get(NameProp.DOMAIN)).newInstance();
	}
	
	private Store getStore() throws Exception{
		Class[] cArg = new Class[1];
		cArg[0] = AbstractDomain.class;
		return (Store) ((Class) properties.get(NameProp.STORE)).getDeclaredConstructor(cArg).newInstance(domain);
	}

	@Override
	public void start(GlatProgram p) throws Exception{
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
		System.out.println("init: " + def_st);
		// launch "threads"
		Transition launch = main_cfg.getOutTransitions(init.getTargetNode()).get(0);
		Fixpoint fx;
		Method m;

		for (Instruction i : launch.getInstructions()) {
			switch (i.getType()) {
			case ASYNCCALL:
			case SYNCCALL:
				m = ((Call) i).getMethodRef();
				System.out.println("launch: " + m.getLabel());
				fx = new SeqFixpoint(p, (Call) i, getStore(), def_st, domain, getStrategy(m.getControlFlowGraph()));
				fx.start();
				result.put(i, fx.getResult());
				break;
			default:
				break;
			}
		}
	}

	private Method getMain(GlatProgram p) {
		for (Method m : p.getMethods()) {
			if (m.getName().equals("main") && m.getParameters().size() == 0) {
				return m;
			}
		}
		throw new NoMainException("No MAIN");
	}

	private IterationStrategy getStrategy(ControlFlowGraph cfg) {
		Class[] cArg = new Class[1];
		cArg[0] = ControlFlowGraph.class;
		IterationStrategy strategy = null;
		try {
			strategy = (IterationStrategy) ((Class) properties.get(NameProp.STRATEGY)).getDeclaredConstructor(cArg)
					.newInstance(cfg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("strategy: " + strategy);
		return strategy;
	}

	@Override
	public Map<Object, Object> getResult() {
		return result;
	}
}
