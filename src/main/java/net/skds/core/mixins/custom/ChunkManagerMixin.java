package net.skds.core.mixins.custom;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ChunkManager;

@Mixin(ChunkManager.class)
public interface ChunkManagerMixin {

	@Invoker("getLoadedChunksIterable")
	public Iterable<ChunkHolder> getLoadedChunks();
}
