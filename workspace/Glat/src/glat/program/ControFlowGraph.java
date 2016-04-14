package glat.program;

import java.util.List;

public interface ControFlowGraph extends PropTable {
	List<Node> getNodes();
	List<GlatTransition> getTransitions();
	Node getInitNode();
	List<GlatTransition> getOutTransitions(Node src);
	List<GlatTransition> getInTransitions(Node target);
}
