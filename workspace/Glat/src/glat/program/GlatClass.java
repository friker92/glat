package glat.program;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GlatClass implements PropTable{
	private Properties prop;
	
	
	/*###################################
	 *        PropTable Methods
	 ###################################*/
	
	/**
	 * 
	 */
	@Override
	public Object getPropValue(Object name) {
		return prop.get(name);
	}

	/**
	 * 
	 */
	@Override
	public Object setPropValue(Object name, Object value) {
		return prop.put(name, value);
	}

	/**
	 * 
	 */
	@Override
	public List<Object> getPropNames() {
		return new ArrayList<Object>(prop.keySet());
	}

}
