package mtanalysis.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import glat.program.Node;
import glat.program.Transition;

public class StrategyNode {

	// **********************************
	// List

	protected Vector<StrategyNode> _l;

	public StrategyNode() {
		leaf = false;
		_l = new Vector<StrategyNode>();
		n = null;
		_t = new ArrayList<Transition>();
	}

	public List<StrategyNode> getStrategy() {
		return _l;
	}

	public void addStrategyNode(StrategyNode stn) {
		_l.add(stn);
	}

	// *************************************
	// LEAF

	protected boolean leaf;
	protected List<Transition> _t;
	protected Node n;

	public StrategyNode(Node e) {
		_l = new Vector<StrategyNode>();
		n = e;
		leaf = true;
		_t = new ArrayList<Transition>();
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void addTransition(Transition t) {
		_t.add(t);
	}

	public List<Transition> getInTransitionOrdered() {
		return _t;
	}

	public Node getNode() {
		return n;
	}

	public void setAllTransitions(List<Transition> inTransitions) {
		_t = inTransitions;
	}

	@Override
	public String toString() {
		if (isLeaf())
			return n + " " + _t.toString();
		return _l.toString();
	}
}
