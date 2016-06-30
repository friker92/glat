package glat.program;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class GlatClass implements BasicInterface {
	private Properties prop;
		
	/*###################################
	 *        PropTable Methods
	 ###################################*/
	
	/**
	 * Get a property value
	 */
	@Override
	public Object getPropValue(Object name) {
		if(prop==null) prop = new Properties();
		return prop.get(name);
	}

	/**
	 * Set a property
	 * @return the previous value of the specified property or null
	 */
	@Override
	public Object setPropValue(Object name, Object value) {
		if(prop==null) prop = new Properties();
		return prop.put(name, value);
	}

	/**
	 * @return a List of the Properties contained
	 */
	@Override
	public List<Object> getPropNames() {
		if(prop==null) prop = new Properties();
		return new ArrayList<Object>(prop.keySet());
	}
	
	@Override
	public boolean hasProp(Object name){
		if(prop==null) prop = new Properties();
		return prop.containsKey(name);
	}
	
	

}
