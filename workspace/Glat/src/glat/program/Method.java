package glat.program;

import java.util.List;

import glat.program.instructions.Call;

public interface Method extends BasicInterface {
	public String getName();
	public String getReturnType();
	public List<Declaration> getParameters();
	public List<Declaration> getVariables();
	public List<Call> getCallPoints();
	public Node getInitNode();
	public ControlFlowGraph getControlFlowGraph();
}
