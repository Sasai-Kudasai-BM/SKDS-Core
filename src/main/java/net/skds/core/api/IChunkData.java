package net.skds.core.api;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.skds.core.util.SKDSUtils;

public interface IChunkData {

	public void serialize(NbtCompound nbt);
	public void deserialize(NbtCompound nbt);

	public SKDSUtils.Side getSide();

	public int getSize();
	
	public default void tick() {}
	public default void onUnload() {}
	public default void read(PacketByteBuf buff) {}
	public default void write(PacketByteBuf buff) {}
	
}