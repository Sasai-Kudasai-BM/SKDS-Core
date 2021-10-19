package net.skds.core.mixins.multithreading;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.core.debug.ExampleData;
import net.skds.core.util.data.ChunkSectionAdditionalData;
import net.skds.core.util.interfaces.IChunkSectionExtended;

@Mixin(value = { ChunkSection.class })
public class ChunkSectionMixin implements IChunkSectionExtended {

	private final ChunkSectionAdditionalData addData = new ChunkSectionAdditionalData((ChunkSection) (Object) this);

	@Inject(method = "setBlockState", at = @At(value = "HEAD"), cancellable = true)
	public synchronized void setBlockState(int x, int y, int z, BlockState blockStateIn,
			CallbackInfoReturnable<BlockState> ci) {
		BlockState oldState = this.setBlockState(x, y, z, blockStateIn, true);
		addData.onBlockAdded(x, y, z, blockStateIn, oldState);
		ci.setReturnValue(oldState);
	}

	//@Overwrite
	//public FluidState getFluidState(int x, int y, int z) {
	//	boolean s = addData.getData(ExampleData.class).isPrikol(x, y, z);
	//	return s ? Fluids.WATER.getDefaultState() : Fluids.EMPTY.getDefaultState();
	//}

	@Shadow
	private BlockState setBlockState(int x, int y, int z, BlockState blockStateIn, boolean b) {
		return null;
	}

	@Override
	public ChunkSectionAdditionalData getData() {
		return addData;
	}
	
	@Inject(method = "write", at = @At(value = "TAIL"))
	void write(PacketBuffer buff, CallbackInfo ci) {
		addData.write(buff);
	}

	@OnlyIn(Dist.CLIENT)
	@Inject(method = "read", at = @At(value = "TAIL"))
	void read(PacketBuffer buff, CallbackInfo ci) {
		addData.read(buff);
	}

	@OnlyIn(Dist.CLIENT)
	@Inject(method = "getSize", at = @At(value = "RETURN"), cancellable = true)
	void getSize(CallbackInfoReturnable<Integer> ci) {
		int size = ci.getReturnValueI() + addData.getSize();
		ci.setReturnValue(size);
	}
}