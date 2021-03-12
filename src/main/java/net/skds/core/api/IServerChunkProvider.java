package net.skds.core.api;

import net.minecraft.world.chunk.IChunk;

public interface IServerChunkProvider {
    public IChunk getCustomChunk(long l);
}