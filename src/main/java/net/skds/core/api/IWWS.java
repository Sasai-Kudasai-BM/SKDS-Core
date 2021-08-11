package net.skds.core.api;

import net.skds.core.util.blockupdate.WWSGlobal;

public interface IWWS {
	public void tickIn();
	public void tickOut();
	public void close();
	public WWSGlobal getG();
	//public void fork();
	//public void join() throws InterruptedException;
}