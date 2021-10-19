package net.skds.core.mixins.custom;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.ChunkSection;
import net.skds.core.util.data.ChunkSectionAdditionalData;

@Mixin(Chunk.class)
public class ChunkMixin {

	@Final
	@Shadow
	private World world;
	@Final
	@Shadow
	private ChunkSection[] sections;
	
	@Inject(method = "Lnet/minecraft/world/chunk/Chunk;<init>(Lnet/minecraft/world/World;Lnet/minecraft/world/chunk/ChunkPrimer;)V", at = @At("TAIL"))
	void init(World worldIn, ChunkPrimer primer, CallbackInfo ci) {
		for (ChunkSection section : sections) {
			if (section != null) {
				ChunkSectionAdditionalData.getFromSection(section).finish(worldIn);
			}
		}
	}

}
