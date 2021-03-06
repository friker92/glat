package mtanalysis.fixpoint;

import java.util.Map;

import glat.program.GlatProgram;

public interface Analysis {
	public void start(GlatProgram p) throws Exception;

	public Map<Object, Object> getResult();
}
