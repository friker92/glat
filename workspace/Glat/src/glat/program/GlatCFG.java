package glat.program;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jgrapht.graph.DefaultDirectedGraph;//.DefaultDirectedWeightedGraph;

public class GlatCFG extends GlatClass implements ControlFlowGraph {

	public GlatCFG(String n){
		gh = new DefaultDirectedGraph<GlatNode, GlatTransition>(GlatTransition.class);
		entry = new GlatNode(n);
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
	public Node getNode(String name) {
		for(Iterator<GlatNode> it = gh.vertexSet().iterator();it.hasNext();){
			GlatNode n = it.next();
			if(n.getName().equals(name))
				return n;
		}
		// TODO : return GlatNode or null?
		return new GlatNode(name);
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
		List<Transition> l = new ArrayList<Transition>();
		gh.edgeSet().forEach((tr)->{
			if(tr.getSrcNode().equals(src))
				l.add(tr);
		});
		return l;//new ArrayList<Transition>(gh.outgoingEdgesOf((GlatNode)src));
	}

	@Override
	public List<Transition> getInTransitions(Node target) {
		List<Transition> l = new ArrayList<Transition>();
		gh.edgeSet().forEach((tr)->{
			if(tr.getTargetNode().equals(target))
				l.add(tr);
		});
		return l;//new ArrayList<Transition>(gh.incomingEdgesOf((GlatNode)target));
	}
	
	/*##############################
	 *        Build Methods        *
	 ##############################*/
	
	public void addTransition(GlatTransition tr){
		GlatNode src = (GlatNode)tr.getSrcNode();
		GlatNode trg = (GlatNode)tr.getTargetNode();
		gh.addVertex(src);
		gh.addVertex(trg);
		gh.addEdge(src,trg , tr);
	}

	/*##############################
	 *     Internal Attributes     *
	 ##############################*/
	
	private Node entry;
	private DefaultDirectedGraph<GlatNode, GlatTransition> gh;
	@Override
	public DefaultDirectedGraph<GlatNode, GlatTransition> getGraph() {
		return gh;
	}


}
