package net.skds.core.multithreading;

import java.util.function.Function;

import net.skds.core.api.multithreading.ITaskRunnable;

public class ThreadProvider {

	public static int PROCESSORS = Runtime.getRuntime().availableProcessors();
	public static UniversalWorkerThread[] THREADS = UniversalWorkerThread.create(PROCESSORS);

	public static void doSyncFork(Function<Integer, ITaskRunnable> sup) {

		for (UniversalWorkerThread t : THREADS) {
			t.forkSync(sup);
		}
	}

	public static void waitForStop() throws InterruptedException {
		while (!isDone()) {
			Thread.yield();
			//LockSupport.parkNanos("waiting for tasks", 10000L);
		}

	}

	public static boolean isDone() {
		for (UniversalWorkerThread t : THREADS) {
			if (!t.isDone()) {
				return false;
			}
		}
		return true;
	}
}