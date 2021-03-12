package net.skds.core.multithreading;

import net.minecraftforge.common.MinecraftForge;
import net.skds.core.SKDSCoreConfig;
import net.skds.core.events.SyncTasksHookEvent;

public class MTHooks {
	public static int COUNTS = 0;
	public static int TIME = 0;

	public static void afterWorldsTick() {
		TIME = SKDSCoreConfig.COMMON.timeoutCutoff.get();
		COUNTS = SKDSCoreConfig.COMMON.minBlockUpdates.get();

		MinecraftForge.EVENT_BUS.post(new SyncTasksHookEvent());

		///ThreadProvider.doSyncFork(WWSGlobal::nextTask);
		try {
			ThreadProvider.waitForStop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}