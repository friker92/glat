package glat.program;

import java.util.List;

public interface Transition extends BasicInterface {
	Node getSrcNode();
	Node getTargetNode();
	List<Instruction> getInstructions();
}
