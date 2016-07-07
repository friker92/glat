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

public abstract class SimpleAnalysis implements Analysis{
	
	protected Properties properties;
	protected AbstractDomain domain;
	protected Map<Object, Object> result;
	
	public enum NameProp {
		DOMAIN
	}
	
	public SimpleAnalysis(Properties prop) throws Exception {
		properties = new Properties(defaultProperties());
		for (Object k : prop.keySet()) {
			properties.put(k, prop.get(k));
		}
		domain = getDomain();
		result = new HashMap<Object, Object>();
	}
	
	protected Properties defaultProperties() {
		Properties prop = new Properties();
		prop.put(NameProp.DOMAIN, IntervalsAbstDomain.class);
		return prop;
	}
	
	protected Method getMain(GlatProgram p) {
		/*
		 * assume: 
		 * void main(){ 
		 * 
		 * start m; 
		 * trans m -> n { 
		 * INIT variables 
		 * }
		 * 
		 * trans n -> o {
		 * CALL threads 
		 * }
		 * 
		 * }
		 */
		for (Method m : p.getMethods()) {
			if (m.getName().equals("main") && m.getParameters().size() == 0) {
				ControlFlowGraph main_cfg = m.getControlFlowGraph();
				if(main_cfg.getOutTransitions(m.getInitNode()).size() != 1)
					throw new NoMainException("Main need ONE transition from "+m.getInitNode());
				Transition init = main_cfg.getOutTransitions(m.getInitNode()).get(0);
				if(main_cfg.getOutTransitions(init.getTargetNode()).size() != 1)
					throw new NoMainException("Main need ONE transition from "+init.getTargetNode());
				Transition launch = main_cfg.getOutTransitions(init.getTargetNode()).get(0);
				if(launch.getInstructions().size() > 0){
					for(Instruction i : launch.getInstructions()){
						if(!(i instanceof Call))
							throw new NoMainException("All instructions at the second transition have to be CALLs");
					}
				}else{
					throw new NoMainException("Main need at least one call at the second transition");
				}
				return m;
			}
		}
		throw new NoMainException("No MAIN");
	}
	
	protected Store<Node, AbstractState> getStore() {
		return new NodeAbstStateStore();
	}
	
	protected AbstractDomain getDomain() throws Exception {
		return (AbstractDomain) ((Class) properties.get(NameProp.DOMAIN)).newInstance();
	}
	
	protected IterationStrategy getStrategy() {
		return new SimpleStrategy();
	}
	
	protected Store<Node, AbstractState> prepareStore(GlatProgram program, Call call, AbstractState stateAtCall) {

		Store<Node, AbstractState> store = getStore();
		Method m = call.getMethodRef();

		List<Variable> vs = new ArrayList<Variable>(m.getVariables());
		vs.addAll(m.getParameters());
		vs.addAll(program.getGlobalVariables());

		AbstractState bt = domain.bottom(vs);
		vs = new ArrayList<Variable>(call.getArgs());
		vs.addAll(program.getGlobalVariables());
		AbstractState def = domain.project(stateAtCall, vs);
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
}
