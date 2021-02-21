package net.skds.skds_core.config;

import java.util.function.Function;

import net.minecraftforge.common.ForgeConfigSpec;
import net.skds.skds_core.SKDSCore;

public class Main {

    public final ForgeConfigSpec.BooleanValue value;
    public final ForgeConfigSpec.IntValue minBlockUpdates;

    // public final ForgeConfigSpec.ConfigValue<ArrayList<String>> ss;
    // private final ForgeConfigSpec.IntValue maxFluidLevel;

    public Main(ForgeConfigSpec.Builder innerBuilder) {
        Function<String, ForgeConfigSpec.Builder> builder = name -> innerBuilder .translation(SKDSCore.MOD_ID + ".config." + name);

        innerBuilder.push("General");

        value = builder.apply("value").comment("value").define("value", true);
        minBlockUpdates = builder.apply("minBlockUpdates").comment("Minimal block updates per tick").defineInRange("minBlockUpdates", 500, 0, 1_000_000);

        innerBuilder.pop();
    }
}