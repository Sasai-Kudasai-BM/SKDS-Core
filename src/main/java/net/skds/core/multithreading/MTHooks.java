package net.skds.core.multithreading;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.profiler.Profiler;
import net.skds.core.SKDSConfig;
import net.skds.core.util.blockupdate.WWSGlobal;

public class MTHooks {
	public static final AtomicInteger COUNTS = new AtomicInteger(0);
	public static volatile float TIME = 0;

	private static ParallelExecutor executor = new ParallelExecutor();

	public static void afterWorldsTick(Profiler profiler, MinecraftServer server) {		
        profiler.push("SKDS Hooks");

		WWSGlobal.tickPreMTH();
		
		TIME = SKDSConfig.get().timeoutCutoff();
		COUNTS.set(SKDSConfig.get().minTasks());

		//TODO run event

		try {
			//executor.execute(task);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WWSGlobal.tickPostMTH();
        profiler.pop();
	}
}