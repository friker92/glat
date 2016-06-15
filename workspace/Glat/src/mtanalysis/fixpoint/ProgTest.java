package mtanalysis.fixpoint;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.Queue;

import glat.parser.Glat;
import glat.parser.ParseException;
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
import mtanalysis.domains.constprop.ConstPropDomain;
import mtanalysis.domains.intervals.IntervalsAbstDomain;
import mtanalysis.domains.sign.SignAbstDomain;
import mtanalysis.exceptions.*;
import mtanalysis.stores.SimpleStore;
import mtanalysis.stores.Store;
import mtanalysis.strategies.IterationStrategy;
import mtanalysis.strategies.SimpleStrategy;

public class ProgTest {

	private static String basePath = System.getProperty("user.home") + "/Systems/glat/workspace/Glat";

	

	public static void main(String[] args) throws Exception {
		Glat g = new Glat();
		GlatProgram p = g.parse(new String[] { basePath + "/examples/example3" });
		Properties prop = new Properties();
		prop.put(NameProp.STORE, SimpleStore.class);
		prop.put(NameProp.STRATEGY, SimpleStrategy.class);
		prop.put(NameProp.DOMAIN, IntervalsAbstDomain.class);
		//prop.put(NameProp.DOMAIN, SignAbstDomain.class);
		//prop.put(NameProp.DOMAIN, ConstPropDomain.class);
		
		
		ProgTest fx = new ProgTest(prop);
		//fx.analyze(p, new SignAbstDomain());
		//fx.analyze(p, new ConstPropDomain());
		fx.analyze(p);//, new IntervalsAbstDomain());

	}


	//************************************************************************************
	//************************************************************************************
	//************************************************************************************
	private Properties properties;
	private AbstractDomain domain;
	private Store table;
	private enum NameProp{
		STORE,STRATEGY,DOMAIN
	}
	
	public ProgTest(Properties prop) throws Exception {	
		properties = new Properties(defaultProperties());
		for(Object k : prop.keySet()){
			properties.put(k, prop.get(k));
		}
		initialize();
	}
	public static Properties defaultProperties(){
		Properties prop = new Properties();
		prop.put(NameProp.STORE, SimpleStore.class);
		prop.put(NameProp.STRATEGY, SimpleStrategy.class);
		prop.put(NameProp.DOMAIN, IntervalsAbstDomain.class);
		return prop;
	}
	private void initialize() throws Exception {
		domain = (AbstractDomain) ((Class) properties.get(NameProp.DOMAIN)).newInstance();
		Class[] cArg = new Class[1]; //Our constructor has 3 arguments
		cArg[0] = AbstractDomain.class;
		table = (Store) ((Class) properties.get(NameProp.STORE)).getDeclaredConstructor(cArg).newInstance(domain);
	}

	public void analyze(GlatProgram p) {
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
		
		Method main = getMain(p);

		ControlFlowGraph main_cfg = main.getControlFlowGraph();
		List<Variable> vs = new ArrayList<Variable>(main.getVariables());
		vs.addAll(p.getGlobalVariables());
		
		AbstractState a = domain.defaultState(vs);
		
		
		// exec init transition
		Transition init = main_cfg.getOutTransitions(main.getInitNode()).get(0);
		AbstractState def_st = domain.exec(init,a);
		System.out.println("init: "+def_st);
		// launch "threads"
		Transition launch = main_cfg.getOutTransitions(init.getTargetNode()).get(0);


		for(Instruction i : launch.getInstructions()){
			switch(i.getType()){
			case ASYNCCALL:
			case SYNCCALL:
				handleCall(p,(Call)i,def_st);
				break;
			default:
				break;
			}
		}			
	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private IterationStrategy getStrategy(ControlFlowGraph cfg){
		Class[] cArg = new Class[1]; //Our constructor has 3 arguments
		cArg[0] = ControlFlowGraph.class;
		IterationStrategy strategy = null;
		try {
			strategy = (IterationStrategy) ((Class) properties.get(NameProp.STRATEGY)).getDeclaredConstructor(cArg).newInstance(cfg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(strategy);
		return strategy;
	}
	
	
	
	private Method getMain(GlatProgram p){
		for( Method m : p.getMethods()){
			if(m.getName().equals("main") && m.getParameters().size()==0){
				return m;
			}
		}
		throw new NoMainException("No MAIN");
	}
	
	private void handleCall(GlatProgram p, Call c, AbstractState def_st){
		Method m;

		m = c.getMethodRef();
		System.out.println("launch: "+ m.getLabel());
		
		List<Variable> vs = new ArrayList<Variable>(m.getVariables());
		vs.addAll(m.getParameters());
		vs.addAll(p.getGlobalVariables());
		AbstractState bt = domain.bottom(vs);
		vs = new ArrayList<Variable>(c.getArgs());
		vs.addAll(p.getGlobalVariables());
		AbstractState def = domain.project(def_st,vs);
		def = domain.rename(def, c.getArgs(), m.getParameters());
		def = domain.extend(bt, def);
		
		for (Node n : m.getControlFlowGraph().getNodes()) {
			if(m.getInitNode().equals(n))
				table.set(n, def);
			else
				table.set(n, bt);
		}
		
		//analyze_strategy(m.getControlFlowGraph(),getStrategy(m.getControlFlowGraph()));
		//System.out.println("\t" + table);
		fixpoint_old(m.getControlFlowGraph());
		
	}
	
	public boolean analyze_strategy(ControlFlowGraph cfg, IterationStrategy st ) {
		   if (st.isLeaf()) {
			  System.out.println(st.getNode() + " ->\t" + table);
		   	  return analyze(cfg,  st.getNode());
		   }
		   
		   // st is a list
		   boolean f = false;
		   boolean changed = true;
		   
		   while ( changed && st.getStrategy().size()>0) {
			 Iterator<IterationStrategy> l = st.getStrategy().iterator();
		     while ( l.hasNext() && changed )  {
		        changed = analyze_strategy( cfg, l.next() );
		        if ( changed ) f=true;     
		     }
		    }
		    
		    return f;
		   
		   
		}
	
	
	private boolean analyze(ControlFlowGraph cfg, Node n){
		AbstractState currState = table.get(n);

		List<AbstractState> lst = new ArrayList<AbstractState>();

		AbstractState st = currState;
		for (Transition t : cfg.getInTransitions(n)) {
			st = domain.exec(t, table.get(t.getSrcNode()));
			lst.add(st);
		}
		lst.add(currState);
		
		st = domain.lub(lst);
		
		return table.modify(n, st);
	}
	
	private void fixpoint_old(ControlFlowGraph cfg){
		Queue<Node> q = new PriorityQueue<Node>(new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				return 0;
			}
		});

		// fixpoint
		q.add(cfg.getInitNode());
		while (!q.isEmpty()) {
			Node n = q.poll();
			if (analyze(cfg,n)) {
				cfg.getOutTransitions(n).forEach((t) -> q.add(t.getTargetNode()));
			}
			
			System.out.println(n + " ->\t" + table);
		}
		System.out.println("\t" + table);
	}
	
	
}
