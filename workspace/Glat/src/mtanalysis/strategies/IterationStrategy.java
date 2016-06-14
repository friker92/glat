package mtanalysis.strategies;

import java.io.ObjectInputStream.GetField;
import java.util.List;
import java.util.Properties;

import glat.program.ControlFlowGraph;
import glat.program.Node;

public interface IterationStrategy {
	//public abstract IterationStrategy(ControlFlowGraph cfg);
	
	public Properties getProp();
	
	public List<IterationStrategy> getStrategy();

	public boolean isLeaf();

	public Node getNode();
}
