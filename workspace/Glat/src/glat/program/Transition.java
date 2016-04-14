package glat.program;

import java.util.List;

public interface Transition extends PropTable {
	Node getSrcNode();
	Node getTargetNode();
	List<Instruction> getInstructions();
}
