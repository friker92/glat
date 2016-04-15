package glat.program;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.graph.SimpleDirectedGraph;//.DefaultDirectedWeightedGraph;

public class GlatCFG extends GlatClass implements ControlFlowGraph {

	public GlatCFG(Node n){
		gh = new SimpleDirectedGraph<GlatNode, GlatTransition>(GlatTransition.class);
		entry = n;
	}

	/*##############################
	 *        Access Methods       *
	 ##############################*/	
	
	@Override
	public String getLabel() {
		return "cfg";
	}

	@Override
	public List<Node> getNodes() {	
		return new ArrayList<Node>(gh.vertexSet());
	}

	@Override
	public List<Transition> getTransitions() {
		return new ArrayList<Transition>(gh.edgeSet());
	}

	@Override
	public Node getInitNode() {
		return entry;
	}

	@Override
	public List<Transition> getOutTransitions(Node src) {
		return new ArrayList<Transition>(gh.outgoingEdgesOf((GlatNode)src));
	}

	@Override
	public List<Transition> getInTransitions(Node target) {
		return new ArrayList<Transition>(gh.incomingEdgesOf((GlatNode)target));
	}
	
	private Node entry;
	private SimpleDirectedGraph<GlatNode, GlatTransition> gh;


}
