package mtanalysis.strategies;

import java.util.List;
import glat.program.Node;
import glat.program.Transition;

public interface StrategyNode {
	public boolean isLeaf();

	public boolean isWidenNode();
	
	public boolean isWriteGlobalNode();
	
	public boolean isImportantNode();

	public List<Transition> getInTransitions();

	public List<StrategyNode> getWidenPoints();
	
	public List<StrategyNode> getWriteGlobalPoints();
	
	public List<StrategyNode> getImportantPoints();

	public Node getCFGNode();

	public List<StrategyNode> getComponents();
}
