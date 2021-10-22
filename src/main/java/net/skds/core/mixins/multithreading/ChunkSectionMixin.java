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

	private ChunkSectionAdditionalData addData;

	@Inject(method = "setBlockState", at = @At(value = "HEAD"), cancellable = true)
	public synchronized void setBlockState(int x, int y, int z, BlockState blockStateIn,
			CallbackInfoReturnable<BlockState> ci) {
		BlockState oldState = this.setBlockState(x, y, z, blockStateIn, true);
		//addData.onBlockAdded(x, y, z, blockStateIn, oldState);
		ci.setReturnValue(oldState);
	}

	//@Overwrite
	//public FluidState getFluidState(int x, int y, int z) {
	//	if (addData != null) {
	//		boolean s = addData.getData(ExampleData.class).isPrikol(x, y, z);
	//		if (s) {
	//			return Fluids.WATER.getDefaultState();
	//		}
	//	}
	//	return Fluids.EMPTY.getDefaultState();
	//}

	@Shadow
	private BlockState setBlockState(int x, int y, int z, BlockState blockStateIn, boolean b) {
		return null;
	}

	@Override
	public ChunkSectionAdditionalData getData() {
		return addData;
	}

	@Override
	public void setData(ChunkSectionAdditionalData data) {
		this.addData = data;
	}

	//@Inject(method = "write", at = @At(value = "TAIL"))
	//void write(PacketBuffer buff, CallbackInfo ci) {
	//	if (addData != null) {
	//		addData.write(buff);
	//	}
	//}

	//@OnlyIn(Dist.CLIENT)
	//@Inject(method = "read", at = @At(value = "TAIL"))
	//void read(PacketBuffer buff, CallbackInfo ci) {
	//	
	//	if (addData != null) {
	//		addData.read(buff);
	//	}
	//}

	//@Inject(method = "getSize", at = @At(value = "RETURN"), cancellable = true)
	//void getSize(CallbackInfoReturnable<Integer> ci) {
	//	if (addData != null) {
	//		int size = ci.getReturnValueI() + addData.getSize();
	//		ci.setReturnValue(size);
	//	}
	//}
}