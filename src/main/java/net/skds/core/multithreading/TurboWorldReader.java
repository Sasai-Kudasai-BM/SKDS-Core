package net.skds.core.multithreading;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.skds.core.api.IServerChunkProvider;
import net.skds.core.util.data.ChunkSectionAdditionalData;

public class TurboWorldReader {

	public final BlockState nullreturnstate = Blocks.AIR.getDefaultState();
	public final FluidState nullreturnFstate = Fluids.EMPTY.getDefaultState();
	public final ServerWorld world;
	private IChunk chunkCash = null;
	private long chunkPosCash = 0;
	private boolean newChunkCash = true;

	public TurboWorldReader(ServerWorld world) {
		this.world = world;
	}

	public IChunk getIChunk(int blockX, int blockZ) {
		long lpos = ChunkPos.asLong(blockX >> 4, blockZ >> 4);
		if (newChunkCash || lpos != chunkPosCash) {
			newChunkCash = false;
			ServerChunkProvider prov = (ServerChunkProvider) world.getChunkProvider();
			chunkCash = ((IServerChunkProvider) prov).getCustomChunk(lpos);
			chunkPosCash = lpos;
		}
		return chunkCash;
	}

	public IChunk getIChunk(BlockPos pos) {
		return getIChunk(pos.getX(), pos.getZ());
	}

	public Chunk getChunk(BlockPos pos) {
		IChunk iChunk = getIChunk(pos);
		if (iChunk != null && iChunk instanceof Chunk) {
			return (Chunk) iChunk;
		}
		return null;
	}

	public BlockState getBlockState(BlockPos pos) {
		IChunk chunk = getIChunk(pos);
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
		IChunk chunk = getIChunk(pos);
		if (chunk == null) {
			return nullreturnFstate;
		}
		FluidState ssss = chunk.getFluidState(pos);
		if (ssss == null) {
			return nullreturnFstate;
		}

		return ssss;
	}

	public ChunkSection getChunkSection(BlockPos pos, boolean create) {
		Chunk chunk = getChunk(pos);
		if (chunk == null) {
			return null;
		}
		int y = pos.getY();
		if (y < 0 || y > 255) {
			return null;
		}
		ChunkSection[] chunksections = chunk.getSections();
		ChunkSection sec = chunksections[y >> 4];
		if (sec == null && create) {
			sec = new ChunkSection(y >> 4 << 4);
			chunksections[y >> 4] = sec;
		}
		return sec;
	}

	public TileEntity getTileEntity(BlockPos pos) {
		Chunk ch = getChunk(pos);
		TileEntity tileentity = null;

		if (ch != null) {
			tileentity = ch.getTileEntity(pos, Chunk.CreateEntityType.IMMEDIATE);
		}

		return tileentity;
	}

	// STATIC

	public static Set<BlockPos> getBlockPoses(AxisAlignedBB aabb) {
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

	public ChunkSectionAdditionalData getCSAD(BlockPos pos, boolean createSection) {
		ChunkSection cs = getChunkSection(pos, createSection);
		if (cs == null) {
			return null;
		}
		return ChunkSectionAdditionalData.getFromSection(cs);
	}

	public static boolean isAir(BlockState state) {
		return state.getMaterial() == Material.AIR;
	}
}