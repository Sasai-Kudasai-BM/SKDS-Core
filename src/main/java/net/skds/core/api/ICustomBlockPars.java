package net.skds.core.api;

public interface ICustomBlockPars {
	public void put(Object o);
	public void clear(Class<?> c);
	public <T extends Object> T get(Class<T> key);
}