package glat.program;

import java.util.List;

import glat.program.instructions.expressions.terminals.Variable;

public interface Program extends BasicInterface {
	public List<Method> getMethods();
	public List<String> getPrimitiveTypes();	
	public List<Variable> getGlobalVariables();
	public Variable getGlobalVariable(String v);
	public MainMethod getEntryMethod();
}
