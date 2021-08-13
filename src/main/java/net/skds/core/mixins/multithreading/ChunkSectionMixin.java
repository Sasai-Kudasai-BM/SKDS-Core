package net.skds.core.mixins.multithreading;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.world.chunk.ChunkSection;
import net.skds.core.util.data.ChunkSectionAdditionalData;
import net.skds.core.util.interfaces.IChunkSectionExtended;

@Mixin(value = { ChunkSection.class })
public class ChunkSectionMixin implements IChunkSectionExtended {

	private final ChunkSectionAdditionalData addData = new ChunkSectionAdditionalData((ChunkSection) (Object) this);

	@Inject(method = "setBlockState", at = @At(value = "HEAD"), cancellable = true)
	public synchronized void setBlockState(int x, int y, int z, BlockState blockStateIn,
			CallbackInfoReturnable<BlockState> ci) {
		ci.setReturnValue(this.setBlockState(x, y, z, blockStateIn, true));
	}

	@Shadow
	private BlockState setBlockState(int x, int y, int z, BlockState blockStateIn, boolean b) {
		return null;
	}

	@Override
	public ChunkSectionAdditionalData getData() {
		return addData;
	}
}