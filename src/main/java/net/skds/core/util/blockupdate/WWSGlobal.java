package net.skds.core.util.blockupdate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.netty.util.internal.ConcurrentSet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.skds.core.api.IWWS;
import net.skds.core.api.IWorldExtended;
import net.skds.core.api.multithreading.ITaskRunnable;
import net.skds.core.events.OnWWSAttachEvent;
import net.skds.core.util.Class2InstanceMap;
import net.skds.core.util.data.ChunkSectionAdditionalData;

public class WWSGlobal {

	private static List<WWSGlobal> INSTANCES = new ArrayList<>();
	public final World world;
	private Set<BlockPos> players = new HashSet<>();
	private Class2InstanceMap<IWWS> WWS = new Class2InstanceMap<>();

	private ConcurrentSet<Long> banPos = new ConcurrentSet<>();
	private ConcurrentSet<Long> banPosOld = new ConcurrentSet<>();

	public WWSGlobal(World w) {
		world = w;
		MinecraftForge.EVENT_BUS.post(new OnWWSAttachEvent(w, this));
		INSTANCES.add(this);
	}

	public static WWSGlobal get(World world) {
		return ((IWorldExtended) world).getWWS();
	}

	private void bpClean() {
		banPos.removeAll(banPosOld);

		banPosOld.clear();
		banPosOld.addAll(banPos);
	}

	public void addWWS(IWWS wws) {
		WWS.put(wws);
	}

	public void stop() {
		WWS.iterate(wws -> wws.close());
	}

	public void tickIn() {
		updatePlayers();
		bpClean();
		WWS.iterate(wws -> wws.tickIn());
	}

	public void tickOut() {
		WWS.iterate(wws -> wws.tickOut());
		/*
		if (!world.isRemote) {
			ServerChunkProvider serverChunkProvider = (ServerChunkProvider) world.getChunkProvider();
			for (ChunkHolder holder : ((ChunkManagerMixin) serverChunkProvider.chunkManager).getLoadedChunks()) {
				if (holder != null) {
					Chunk chunk = holder.getChunkIfComplete();
					if (chunk != null) {
						for (ChunkSection section : chunk.getSections()) {
							if (section != null) {
								ChunkSectionAdditionalData.getFromSection(section).tick();
							}
						}
					}
				}
			}
		}
		//*/
	}

	private void updatePlayers() {
		List<? extends PlayerEntity> playersss = world.getPlayers();
		Set<BlockPos> np = new HashSet<>();
		for (PlayerEntity p : playersss) {
			BlockPos pos = p.getPosition();
			np.add(pos);
		}
		players = np;
	}

	public double getSqDistToNBP(BlockPos pos) {
		double dist = 1.0E20;
		for (BlockPos pos2 : players) {
			double dx = (pos.getX() - pos2.getX());
			double dz = (pos.getZ() - pos2.getZ());
			dist = Math.min(dist, (dx * dx) + (dz * dz));
		}
		return dist;
		//return 1;
	}

	public <T extends IWWS> T getTyped(Class<T> type) {
		return WWS.get(type);
	}

	public void unloadWorld(World w) {
		INSTANCES.remove(this);
		stop();
	}

	public boolean unbanPos(long pos) {
		return banPos.remove(pos);
	}

	public boolean banPos(long pos) {
		boolean ss = banPos.add(pos);
		return ss;
	}

	public boolean isPosReady(long pos) {
		return !banPos.contains(pos);
	}

	public static void tickPostMTH() {
		INSTANCES.forEach(wwsg -> {
			wwsg.WWS.iterate(w -> w.tickPostMTH());
		});
	}

	public static void tickPreMTH() {
		INSTANCES.forEach(wwsg -> {
			wwsg.WWS.iterate(w -> w.tickPreMTH());
		});
	}

	public static class TickSectionTask implements ITaskRunnable {

		private final ChunkSectionAdditionalData csad;

		public TickSectionTask(ChunkSectionAdditionalData csad) {
			this.csad = csad;
		}

		@Override
		public void run() {
			csad.tick();
		}

		@Override
		public boolean revoke(World w) {
			return false;
		}

		@Override
		public double getPriority() {
			return -1E6;
		}

		@Override
		public int getSubPriority() {
			return 0;
		}

	}
}