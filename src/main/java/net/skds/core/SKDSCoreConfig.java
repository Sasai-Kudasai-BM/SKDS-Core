package net.skds.core;

import java.io.File;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.skds.core.config.Main;

public class SKDSCoreConfig {

    public static final Main COMMON;
    private static final ForgeConfigSpec SPEC;


    public static final int MAX_FLUID_LEVEL = 8;

    static {
        Pair<Main, ForgeConfigSpec> cm = new ForgeConfigSpec.Builder().configure(Main::new);
        COMMON = cm.getLeft();
        SPEC = cm.getRight();
    }

    public static void init() {
        File dir = new File("config/" + SKDSCore.MOD_ID);        
		dir.mkdir();
        ModLoadingContext.get().registerConfig(Type.COMMON, SPEC, SKDSCore.MOD_ID + "/main.toml");
    }
}