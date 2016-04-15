package glat.program;

import java.util.List;

public interface Program extends BasicInterface {
	public List<Method> getMethods();
	public List<String> getPrimitiveTypes();	
	public List<Declaration> getGlobalVariables();
	public MainMethod getEntryMethod();
}
