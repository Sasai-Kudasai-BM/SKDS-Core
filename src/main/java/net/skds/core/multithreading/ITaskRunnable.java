package net.skds.core.multithreading;

import net.minecraft.world.World;

public interface ITaskRunnable extends Runnable {
	
	public boolean revoke(World w);
	public double getPriority();
	public int getSubPriority();
}