package mtanalysis.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import glat.program.Node;
import glat.program.Transition;

public class StrategyNodeImp implements StrategyNode {

	protected Vector<StrategyNode> _l;
	protected boolean leaf;
	protected boolean widen;
	protected List<Transition> _t;
	protected Node n;
	private List<StrategyNode> _wp;

	public StrategyNodeImp() {
		leaf = false;
		widen = false;
		_l = new Vector<StrategyNode>();
		_wp = new ArrayList<StrategyNode>();
		n = null;
		_t = new ArrayList<Transition>();
	}

	public StrategyNodeImp(Node e, boolean isWidenNode) {
		_l = new Vector<StrategyNode>();
		n = e;
		leaf = true;
		widen = isWidenNode;
		_t = new ArrayList<Transition>();
		_wp = new ArrayList<StrategyNode>();
	}

	@Override
	public List<StrategyNode> getComponents() {
		return _l;
	}

	@Override
	public boolean isLeaf() {
		return leaf;
	}

	@Override
	public List<Transition> getInTransitions() {
		return _t;
	}

	@Override
	public Node getCFGNode() {
		return n;
	}

	@Override
	public boolean isWidenNode() {
		return widen;
	}

	@Override
	public List<StrategyNode> getWidenPoints() {
		return _wp;
	}

	public void addStrategyNode(StrategyNodeImp stn) {
		if (stn.isWidenNode())
			_wp.add(stn);
		_l.add(stn);
	}

	public void addTransition(Transition t) {
		_t.add(t);
	}

	public void setAllTransitions(List<Transition> inTransitions) {
		_t = inTransitions;
	}

	@Override
	public String toString() {
		if (isLeaf())
			return "" + n + (isWidenNode() ? " w" : "");// + " " + _t;
		return _l + "";
	}

}
