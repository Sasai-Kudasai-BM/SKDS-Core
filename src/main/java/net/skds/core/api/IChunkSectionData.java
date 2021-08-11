package net.skds.core.api;

import net.minecraft.nbt.CompoundNBT;

public interface IChunkSectionData {
  void serialize(CompoundNBT paramCompoundNBT);
  
  void deserialize(CompoundNBT paramCompoundNBT);
}
