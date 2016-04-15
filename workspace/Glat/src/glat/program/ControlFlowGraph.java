package glat.program;

import java.util.List;

import org.jgrapht.WeightedGraph;

public interface ControlFlowGraph extends BasicInterface {
	List<Node> getNodes();
	List<Transition> getTransitions();
	Node getInitNode();
	List<Transition> getOutTransitions(Node src);
	List<Transition> getInTransitions(Node target);
}
