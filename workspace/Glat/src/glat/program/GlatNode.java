package glat.program;

public class GlatNode extends GlatClass implements Node {

	public GlatNode(String name) {
		this.name = name;
	}

	/*##############################
	 *        Access Methods       *
	 ##############################*/
	
	@Override
	public String getLabel() {
		return "node:"+name;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String toString(){
		return getLabel();
	}
	
	/*##############################
	 *        Build Methods        *
	 ##############################*/
	
	/*##############################
	 *     Internal Attributes     *
	 ##############################*/
	
	private String name;
}
