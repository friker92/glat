package mtanalysis.stores;

import java.util.Set;

public interface Store<Key,Value> {

	public int setValue(Key key, Value value);
	public Value getValue(Key key);
	public int getCount(Key key);
	public int setCount(Key key, int count);
	public Set<Key> getKeys();

}
