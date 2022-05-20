package net.skds.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;

public class SKDSCore implements ModInitializer {

    public static MinecraftServer SERVER = null;
    public static final String MOD_ID = "skds-core";
    public static final String MOD_NAME = "SKDS Core";

	public static final Logger LOGGER = LogManager.getFormatterLogger(MOD_NAME);

	public SKDSCore() {	
		SKDSConfig.register(new SKDSConfig());
	}

	public static void main(String[] args) {
		//SKDSConfig.register(new SKDSConfig());
		//SKDSConfig.init();
		//System.out.println(SKDSConfig.get());
		
	}

	@Override
	public void onInitialize() {
		SKDSConfig.init();
		//PacketHandler.init();
		//ChunkSectionAdditionalData.register(ExampleData::new, Side.BOTH);
		//CapabilityProvider.init();
		//System.out.println(SKDSConfig.get());		
		//System.out.println(SKDSConfig.get().minTasks());		
		//System.out.println(SKDSConfig.get().timeoutCutoff());		
	}

	//private void setupClient() {
	//	//RegisterDebugClient.register();
	//}

}
