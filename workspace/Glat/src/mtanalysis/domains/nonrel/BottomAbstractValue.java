package mtanalysis.domains.nonrel;

public class BottomAbstractValue implements AbstractValue {

	@Override
	public String getDesc() {
		return "Bottom Value";
	}

	@Override
	public AbstractValue lub(AbstractValue a) {
		return a;
	}

	@Override
	public AbstractValue widen(AbstractValue a) {
		return a;
	}

	@Override
	public boolean lte(AbstractValue a) {
		return true;
	}

}
