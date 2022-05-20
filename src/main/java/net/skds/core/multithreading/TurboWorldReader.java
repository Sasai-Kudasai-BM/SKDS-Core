package net.skds.core.multithreading;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;

public class TurboWorldReader {

	public final int minY;
	public final int maxY;
	public final int sections;

	public final BlockState nullreturnstate = Blocks.AIR.getDefaultState();
	public final FluidState nullreturnFstate = Fluids.EMPTY.getDefaultState();
	public final World world;
	private Chunk chunkCash = null;
	private long chunkPosCash = 0;
	private boolean newChunkCash = true;
	
	public final boolean isClient;

	public TurboWorldReader(World world) {
		this.isClient = world.isClient;
		this.world = world;
		this.minY = world.getBottomY();
		this.maxY = minY + world.getHeight();
		this.sections = world.getHeight() >> 4;
	}

	public Chunk getIChunk(int blockX, int blockZ) {
		long lpos = ChunkPos.toLong(blockX >> 4, blockZ >> 4);
		if (newChunkCash || lpos != chunkPosCash) {
			newChunkCash = false;
			var manager = world.getChunkManager();
			if (isClient) {
				chunkCash = ((ServerChunkManager) manager).threadedAnvilChunkStorage.ge prov.getChunkNow(blockX >> 4, blockZ >> 4);
			} else {
				chunkCash = ((IServerChunkProvider) prov).getCustomChunk(lpos);
			}
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

	public static boolean isAir(BlockState state) {
		return state.getMaterial() == Material.AIR;
	}
}