package net.skds.skds_core.util.api;

import java.util.HashMap;
import java.util.Map;

public class CustomBlockPars {

	private final Map<Class<?>, Object> pars = new HashMap<>();

	public void put(Class<?> key, Object o) {
		pars.put(key, o);
	}

	public Object get(Class<?> key) {
		return pars.get(key);
	}
}