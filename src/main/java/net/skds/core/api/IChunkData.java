package net.skds.core.api;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.skds.core.util.SKDSUtils;

public interface IChunkData {

	public void serialize(CompoundNBT nbt);
	public void deserialize(CompoundNBT nbt);

	public SKDSUtils.Side getSide();

	public int getSize();
	
	public default void tick() {}
	public default void onUnload() {}
	public default void read(PacketBuffer buff) {}
	public default void write(PacketBuffer buff) {}
	
}