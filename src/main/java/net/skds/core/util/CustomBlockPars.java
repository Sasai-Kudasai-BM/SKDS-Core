package net.skds.core.util;

import java.util.HashMap;
import java.util.Map;

import net.skds.core.api.ICustomBlockPars;

public class CustomBlockPars implements ICustomBlockPars {

	private final Map<Class<?>, Object> pars = new HashMap<>(4);

	@Override
	public void put(Object o) {
		pars.put(o.getClass(), o);
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
	public void clear(Class<?> c) {
		pars.remove(c);
	}
}