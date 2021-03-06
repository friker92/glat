package glat.mainexample.direct;

import java.util.*;

import glat.mainexample.Analysis;
import glat.program.*;
import glat.program.instructions.expressions.TypeValue;
import glat.program.instructions.expressions.terminals.Variable;

public class ExampleAnalysis implements Analysis {

	public ExampleAnalysis(GlatProgram p) {
		this.p = p;
	}

	@Override
	public void print() {
		System.out.println("Your program have:");
		System.out.println("\tYou have declared the following types:");
		List<TypeValue> prim = p.getPrimitiveTypes();
		prim.forEach((s) -> System.out.println("\t\t- " + s));
		System.out.println("\tThere are a few global variables:");
		List<Variable> lv = p.getGlobalVariables();
		lv.forEach((v) -> System.out.println("\t\t" + v.getType() + ":" + v.getName()));
		List<Method> lm = p.getMethods();
		lm.forEach((m) -> print(m));
	}

	private void print(Method m) {
		List<Variable> args = m.getParameters();
		List<Variable> vars = m.getVariables();
		ControlFlowGraph cfg = m.getControlFlowGraph();
		System.out.println("\t GlatMethod " + m.getLabel() + "");
		System.out.println("\t\t Parameters:");
		args.forEach((v) -> System.out.println("\t\t\t" + v.getType() + ":" + v.getName()));
		System.out.println("\t\t Variables:");
		vars.forEach((v) -> System.out.println("\t\t\t" + v.getType() + ":" + v.getName()));
		System.out.println("\t\t Control Flow Graph:");
		List<Transition> st = cfg.getTransitions();
		st.forEach((t) -> print(t));
	}

	private void print(Transition tr) {
		List<Instruction> Tcode = tr.getInstructions();
		Node source = tr.getSrcNode();
		Node dest = tr.getTargetNode();
		System.out.println("\t\t\t" + source + " -> " + dest + " : {");
		for (Instruction i : Tcode) {
			print(i);
		}
		System.out.println("\t\t\t}");
	}

	private void print(Instruction i) {
		switch (i.getType()) {
		case ASSIGNMENT:
		case ASSERT:
		case ASSUME:
		case SYNCCALL:
		case ASYNCCALL:
		case UNLOCK:
		case LOCK:
		case JOIN:
		case RETURN:
			System.out.println("\t\t\t\t" + i.toString());
			break;
		}
	}

	@Override
	public boolean run() {
		return !(p == null);
	}

	private GlatProgram p;

	@Override
	public void title() {
		System.out.println("Structure Analysis.");
	}
}
