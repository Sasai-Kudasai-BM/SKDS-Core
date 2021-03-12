package net.skds.core.api;

public interface IWWS {
	public void tickIn();
	public void tickOut();
	public void close();
	public IWWSG getG();
	//public void fork();
	//public void join() throws InterruptedException;
}