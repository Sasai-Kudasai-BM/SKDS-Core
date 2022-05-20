package net.skds.core.config;

public interface IJsonConfigUnit {
	public boolean updateOnReload();
	public String getName();

	public String getModID();

	public boolean apply(JsonConveyor conveyor);

	public default String getFormat() {
		return "json";
	}
	public default String getPath() {
		return getName() + "." + getFormat();
	}
}
