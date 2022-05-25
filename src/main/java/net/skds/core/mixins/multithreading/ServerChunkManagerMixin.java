package net.skds.core.mixins.multithreading;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.EmptyChunk;
import net.skds.core.mixinglue.ServerChunkManagerGlue;
import net.skds.core.multithreading.ISKDSThread;

@Mixin(value = { ServerChunkManager.class })
public abstract class ServerChunkManagerMixin implements ServerChunkManagerGlue {

	@Final
	@Shadow
	public ServerWorld world;

	public Chunk getCustomChunk(long l) {
		ChunkHolder chunkHolder = this.getChunkHolder(l);
		if (chunkHolder == null) {
			return null;
		}
		return chunkHolder.getCurrentChunk();
	}

	@Shadow
	private ChunkHolder getChunkHolder(long l) {
		return null;
	}

	@Inject(method = "getChunk", at = @At(value = "HEAD", ordinal = 0), cancellable = true)
	void getChunk(int chunkX, int chunkZ, ChunkStatus requiredStatus, boolean create, CallbackInfoReturnable<Chunk> ci) {
		if (Thread.currentThread() instanceof ISKDSThread) {
			long p = ChunkPos.toLong(chunkX, chunkZ);
			Chunk chunk = getCustomChunk(p);
			if (create && (chunk == null || !(chunk instanceof Chunk))) {
				chunk = new EmptyChunk(world, new ChunkPos(p), world.getRegistryManager().get(Registry.BIOME_KEY).entryOf(BiomeKeys.PLAINS));
			}
			ci.setReturnValue(chunk);
		}
	}
}