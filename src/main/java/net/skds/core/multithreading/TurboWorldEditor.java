package net.skds.core.multithreading;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.server.ServerWorld;

public class TurboWorldEditor {

	public final BlockState nullreturnstate = Blocks.AIR.getDefaultState();
	public final ServerWorld world;	
	public final TurboWorldReader reader;
	public final Action<BlockPos, BlockState, BlockState, ServerWorld> action;

	public TurboWorldEditor(TurboWorldReader reader, Action<BlockPos, BlockState, BlockState, ServerWorld> act) {
		this.world = reader.world;
		this.reader = reader;
		this.action = act;
	}	
	
	public BlockState setState(BlockPos pos, BlockState newState) {

		BlockState oldState = setMaskedBlockState(pos, newState);
		if (oldState != null) {
			action.applyAction(pos, newState, oldState, world);

		} else {
			return nullreturnstate;
		}
		return oldState;
	}

	
	public BlockState setMaskedBlockState(BlockPos pos, BlockState state) {
		Chunk chunk = reader.getChunk(pos);
		if (chunk == null) {
			return null;
		}

		int y = pos.getY();
		if (y < 0 || y > 255) {
			return null;
		}
		ChunkSection[] chunksections = chunk.getSections();
		ChunkSection sec = chunksections[y >> 4];
		if (sec == null) {
			sec = new ChunkSection(y >> 4 << 4);
			chunksections[y >> 4] = sec;
		}
		BlockState setted = sec.setBlockState(pos.getX() & 15, y & 15, pos.getZ() & 15, state);

		return setted;
	}

	public static interface Action<P extends BlockPos, A extends BlockState, B extends BlockState, W extends ServerWorld> {
		public void applyAction(P pos, A newState, B oldState, W w);
	}
}