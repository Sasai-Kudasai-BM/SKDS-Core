package net.skds.core.config;

import java.util.function.Function;

import net.minecraftforge.common.ForgeConfigSpec;
import net.skds.core.SKDSCore;

public class Main {

    public final ForgeConfigSpec.IntValue minBlockUpdates, timeoutCutoff;

    // public final ForgeConfigSpec.ConfigValue<ArrayList<String>> ss;
    // private final ForgeConfigSpec.IntValue maxFluidLevel;

    public Main(ForgeConfigSpec.Builder innerBuilder) {
        Function<String, ForgeConfigSpec.Builder> builder = name -> innerBuilder .translation(SKDSCore.MOD_ID + ".config." + name);

        innerBuilder.push("General");

        timeoutCutoff = builder.apply("timeout").comment("Time befor tick's end to stop synchronized tasks (ms)").defineInRange("timeout", 4, 0, 50);
        minBlockUpdates = builder.apply("minBlockUpdates").comment("Minimal block updates per tick").defineInRange("minBlockUpdates", 500, 0, 1_000_000);

        innerBuilder.pop();
    }
}