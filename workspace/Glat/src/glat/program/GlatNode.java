package glat.program;

public class GlatNode extends GlatClass implements Node {

	public GlatNode(String name) {
		this.name = name;
	}

	/*##############################
	 *        Access Methods       *
	 ##############################*/
	public String hash(){
		return name;
	}
	public boolean equals(Object object) {
	    if (object == null || !(object instanceof GlatNode)) {
	        return false;
	    }       
	    return (this.name.equals(((GlatNode) object).name));
	}   
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
		return getName();
	}
	
	/*##############################
	 *        Build Methods        *
	 ##############################*/
	
	/*##############################
	 *     Internal Attributes     *
	 ##############################*/
	
	private String name;
}
