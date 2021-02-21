package net.skds.skds_core.util.Interface;

import net.minecraft.world.chunk.IChunk;

public interface IServerChunkProvider {
    public IChunk getCustomChunk(long l);
}