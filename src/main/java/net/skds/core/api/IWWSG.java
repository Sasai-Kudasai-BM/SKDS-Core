package net.skds.core.api;

import net.minecraft.util.math.BlockPos;

public interface IWWSG {
	public IWWS getTyped(Class<? extends IWWS> type);
	public double getSqDistToNBP(BlockPos pos);
	public boolean isPosReady(long pos);
	public boolean banPos(long pos);
	public boolean unbanPos(long pos);
	public void addWWS(IWWS wws);
}