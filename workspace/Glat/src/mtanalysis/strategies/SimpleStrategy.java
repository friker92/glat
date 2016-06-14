package mtanalysis.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.KosarajuStrongConnectivityInspector;
import org.jgrapht.graph.DirectedSubgraph;

import glat.program.ControlFlowGraph;
import glat.program.Node;

public class SimpleStrategy implements IterationStrategy {
	protected Properties prop;
	protected List<IterationStrategy> lst;
	protected Node n;
	protected boolean leaf;
	public SimpleStrategy(ControlFlowGraph cfg){
		prop = new Properties();
		lst = new ArrayList<IterationStrategy>();
		buildList2(cfg);
	}
	
	public SimpleStrategy(Node e){
		n = e;
		prop = new Properties();
		lst = new ArrayList<IterationStrategy>();
		leaf = true;
	}
	
	public SimpleStrategy(Set<Node> vertexSet) {
		n = null;
		prop = new Properties();
		lst = new ArrayList<IterationStrategy>();
		vertexSet.forEach((v)->lst.add(new SimpleStrategy(v)));
		leaf = false;
	}

	private void buildList(ControlFlowGraph cfg) {
		if(cfg.getNodes().size()==1){
			n = cfg.getNodes().get(0);
			leaf = true;
		}else{
			n = null;
			for(Node e : cfg.getNodes()){
				lst.add(new SimpleStrategy(e));
			}
			leaf = false;
		}
	}
	
	private void buildList2(ControlFlowGraph cfg) {
		if(cfg.getNodes().size()==1){
			n = cfg.getNodes().get(0);
			leaf = true;
		}else{
			n = null;
			leaf = false;
			KosarajuStrongConnectivityInspector sci =
			           new KosarajuStrongConnectivityInspector( cfg.getGraph());
			       List<DirectedSubgraph> stronglyConnectedSubgraphs = sci.stronglyConnectedSubgraphs();
			for( DirectedSubgraph dsg : stronglyConnectedSubgraphs){
				lst.add(new SimpleStrategy(dsg.vertexSet()));
			}
		}
	}
	
	@Override
	public Properties getProp() {
		return prop;
	}
	@Override
	public List<IterationStrategy> getStrategy() {
		return lst;
	}

	@Override
	public boolean isLeaf() {
		return leaf;
	}

	@Override
	public Node getNode() {
		return n;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return lst.toString() + " node: "+ n;
	}
}
