package net.skds.core.multithreading;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.profiler.IProfiler;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.skds.core.SKDSCoreConfig;
import net.skds.core.events.SyncTasksHookEvent;
import net.skds.core.util.blockupdate.WWSGlobal;

public class MTHooks {
	public static final AtomicInteger COUNTS = new AtomicInteger(0);
	public static volatile int TIME = 0;

	public static void afterWorldsTick(IProfiler profiler, MinecraftServer server) {		
        profiler.startSection("SKDS Hooks");

		//WWSGlobal.tickPreMTH();
		
		TIME = SKDSCoreConfig.COMMON.timeoutCutoff.get();
		COUNTS.set(SKDSCoreConfig.COMMON.minBlockUpdates.get());

		MinecraftForge.EVENT_BUS.post(new SyncTasksHookEvent(profiler));

		try {
			ThreadProvider.waitForStop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		WWSGlobal.tickPostMTH();
        profiler.endSection();
	}
}