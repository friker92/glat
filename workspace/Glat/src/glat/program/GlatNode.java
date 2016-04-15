package glat.program;

public class GlatNode extends GlatClass implements Node {

	public GlatNode(String name) {
		this.name = name;
	}

	@Override
	public String getLabel() {
		return "node:"+name;
	}

	@Override
	public String getName() {
		return name;
	}

	private String name;
}
