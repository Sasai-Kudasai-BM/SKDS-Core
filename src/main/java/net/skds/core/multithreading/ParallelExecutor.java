package net.skds.core.multithreading;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BooleanSupplier;

public class ParallelExecutor implements AutoCloseable {

	private BooleanSupplier task;
	private final int threadCount;
	private final Exec[] threads;
	private CountDownLatch latch;

	public ParallelExecutor() {
		this(Runtime.getRuntime().availableProcessors());
	}

	public ParallelExecutor( int threadCount) {
		this.threadCount = threadCount;

		this.threads = new Exec[this.threadCount];
		for (int index = 0; index < threads.length; index++) {
			threads[index] = new Exec(index);
		}
	}

	public void execute(BooleanSupplier task) {
		this.task = task;
		this.latch = new CountDownLatch(threadCount);
		try {
			for (Exec exec : threads) {
				LockSupport.unpark(exec);
			}
			latch.await();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class Exec extends Thread implements ISKDSThread {

		private boolean alive = true;
		private int index;

		public Exec(int n) {
			setName("SKDS-Executor-" + n);
			setDaemon(false);
			start();
			this.index = n;
		}

		public void kill() {
			alive = false;
			LockSupport.unpark(this);
			//resume();
		}

		@Override
		public void run() {
			LockSupport.park();
			while (alive) {
				try {
					while (task.getAsBoolean());
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					latch.countDown();
					LockSupport.park();
				}
			}
		}

		@Override
		public int getIndex() {
			return index;
		}
	}

	public static class Supl implements BooleanSupplier {

		private Iterator<Runnable> src;

		public Supl(Collection<Runnable> src) {
			this.src = src.iterator();
		}

		public Supl(Runnable[] src) {
			this.src = Arrays.asList(src).iterator();
		}

		@Override
		public boolean getAsBoolean() {
			Runnable r;
			synchronized (src) {
				if (src.hasNext()) {
					r = src.next();
				} else {
					return false;
				}
			}
			r.run();
			return true;
		}
	}

	@Override
	public void close() throws Exception {
		for (Exec exec : threads) {
			exec.kill();
		}
		
	}
}