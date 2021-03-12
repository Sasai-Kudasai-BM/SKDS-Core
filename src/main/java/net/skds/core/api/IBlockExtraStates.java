package net.skds.core.api;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;

public interface IBlockExtraStates {
	
	default void customStatesRegister(Block b, StateContainer.Builder<Block, BlockState> builder) {		
	}
}