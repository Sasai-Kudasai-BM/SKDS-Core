package net.skds.core.multithreading;

import java.util.concurrent.locks.LockSupport;
import java.util.function.Function;

import net.skds.core.SKDSCore;
import net.skds.core.api.multithreading.ISKDSThread;
import net.skds.core.api.multithreading.ITaskRunnable;
import net.skds.core.util.Cycler;

public class UniversalWorkerThread extends Thread implements ISKDSThread {

	//private Supplier<ITaskRunnable> syncSup = (() -> null);

	private Cycler<Function<Integer, ITaskRunnable>> cycler = new Cycler<>();

	//private Supplier<ITaskRunnable> asyncSup = (() -> null);
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
			LockSupport.park(this);
			isDone = false;
			if (cont && !yeld) {
				try {
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
		ITaskRunnable task = func.apply(num);
		if (task == null) {
			cycler.removeEntry(func);
		}
		return task;
	}

	private void takeTasksStack() {
		ITaskRunnable task;
		while (shouldContain() && (task = pollTask()) != null) {
			try {
				task.run();
			} catch (Exception e) {
				SKDSCore.LOGGER.error("Exeption while executing task ", e);
			}
			task = null;
		}
	}

	public void forkSync(Function<Integer, ITaskRunnable> sup) {
		//syncSup = sup;
		cycler.addEntry(sup);
		yeld = false;
		isDone = false;
		LockSupport.unpark(this);
	}

	public void waitForJoin() {
		while (!isDone()) {
			Thread.yield();
			LockSupport.parkNanos("waiting for workers to finish", 100000L);
		}
		yeld = true;
	}

	public boolean isDone() {
		return isDone;
	}

	@Override
	public int getIndex() {
		return num;
	}
}