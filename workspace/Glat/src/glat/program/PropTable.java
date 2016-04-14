package glat.program;

import java.util.List;

public interface PropTable {
	Object getPropValue(Object name);
	Object setPropValue(Object name, Object value);
	List<Object> getPropNames();
}
