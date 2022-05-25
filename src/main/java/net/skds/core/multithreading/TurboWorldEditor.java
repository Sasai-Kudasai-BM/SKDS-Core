package net.skds.core.multithreading;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

public class TurboWorldEditor {

	public final TurboWorldReader reader;
	public final Action<BlockPos, BlockState, BlockState, World> action;

	public TurboWorldEditor(TurboWorldReader reader, Action<BlockPos, BlockState, BlockState, World> act) {
		this.reader = reader;
		this.action = act;
	}	
	
	public BlockState setState(BlockPos pos, BlockState newState, int flags) {
		WorldChunk chunk = reader.getChunk(pos);
		if (chunk == null) {
			return reader.nullreturnstate;
		}
		BlockState oldState = chunk.setBlockState(pos, newState, false);
		if (oldState != null) {
			action.applyAction(pos, newState, oldState, reader.world);
		} else {
			return reader.nullreturnstate;
		}
		return oldState;
	}

	public static interface Action<P extends BlockPos, A extends BlockState, B extends BlockState, W extends World> {
		public void applyAction(P pos, A newState, B oldState, W w);
	}
}