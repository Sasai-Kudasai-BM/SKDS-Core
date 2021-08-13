package net.skds.core.multithreading;

import java.util.concurrent.locks.LockSupport;
import java.util.function.Function;

import net.skds.core.SKDSCore;
import net.skds.core.api.multithreading.ISKDSThread;
import net.skds.core.api.multithreading.ITaskRunnable;
import net.skds.core.util.Cycler;

public class UniversalWorkerThread extends Thread implements ISKDSThread {

	// private Supplier<ITaskRunnable> syncSup = (() -> null);

	private Cycler<Function<Integer, ITaskRunnable>> cycler = new Cycler<>();

	// private Supplier<ITaskRunnable> asyncSup = (() -> null);
	public boolean isDone = true;
	public boolean cont = true;
	public boolean yeld = true;
	public final int num;

	public UniversalWorkerThread(int num) {
		this.num = num;
		setDaemon(true);
		setName("SKDS-Worker-" + num);
		start();
	}

	public static UniversalWorkerThread[] create(int count) {
		UniversalWorkerThread[] arr = new UniversalWorkerThread[count];
		for (int n = 0; n < count; n++) {
			arr[n] = new UniversalWorkerThread(n);
		}
		return arr;
	}

	public void close() {
		cont = false;
		LockSupport.unpark(this);
	}

	@Override
	public void run() {
		while (cont) {
			isDone = true;
			Thread.yield();
			LockSupport.parkNanos("waiting for tasks", 100000L);
			isDone = false;

			// System.out.println("aa");
			if (cont && !yeld) {
				try {
					// System.out.println("x");

					takeTasksStack();
				} catch (Exception e) {
					SKDSCore.LOGGER.error("Exeption while taking task stack ", e);
				}
			}
		}
	}

	private boolean shouldContain() {
		return !yeld;
	}

	private ITaskRunnable pollTask() {
		Function<Integer, ITaskRunnable> func = cycler.next();
		if (func == null) {
			return null;
		}

		ITaskRunnable task = null;
		//task = func.apply(num);
		while (func == null || (task = func.apply(num)) == null) {

			//cycler.removeEntry(func);
			cycler.removeLast();
			if (cycler.isEmpty()) {
				return null;
			}
			func = cycler.next();
		}
		return task;
	}

	private void takeTasksStack() throws Exception {
		ITaskRunnable task;
		// System.out.println(shouldContain());
		while (shouldContain() && (task = pollTask()) != null) {
			try {
				task.run();
			} catch (Exception e) {
				SKDSCore.LOGGER.error("Exeption while executing task ", e);
			}
			task = null;
		}
		yeld = true;

	}

	public void forkSync(Function<Integer, ITaskRunnable> sup) {
		// syncSup = sup;
		cycler.addEntry(sup);
		yeld = false;
		isDone = false;
		LockSupport.unpark(this);
	}

	public boolean isDone() {
		//return isDone;
		return yeld;
	}

	@Override
	public int getIndex() {
		return num;
	}
}