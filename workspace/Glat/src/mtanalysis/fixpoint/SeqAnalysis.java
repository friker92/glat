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

	private Store getStore() throws Exception {
		Class[] cArg = new Class[1];
		cArg[0] = AbstractDomain.class;
		return (Store) ((Class) properties.get(NameProp.STORE)).getDeclaredConstructor(cArg).newInstance(domain);
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
		System.out.println("init: " + def_st);
		// launch "threads"
		Transition launch = main_cfg.getOutTransitions(init.getTargetNode()).get(0);
		Fixpoint fx;
		Method m;
		
		Vector<Store> vstore = new Vector<Store>();
		for (Instruction i : launch.getInstructions()) {
			vstore.addElement(prepareStore(p,(Call)i,def_st,getStore()));
		}
		Iterator<Store> istore = vstore.iterator();
		Store sto;

		for (Instruction i : launch.getInstructions()) {
			switch (i.getType()) {
			case ASYNCCALL:
			case SYNCCALL:
				m = ((Call) i).getMethodRef();
				System.out.println("launch: " + m.getLabel());
				sto = istore.next();
				fx = new SeqFixpoint(p, (Call) i, sto, domain, getStrategy(m.getControlFlowGraph()));
				fx.start();
				result.put(i, fx.getResult());
				break;
			default:
				break;
			}
		}
	}

	private Store prepareStore(GlatProgram program, Call call, AbstractState default_state, Store table){
		Method m;

		m = call.getMethodRef();

		List<Variable> vs = new ArrayList<Variable>(m.getVariables());
		vs.addAll(m.getParameters());
		vs.addAll(program.getGlobalVariables());
		AbstractState bt = domain.bottom(vs);
		vs = new ArrayList<Variable>(call.getArgs());
		vs.addAll(program.getGlobalVariables());
		AbstractState def = domain.project(default_state, vs);// call.getArgs());
		def = domain.rename(def, call.getArgs(), m.getParameters());
		def = domain.extend(bt, def);

		for (Node n : m.getControlFlowGraph().getNodes()) {
			if (m.getInitNode().equals(n))
				table.set(n, def);
			else
				table.set(n, bt);
		}
		return table;
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
