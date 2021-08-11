package net.skds.core.util.blockupdate;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ServerWorld;
import net.skds.core.api.IWWS;
import net.skds.core.multithreading.TurboWorldEditor;
import net.skds.core.multithreading.TurboWorldReader;

public abstract class BasicExecutor implements Runnable {

	protected final TurboWorldReader reader;
	protected final TurboWorldEditor editor;

	protected final BlockState nullreturnstate = Blocks.BARRIER.getDefaultState();
	protected final ServerWorld w;
	protected final IWWS owner;
	protected Set<BlockPos> banPoses = new HashSet<>();
	protected boolean cancel = false;

	protected BasicExecutor(ServerWorld w, IWWS owner) {
		this.reader = new TurboWorldReader(w);
		this.editor = new TurboWorldEditor(reader, this::applyAction);
		this.owner = owner;
		this.w = w;
	}

	protected abstract void applyAction(BlockPos pos, BlockState newState, BlockState oldState, ServerWorld w);

	protected BlockState setState(BlockPos pos, BlockState newState) {

		BlockState oldState = setFinBlockState(pos, newState);
		if (oldState != null) {
			applyAction(pos, newState, oldState, w);

		}
		return oldState;
	}

	protected IChunk getIChunk(int blockX, int blockZ) {
		return reader.getIChunk(blockX, blockZ);
	}

	protected IChunk getIChunk(BlockPos pos) {
		return reader.getIChunk(pos.getX(), pos.getZ());
	}

	protected Chunk getChunk(BlockPos pos) {
		return reader.getChunk(pos);
	}

	protected BlockState getBlockState(BlockPos pos) {
		return reader.getBlockState(pos);
	}

	@Deprecated
	protected BlockState setFinBlockState(BlockPos pos, BlockState state) {
		return editor.setMaskedBlockState(pos, state);
	}

	protected boolean isAir(BlockState statex) {
		return statex.getMaterial() == Material.AIR;
	}

	// ============== ENTITY ================= //

	protected TileEntity getTileEntity(BlockPos pos) {
		return reader.getTileEntity(pos);
	}

	protected void addEntity(Entity e) {
		BlockUpdataer.addEntity(e);
	}

	// ==============================
	public static Direction dirFromVec(BlockPos pos1, BlockPos pos2) {
		return Direction.getFacingFromVector(pos2.getX() - pos1.getX(), pos2.getY() - pos1.getY(),
				pos2.getZ() - pos1.getZ());
	}
}