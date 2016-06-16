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
		widen = false;
		_l = new Vector<StrategyNode>();
		_wp = new ArrayList<StrategyNode>();
		n = null;
		_t = new ArrayList<Transition>();
	}

	public List<StrategyNode> getStrategy() {
		return _l;
	}

	public void addStrategyNode(StrategyNode stn) {
		if (stn.isWiden())
			_wp.add(stn);
		_l.add(stn);
	}

	// *************************************
	// LEAF

	protected boolean leaf;
	protected boolean widen;
	protected List<Transition> _t;
	protected Node n;
	private List<StrategyNode> _wp;

	public StrategyNode(Node e, boolean w) {
		_l = new Vector<StrategyNode>();
		n = e;
		leaf = true;
		widen = w;
		_t = new ArrayList<Transition>();
		_wp = new ArrayList<StrategyNode>();
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
			return "" + n + (isWiden() ? " w" : "");// + " " + _t;
		return _l + "";
	}

	public boolean isWiden() {
		return widen;
	}

	public List<StrategyNode> getWidenPoints() {
		return _wp;
	}
}
