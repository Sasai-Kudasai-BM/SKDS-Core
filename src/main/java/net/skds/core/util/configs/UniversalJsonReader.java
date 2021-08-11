package net.skds.core.util.configs;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import net.minecraft.resources.DataPackRegistries;
import net.skds.core.api.IJsonConfigUnit;
import static net.skds.core.SKDSCore.LOGGER;

public class UniversalJsonReader {

	public static DataPackRegistries DATA_PACK_RREGISTRIES = null;

	public DataPackRegistries getDPR() {
		return DATA_PACK_RREGISTRIES;
	}

	public static boolean read(IJsonConfigUnit unit) {
		File configDir = new File(unit.getPath());
		try {
			configDir.mkdirs();
			
			File readFile = new File(configDir, unit.getFormatedName());
			boolean exsists = readFile.exists();
			if (!exsists) {
				create(unit);
			}

			if (!interpret(unit, readFile)) {
				if (!exsists) {
					return false;
				}
				create(unit);
				if (!interpret(unit, readFile)) {
					return false;
				}
			}
			
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean readResource(IJsonConfigUnit unit, InputStream is) {
		try {
			
			if (!interpretResource(unit, is)) {
				return false;
			}
			
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private static boolean interpret(IJsonConfigUnit unit, File reading) throws IOException {
		JsonObject jsonobject = new JsonObject();

		InputStream inputStream = new FileInputStream(reading);
		Reader r = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		JsonReader jsonReader = new JsonReader(r);
		Gson GSON = new Gson();
		try {
			jsonobject = GSON.getAdapter(JsonObject.class).read(jsonReader);
		} catch (IOException e) {
			LOGGER.error("Empty or invalid config file! " + unit.getFormatedName());
			inputStream.close();

			create(unit);
			inputStream = new FileInputStream(reading);
			r = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			jsonReader = new JsonReader(r);

			jsonobject = GSON.getAdapter(JsonObject.class).read(jsonReader);
		}
		r.close();
		jsonReader.close();

		return unit.apply(jsonobject);
	}

	private static boolean interpretResource(IJsonConfigUnit unit, InputStream inputStream) throws IOException {
		JsonObject jsonobject = new JsonObject();

		Reader r = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		JsonReader jsonReader = new JsonReader(r);
		Gson GSON = new Gson();
		try {
			jsonobject = GSON.getAdapter(JsonObject.class).read(jsonReader);
		} catch (IOException e) {
			LOGGER.error("Empty or invalid config data file! " + unit.getFormatedName());	
			r.close();
			jsonReader.close();
			return false;	
		}
		r.close();
		jsonReader.close();

		return unit.apply(jsonobject);
	}

	private static boolean create(IJsonConfigUnit unit) throws IOException {
		BufferedInputStream is = new BufferedInputStream(unit.getClass().getClassLoader().getResourceAsStream(unit.getJarPath() + "/" + unit.getFormatedName()));

		File configFile = new File(unit.getPath(), unit.getFormatedName());
		if (configFile.exists()) {
			backup(unit);
		}
		Files.copy(is, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		return true;
	}

	private static boolean backup(IJsonConfigUnit unit) throws IOException {
		File configFile = new File(unit.getPath(), unit.getFormatedName());
		File backupFile = new File(unit.getPath(), unit.getName() + "-backup." + unit.getFormat());
		int index = 0;
		while (backupFile.exists()) {
			index++;			
			backupFile = new File(unit.getPath(), unit.getName() + "-backup (" + index + ") ." + unit.getFormat());
		}
		LOGGER.error("Creating config backup" + unit.getFormatedName());
		Files.copy(configFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		return true;
	}
}
