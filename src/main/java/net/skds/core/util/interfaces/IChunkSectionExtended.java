package net.skds.core.util.interfaces;

import net.minecraft.world.chunk.ChunkSection;
import net.skds.core.util.data.ChunkSectionAdditionalData;

public interface IChunkSectionExtended {	
	public ChunkSectionAdditionalData getData();
	public void setData(ChunkSectionAdditionalData data);

	public static ChunkSectionAdditionalData getData(ChunkSection section) {
		return ((IChunkSectionExtended) section).getData();
	}

	public static void setData(ChunkSectionAdditionalData data, ChunkSection section) {
		((IChunkSectionExtended) section).setData(data);
	}
}