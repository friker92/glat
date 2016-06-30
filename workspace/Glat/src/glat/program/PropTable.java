package glat.program;

import java.util.List;

public interface PropTable {
	public boolean hasProp(Object name);
	public Object getPropValue(Object name);
	public Object setPropValue(Object name, Object value);
	public List<Object> getPropNames();
}
