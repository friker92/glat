package mtanalysis.strategies;

import java.util.Properties;

import glat.program.ControlFlowGraph;

public interface IterationStrategy {

	public Properties getProperties();

	public StrategyNode getStrategy(ControlFlowGraph cfg);

}
