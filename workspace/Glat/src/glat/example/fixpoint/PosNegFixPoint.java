package glat.example.fixpoint;

import glat.example.Analysis;
import glat.fixpoint.FixPoint;
import glat.program.GlatProgram;

public class PosNegFixPoint implements Analysis{

	public PosNegFixPoint(GlatProgram p) {
		this.p = p;
	}
	
	@Override
	public boolean run() {
		fxp = new FixPoint(new PosNegDomain(),p);
		fxp.fixPoint();
		return true;
	}

	@Override
	public void print() {
		System.out.println(fxp.print());
	}
	@Override
	public void title() {
		System.out.println("FIXPOINT use example: Positive Negative Analysis.");
	}
	private FixPoint fxp;
	private GlatProgram p;
}
