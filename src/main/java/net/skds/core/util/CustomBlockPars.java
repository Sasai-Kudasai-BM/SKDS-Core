package net.skds.core.util;

import net.skds.core.api.ICustomBlockPars;

public class CustomBlockPars implements ICustomBlockPars {

	private final Class2InstanceMap<Object> pars = new Class2InstanceMap<>();

	@Override
	public void put(Object o) {
		pars.put(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Object> T get(Class<T> key) {
		Object o = pars.get(key);
		if (o != null) {
			return (T) o;
		}
		return null;
	}

	@Override
	public <T extends Object> void clear(Class<T> c) {
		pars.remove(c);
	}
}