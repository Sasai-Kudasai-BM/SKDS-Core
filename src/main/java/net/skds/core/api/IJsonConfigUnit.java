package net.skds.core.api;

import com.google.gson.JsonObject;

public interface IJsonConfigUnit {
	public String getPath();
	public String getFormat();
	public String getName();
	public String getJarPath();
	public default String getFormatedName() {
		return getName() + "." + getFormat();
	}
	public boolean apply(JsonObject jo);
}
