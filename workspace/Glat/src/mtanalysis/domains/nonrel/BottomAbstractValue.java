package mtanalysis.domains.nonrel;

public class BottomAbstractValue implements AbstractValue {

	private static BottomAbstractValue instance = null;

	protected BottomAbstractValue() {
	}

	@Override
	public String getDesc() {
		return "Bottom Value";
	}

	@Override
	public AbstractValue lub(AbstractValue a) {
		return a;
	}

	@Override
	public AbstractValue glb(AbstractValue a) {
		return this;
	}

	@Override
	public AbstractValue widen(AbstractValue a) {
		return a;
	}

	@Override
	public boolean lte(AbstractValue a) {
		return true;
	}

	public static BottomAbstractValue getInstance() {
		if (instance == null) {
			instance = new BottomAbstractValue();
		}
		return instance;
	}

}
