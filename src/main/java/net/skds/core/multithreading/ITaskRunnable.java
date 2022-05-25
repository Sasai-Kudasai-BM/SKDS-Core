package net.skds.core.multithreading;

public interface ITaskRunnable extends Runnable {
	
	public boolean revoke();
	public double getPriority();
	public float getWeight();
}