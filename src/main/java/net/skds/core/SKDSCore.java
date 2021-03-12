package net.skds.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.skds.core.network.PacketHandler;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("skds_core")
public class SKDSCore
{

    public static final String MOD_ID = "skds_core";
    public static final String MOD_NAME = "SKDS Core";
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public static Events EVENTS = new Events();

    public SKDSCore() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        MinecraftForge.EVENT_BUS.register(EVENTS);
        MinecraftForge.EVENT_BUS.register(this);

        SKDSCoreConfig.init();
        PacketHandler.init();
    }
    

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {        
    }

    private void processIMC(final InterModProcessEvent event) {
    }
}
