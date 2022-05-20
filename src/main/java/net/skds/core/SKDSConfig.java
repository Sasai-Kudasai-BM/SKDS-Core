package net.skds.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.skds.core.config.IJsonConfigUnit;
import net.skds.core.config.JsonConveyor;
import net.skds.core.config.UniversalJsonReader;


public class SKDSConfig implements IJsonConfigUnit {

	private static final Map<Class<? extends IJsonConfigUnit>, IJsonConfigUnit> CONFIGS = new HashMap<>();

	private static CoreConfig inst;

	public static CoreConfig get() {
		if (inst == null) {
			throw new RuntimeException("Config is not loaded yet!");
		}
		return inst;
	}

	public static record CoreConfig(int minTasks, float timeoutCutoff) {}

	@Override
	public String getModID() {
		return SKDSCore.MOD_ID;
	}

	@Override
	public boolean updateOnReload() {
		return true;
	}

	@Override
	public String getName() {
		return "main";
	}

	@Override
	public boolean apply(JsonConveyor conveyor) {
		inst = new CoreConfig(conveyor.getInt("minTasks"), conveyor.getFloat("timeoutCutoff"));
		return true;
	}

	public static void register(IJsonConfigUnit jcu) {
		CONFIGS.put(jcu.getClass(), jcu);
	}

	public static int onReload() {
		int count = 0;
		for (var config : CONFIGS.values()) {
			if (config.updateOnReload()) {
				UniversalJsonReader.read(config);
			}
		}
		return count;
	}

	public static int loadConfigs() {
		int count = 0;
		for (var config : CONFIGS.values()) {
			UniversalJsonReader.read(config);
		}
		return count;
	}

	public static void init() {
		File dir = new File("config/" + SKDSCore.MOD_ID);
		dir.mkdir();
		loadConfigs();
	}
}