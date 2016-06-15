package mtanalysis.strategies;

import java.util.Properties;

public interface IterationStrategy {
	// public abstract IterationStrategy(ControlFlowGraph cfg);

	public Properties getProp();

	public StrategyNode getStrategy();

}
