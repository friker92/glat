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
		return getName();
	}
	
	/*##############################
	 *        Build Methods        *
	 ##############################*/
	
	/*##############################
	 *     Internal Attributes     *
	 ##############################*/
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GlatNode other = (GlatNode) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	private String name;
}
