package net.skds.core.multithreading;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.WorldChunk.CreationType;
import net.skds.core.mixinglue.ServerChunkManagerGlue;

public class TurboWorldReader {

	public final int minY;
	public final int maxY;
	public final int sections;

	public final BlockState nullreturnstate = Blocks.AIR.getDefaultState();
	public final FluidState nullreturnFstate = Fluids.EMPTY.getDefaultState();
	public final World world;
	private WorldChunk chunkCash = null;
	private long chunkPosCash = 0;

	public final boolean isClient;

	public TurboWorldReader(World world) {
		this.isClient = world.isClient;
		this.world = world;
		this.minY = world.getBottomY();
		this.maxY = minY + world.getHeight();
		this.sections = world.getHeight() >> 4;
	}

	public Chunk getIChunk(int blockX, int blockZ) {
		Chunk c;
		var manager = world.getChunkManager();
		if (!isClient) {
			c = ((ServerChunkManagerGlue) manager).getChunkSKDS(ChunkPos.toLong(blockX >> 4, blockZ >> 4));
		} else {
			c = manager.getWorldChunk(blockX >> 4, blockZ >> 4);
		}
		return c;
	}

	public WorldChunk getChunk(BlockPos pos) {

		long lpos = ChunkPos.toLong(pos.getX() >> 4, pos.getZ() >> 4);
		if (chunkCash == null || lpos != chunkPosCash) {
			chunkPosCash = lpos;
			Chunk cc = getIChunk(pos.getX() >> 4, pos.getZ() >> 4);

			if (cc != null && cc instanceof WorldChunk wc) {
				chunkCash = wc;
			}
		}

		return chunkCash;

	}

	public BlockState getBlockState(BlockPos pos) {
		WorldChunk chunk = getChunk(pos);
		if (chunk == null) {
			return nullreturnstate;
		}
		BlockState ssss = chunk.getBlockState(pos);
		if (ssss == null) {
			return nullreturnstate;
		}

		return ssss;
	}

	public FluidState getFluidState(BlockPos pos) {
		WorldChunk chunk = getChunk(pos);
		if (chunk == null) {
			return nullreturnFstate;
		}
		FluidState ssss = chunk.getFluidState(pos);
		if (ssss == null) {
			return nullreturnFstate;
		}

		return ssss;
	}

	public BlockEntity getTileEntity(BlockPos pos) {
		WorldChunk ch = getChunk(pos);
		BlockEntity tileentity = null;

		if (ch != null) {
			tileentity = ch.getBlockEntity(pos, CreationType.IMMEDIATE);
		}

		return tileentity;
	}

	// STATIC

	public static Set<BlockPos> getBlockPoses(Box aabb) {
		int x0 = (int) Math.floor(aabb.minX);
		int y0 = (int) Math.floor(aabb.minY);
		int z0 = (int) Math.floor(aabb.minZ);

		int x2 = (int) Math.ceil(aabb.maxX);
		int y2 = (int) Math.ceil(aabb.maxY);
		int z2 = (int) Math.ceil(aabb.maxZ);

		Set<BlockPos> set = new HashSet<>();
		for (int x = x0; x <= x2; x++) {
			for (int y = y0; y <= y2; y++) {
				for (int z = z0; z <= z2; z++) {
					set.add(new BlockPos(x, y, z));
				}
			}
		}
		return set;
	}

	public static boolean isAir(BlockState state) {
		return state.getMaterial() == Material.AIR;
	}
}