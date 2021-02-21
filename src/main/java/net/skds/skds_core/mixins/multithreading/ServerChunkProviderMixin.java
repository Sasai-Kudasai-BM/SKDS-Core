package net.skds.skds_core.mixins.multithreading;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ServerChunkProvider;
import net.skds.skds_core.util.Interface.ISKDSThread;
import net.skds.skds_core.util.Interface.IServerChunkProvider;

@Mixin(value = { ServerChunkProvider.class })
public class ServerChunkProviderMixin implements IServerChunkProvider {

    public IChunk getCustomChunk(long l) {
        ChunkHolder chunkHolder = this.func_217213_a(l);
        if (chunkHolder == null) {
            return null;
        }
        return chunkHolder.func_219287_e();
    }

    @Shadow(aliases = "func_217213_a")
    private ChunkHolder func_217213_a(long l) {
        return null;
    }

    // *
    @Inject(method = "Lnet/minecraft/world/server/ServerChunkProvider;getChunk(IILnet/minecraft/world/chunk/ChunkStatus;Z)Lnet/minecraft/world/chunk/IChunk;", at = @At(value = "HEAD", ordinal = 0), cancellable = true)
    private void gc(int x, int z, ChunkStatus status, boolean b, CallbackInfoReturnable<IChunk> ci) {
        if (Thread.currentThread() instanceof ISKDSThread) {
            long lpos = ChunkPos.asLong(x >> 4, z >> 4);
            IChunk c = getCustomChunk(lpos);

            ci.setReturnValue(c);
        }
    }
    // */
}