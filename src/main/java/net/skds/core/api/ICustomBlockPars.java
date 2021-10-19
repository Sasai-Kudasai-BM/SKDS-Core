package net.skds.core.api;

public interface ICustomBlockPars {
	public void put(Object o);
	public <T extends Object> void clear(Class<T> c);
	public <T extends Object> T get(Class<T> key);
}