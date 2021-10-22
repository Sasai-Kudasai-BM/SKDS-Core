package net.skds.core.mixins.custom;

import java.util.concurrent.atomic.AtomicInteger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SChunkDataPacket;
import net.minecraft.world.chunk.Chunk;
import net.skds.core.util.data.capability.ChunkCapabilityData;

@Mixin(SChunkDataPacket.class)
public class SChunkDataPacketMixin {


	@Inject(method = "extractChunkData", at = @At("RETURN"))
	void extractChunkData(PacketBuffer buf, Chunk chunkIn, int writeSkylight, CallbackInfoReturnable<Integer> ci) {
		ChunkCapabilityData.apply(chunkIn, dat -> dat.write(buf));
	}

	@Inject(method = "calculateChunkSize", at = @At("RETURN"), cancellable = true)
	void calculateChunkSize(Chunk chunkIn, int changedSectionsIn, CallbackInfoReturnable<Integer> ci) {
		AtomicInteger i = new AtomicInteger(ci.getReturnValue());
		ChunkCapabilityData.apply(chunkIn, dat -> i.addAndGet(dat.getSize()));
		ci.setReturnValue(i.get());
	}
}
