package net.skds.skds_core.mixins.custom;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.state.StateContainer.Builder;
import net.skds.skds_core.util.Interface.IBlockExtended;
import net.skds.skds_core.util.api.CustomBlockPars;

@Mixin(Block.class)
public class BlockMixin implements IBlockExtended {

	private CustomBlockPars customBlockPars = new CustomBlockPars();

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;fillStateContainer(Lnet/minecraft/state/StateContainer$Builder;)V"))
	protected void aaa(Block b, StateContainer.Builder<Block, BlockState> builder) {
		fillStateContainer(builder);
		customStatesRegister(b, builder);
	}

	protected void customStatesRegister(Block b, StateContainer.Builder<Block, BlockState> builder) {		
	}

	@Shadow
	protected final void setDefaultState(BlockState state) {
	}

	@Shadow
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
	}

	@Override
	public CustomBlockPars getCustomBlockPars() {
		return customBlockPars;
	}

	@Override
	public void setCustomBlockPars(CustomBlockPars param) {
		customBlockPars = param;
	}
}