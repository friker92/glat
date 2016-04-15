package glat.program;

import glat.program.instructions.TypeInst;

public interface Instruction extends BasicInterface {
	public TypeInst getType();
}
