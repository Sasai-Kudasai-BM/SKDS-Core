package net.skds.core.api;

import net.skds.core.util.blockupdate.WWSGlobal;

public interface IWWS {
	public void tickIn();
	public void tickOut();
	public void onClose();
	public void onSave();
	public WWSGlobal getGlob();

	public void tickPreMTH();
	public void tickPostMTH();
}