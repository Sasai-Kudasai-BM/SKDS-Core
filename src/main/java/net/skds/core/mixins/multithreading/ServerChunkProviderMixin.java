package net.skds.core.mixins.multithreading;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.skds.core.api.IServerChunkProvider;
import net.skds.core.api.multithreading.ISKDSThread;

@Mixin(value = { ServerChunkProvider.class })
public abstract class ServerChunkProviderMixin implements IServerChunkProvider {

	@Final
	@Shadow
	public ServerWorld world;

	public IChunk getCustomChunk(long l) {
		ChunkHolder chunkHolder = this.func_217213_a(l);
		if (chunkHolder == null) {
			return null;
		}
		return chunkHolder.func_219287_e();
	}

	@Shadow
	private ChunkHolder func_217213_a(long l) {
		return null;
	}

	@Inject(method = "getChunk", at = @At(value = "HEAD", ordinal = 0), cancellable = true)
	public void getChunk(int chunkX, int chunkZ, ChunkStatus requiredStatus, boolean load,
			CallbackInfoReturnable<IChunk> ci) {
		if (Thread.currentThread() instanceof ISKDSThread) {
			long p = ChunkPos.asLong(chunkX, chunkZ);
			IChunk iChunk = getCustomChunk(p);
			if (load && (iChunk == null || !(iChunk instanceof Chunk))) {
				iChunk = new EmptyChunk(world, new ChunkPos(chunkX, chunkZ));
			}
			ci.setReturnValue(iChunk);
		}
	}
}