package mtanalysis.domains.product;

import java.util.ArrayList;
import java.util.List;
import glat.program.instructions.expressions.terminals.Variable;
import mtanalysis.domains.AbstractState;

public class ProductAbstractState implements AbstractState {

	private List<AbstractState> absStates;
	
	public ProductAbstractState(List<AbstractState> sts) {
		absStates = sts;
	}

	@Override
	public List<Variable> getVars() {
		List<Variable> set = new ArrayList<Variable>();
		for( AbstractState abs : absStates){
			for(Variable v :abs.getVars()){
				if(!set.contains(v))
					set.add(v);
			}
		}
		return set;
	}

	@Override
	public AbstractState copy() {
		List<AbstractState> sts = new ArrayList<AbstractState>();
		for(AbstractState st : absStates){
			sts.add(st.copy());
		}
		ProductAbstractState p = new ProductAbstractState(sts);
		return p;
	}

	@Override
	public String getDesc() {
		String str = "Product State : <";
		boolean first = true;
		for(AbstractState st : absStates){
			if(first){
				first = false;
				str += ", ";
			}
			str += st.getDesc();
		}
		str += ">";
		return str;
	}

	public AbstractState getAbstractState(int index) {
		return absStates.get(index);
	}



}
