package net.skds.core.mixins.custom;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.biome.BiomeContainer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.core.util.data.capability.ChunkCapabilityData;

@Mixin(Chunk.class)
public class ChunkMixin {

	@OnlyIn(Dist.CLIENT)
	@Inject(method = "read", at = @At("TAIL"))
	void read(BiomeContainer biomeContainerIn, PacketBuffer packetBufferIn, CompoundNBT nbtIn, int availableSections, CallbackInfo ci) {
		ChunkCapabilityData.apply((Chunk) (Object) this, dat -> dat.read(packetBufferIn));
	}
}
