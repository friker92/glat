package mtanalysis.strategies;

import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.jgrapht.alg.KosarajuStrongConnectivityInspector;
import org.jgrapht.graph.DirectedSubgraph;

import glat.program.ControlFlowGraph;
import glat.program.Node;

public class SimpleStrategy implements IterationStrategy {
	protected Properties prop;
	protected StrategyNode lst;

	public SimpleStrategy(ControlFlowGraph cfg) {
		prop = new Properties();
		buildList2(cfg);
	}

	private void buildList(ControlFlowGraph cfg) {
		if (cfg.getNodes().size() == 1) {
			lst = new StrategyNode(cfg.getNodes().get(0));
			lst.setAllTransitions(cfg.getInTransitions(cfg.getNodes().get(0)));
		} else {
			StrategyNode tmp;
			lst = new StrategyNode();
			for (Node e : cfg.getNodes()) {
				tmp = new StrategyNode(e);
				tmp.setAllTransitions(cfg.getInTransitions(e));
				lst.addStrategyNode(tmp);
			}
		}
	}

	private void buildList2(ControlFlowGraph cfg) {
		if (cfg.getNodes().size() == 1) {
			lst = new StrategyNode(cfg.getNodes().get(0));
			lst.setAllTransitions(cfg.getInTransitions(cfg.getNodes().get(0)));
		} else {
			StrategyNode tmp,tmp2;
			lst = new StrategyNode();
			KosarajuStrongConnectivityInspector sci = new KosarajuStrongConnectivityInspector(cfg.getGraph());
			List<DirectedSubgraph> stronglyConnectedSubgraphs = sci.stronglyConnectedSubgraphs();
			for (DirectedSubgraph dsg : stronglyConnectedSubgraphs) {
				Set<Node> s = dsg.vertexSet();
				if(s.size()>0){
					tmp = new StrategyNode();
					
					for(Node ee : s){
						tmp2 = new StrategyNode(ee);
						tmp2.setAllTransitions(cfg.getInTransitions(ee));
						tmp.addStrategyNode(tmp2);
					}
					lst.addStrategyNode(tmp);
				}
				
			}

			for (Node e : cfg.getNodes()) {
				tmp = new StrategyNode(e);
				tmp.setAllTransitions(cfg.getInTransitions(e));
				lst.addStrategyNode(tmp);
			}
		}
	}

	@Override
	public Properties getProp() {
		return prop;
	}

	@Override
	public StrategyNode getStrategy() {
		return lst;
	}

	@Override
	public String toString() {
		return lst.toString();
	}
}
