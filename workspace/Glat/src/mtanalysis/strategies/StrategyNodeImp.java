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
	protected boolean writeGlobal;
	protected List<Transition> _t;
	protected Node n;
	private List<StrategyNode> _wp;
	private List<StrategyNode> _wgp;
	private List<StrategyNode> _ip;

	public StrategyNodeImp() {
		leaf = false;
		widen = false;
		writeGlobal = false;
		_l = new Vector<StrategyNode>();
		_wp = new ArrayList<StrategyNode>();
		_wgp = new ArrayList<StrategyNode>();
		_ip = null;
		n = null;
		_t = new ArrayList<Transition>();
	}

	public StrategyNodeImp(Node e, boolean isWidenNode) {
		_l = new Vector<StrategyNode>();
		n = e;
		leaf = true;
		widen = isWidenNode;
		writeGlobal = false;
		_t = new ArrayList<Transition>();
		_wp = new ArrayList<StrategyNode>();
		_wgp = new ArrayList<StrategyNode>();
		_ip = null;
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
		for(Transition t : _t){
			if((boolean)t.getPropValue("isWriteGlobal")){
				writeGlobal = true;
				break;
			}
		}
	}

	@Override
	public String toString() {
		if (isLeaf())
			return "" + n + (isWidenNode() ? " w" : "") +(isWriteGlobalNode() ? " wg" : "");// + " " + _t;
		return _l + "";
	}

	@Override
	public List<StrategyNode> getWriteGlobalPoints() {
		return _wgp;
	}

	@Override
	public List<StrategyNode> getImportantPoints() {
		if(_ip == null) calculateImportantPoints();
		return _ip;
	}

	private void calculateImportantPoints() {
		_ip = new ArrayList<StrategyNode>(_wp);
		for(StrategyNode stNode : _wgp){
			if(!_ip.contains(stNode))
				_ip.add(stNode);
		}
	}

	@Override
	public boolean isWriteGlobalNode() {
		return writeGlobal;
	}

	@Override
	public boolean isImportantNode() {
		return isWidenNode() || isWriteGlobalNode();
	}
	
}
