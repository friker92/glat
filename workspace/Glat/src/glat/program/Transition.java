package glat.program;

import java.util.List;

public interface Transition extends BasicInterface {
	public Node getSrcNode();
	public Node getTargetNode();
	public List<Instruction> getInstructions();
	public Instruction getInstruction(int pos);
	public int getNumInstructions();
}
