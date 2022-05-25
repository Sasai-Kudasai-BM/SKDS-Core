package net.skds.core.mixinglue;

import net.minecraft.world.chunk.Chunk;

public interface ServerChunkManagerGlue {

	public Chunk getChunkSKDS(long pos);
}
