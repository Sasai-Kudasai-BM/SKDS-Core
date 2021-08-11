package net.skds.core.api;

import net.minecraft.nbt.CompoundNBT;

public interface IChunkSectionData {

	public void serialize(CompoundNBT nbt);
	public void deserialize(CompoundNBT nbt);
	
}