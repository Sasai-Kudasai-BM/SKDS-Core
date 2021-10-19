package net.skds.core.api;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.skds.core.util.SKDSUtils;

public interface IChunkSectionData {

	public void serialize(CompoundNBT nbt);
	public void deserialize(CompoundNBT nbt);

	public SKDSUtils.Side getSide();
	public int getSize();
	
	public default void onBlockAdded(int x, int y, int z, BlockState newState, BlockState oldState) {}
	public default void tickParallel() {}
	public default void read(PacketBuffer buff) {}
	public default void write(PacketBuffer buff) {}
	
}