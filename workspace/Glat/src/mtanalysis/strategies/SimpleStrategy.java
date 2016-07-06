package mtanalysis.strategies;

import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.jgrapht.alg.KosarajuStrongConnectivityInspector;
import org.jgrapht.graph.DirectedSubgraph;

import glat.program.ControlFlowGraph;
import glat.program.Node;

public class SimpleStrategy implements IterationStrategy {
	protected IterationStrategy generator;
	protected Properties prop;
	
	public SimpleStrategy(){
		prop = new Properties();
	}
	
	@Override
	public StrategyNode getStrategy(ControlFlowGraph cfg) {
		StrategyNodeImp lst;
		if (cfg.getNodes().size() == 1) {
			lst = new StrategyNodeImp(cfg.getNodes().get(0), true);
			lst.setAllTransitions(cfg.getInTransitions(cfg.getNodes().get(0)));
		} else {
			StrategyNodeImp tmp, tmp2;
			lst = new StrategyNodeImp();
			KosarajuStrongConnectivityInspector sci = new KosarajuStrongConnectivityInspector(cfg.getGraph());
			List<DirectedSubgraph> stronglyConnectedSubgraphs = sci.stronglyConnectedSubgraphs();
			for (DirectedSubgraph dsg : stronglyConnectedSubgraphs) {
				Set<Node> s = dsg.vertexSet();
				if (s.size() > 0) {
					tmp = new StrategyNodeImp();

					for (Node ee : s) {
						tmp2 = new StrategyNodeImp(ee, true);
						tmp2.setAllTransitions(cfg.getInTransitions(ee));
						tmp.addStrategyNode(tmp2);
					}
					lst.addStrategyNode(tmp);
				}

			}

			for (Node e : cfg.getNodes()) {
				tmp = new StrategyNodeImp(e, true);
				tmp.setAllTransitions(cfg.getInTransitions(e));
				lst.addStrategyNode(tmp);
			}
		}
		return lst;
	}

	@Override
	public Properties getProperties() {
		return prop;
	}

}
