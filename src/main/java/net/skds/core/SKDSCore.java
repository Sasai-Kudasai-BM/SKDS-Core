package net.skds.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.skds.core.network.PacketHandler;
import net.skds.core.util.data.capability.CapabilityProvider;

@Mod(SKDSCore.MOD_ID)
public class SKDSCore {

    public static MinecraftServer SERVER = null;
    public static final String MOD_ID = "skds_core";
    public static final String MOD_NAME = "SKDS Core";
    // Directly reference a log4j logger.
	public static final Logger LOGGER = LogManager.getFormatterLogger(MOD_NAME);
	public static Events EVENTS = new Events();

	public SKDSCore() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

		MinecraftForge.EVENT_BUS.register(EVENTS);
		MinecraftForge.EVENT_BUS.register(this);
		SKDSCoreConfig.init();
		//RegisterDebug.register();		
	}

    private void setup(final FMLCommonSetupEvent event) {
		PacketHandler.init();
		//ChunkSectionAdditionalData.register(ExampleData::new, Side.BOTH);
		CapabilityProvider.init();
    }

	private void setupClient(final FMLClientSetupEvent event) {
		//RegisterDebugClient.register();
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
	}

    private void processIMC(final InterModProcessEvent event) {
    }
}
