package net.skds.core.api;

import com.google.gson.JsonObject;

public interface IJsonConfigUnit {
	String getPath();

	String getFormat();

	default String getFormatedName() {
		return getName() + "." + getFormat();
	}

	String getName();

	String getJarPath();

	boolean apply(JsonObject paramJsonObject);
}
