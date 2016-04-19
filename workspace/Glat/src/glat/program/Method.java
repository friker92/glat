package glat.program;

import java.util.List;

import glat.program.instructions.Call;
import glat.program.instructions.expressions.terminals.Variable;

public interface Method extends BasicInterface {
	public String getName();
	public String getReturnType();
	public List<Variable> getParameters();
	public List<Variable> getVariables();
	public List<Call> getCallPoints();
	public Node getInitNode();
	public ControlFlowGraph getControlFlowGraph();
}
