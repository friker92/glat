package glat.program;

import java.util.List;

public interface PropTable {
	public Object getPropValue(Object name);//{return null;}
	public Object setPropValue(Object name, Object value);//{return null;}
	public List<Object> getPropNames();//{return null;}
}
