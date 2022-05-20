package net.skds.core.config;

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

import net.skds.core.SKDSCore;

import static net.skds.core.SKDSCore.LOGGER;

public class UniversalJsonReader {


	public static boolean read(IJsonConfigUnit unit) {
		File pat = new File("config/" + unit.getModID());
		File configFile = new File(pat, unit.getPath());
		try {
			pat.mkdir();
			
			boolean exsists = configFile.exists();

			if (exsists && configFile.isDirectory()) {
				configFile.delete();
			}

			if (!exsists) {
				create(unit, configFile);
			}

			if (!interpret(unit, configFile)) {
				if (!exsists) {
					return false;
				}
				create(unit, configFile);
				if (!interpret(unit, configFile)) {
					return false;
				}
			}
			
		} catch (Exception e) {
			LOGGER.error("Config error! ", e);
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
			LOGGER.error("Empty or invalid config file! " + unit.getPath());
			inputStream.close();

			create(unit, reading);
			inputStream = new FileInputStream(reading);
			r = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			jsonReader = new JsonReader(r);

			jsonobject = GSON.getAdapter(JsonObject.class).read(jsonReader);
		}
		r.close();
		jsonReader.close();

		return unit.apply(new JsonConveyor(jsonobject));
	}

	private static boolean interpretResource(IJsonConfigUnit unit, InputStream inputStream) throws IOException {
		JsonObject jsonobject = new JsonObject();

		Reader r = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		JsonReader jsonReader = new JsonReader(r);
		Gson GSON = new Gson();
		try {
			jsonobject = GSON.getAdapter(JsonObject.class).read(jsonReader);
		} catch (IOException e) {
			LOGGER.error("Empty or invalid config data file! " + unit.getPath());	
			r.close();
			jsonReader.close();
			return false;	
		}
		r.close();
		jsonReader.close();

		return unit.apply(new JsonConveyor(jsonobject));
	}

	private static boolean create(IJsonConfigUnit unit, File toCreate) throws IOException {

		InputStream is = unit.getClass().getClassLoader().getResourceAsStream(SKDSCore.MOD_ID + "/configs/" + unit.getPath());
		BufferedInputStream bis = new BufferedInputStream(is);

		if (toCreate.exists()) {
			backup(toCreate);
		}
		Files.copy(bis, toCreate.toPath(), StandardCopyOption.REPLACE_EXISTING);
		is.close();
		return true;
	}

	private static boolean backup(File file) throws IOException {
		File backupFile = new File(file.toPath() + ".backup");
		int index = 0;
		while (backupFile.exists()) {
			index++;			
			backupFile = new File(file.toPath() + ".backup[" + index + "]");
		}
		LOGGER.warn("Creating config backup: " + backupFile.toString());
		Files.copy(file.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		return true;
	}
}
