package glat.program;

import java.util.List;

import org.jgrapht.graph.DefaultDirectedGraph;

public interface ControlFlowGraph extends BasicInterface {
	public List<Node> getNodes();
	public Node getNode(String name);
	public List<Transition> getTransitions();
	public Node getInitNode();
	public List<Transition> getOutTransitions(Node src);
	public List<Transition> getInTransitions(Node target);
	public DefaultDirectedGraph<GlatNode, GlatTransition> getGraph();
}
