package net.skds.core.util;

import java.util.HashMap;
import java.util.Map;

import net.skds.core.api.ICustomBlockPars;

public class CustomBlockPars implements ICustomBlockPars {

	private final Map<Class<?>, Object> pars = new HashMap<>();

	@Override
	public void put(Class<?> key, Object o) {
		pars.put(key, o);
	}

	@Override
	public Object get(Class<?> key) {
		return pars.get(key);
	}
}