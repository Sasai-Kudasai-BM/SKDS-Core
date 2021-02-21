package net.skds.skds_core.util.blockupdate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.netty.util.internal.ConcurrentSet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.skds.skds_core.events.OnWWSAttachEvent;
import net.skds.skds_core.util.Interface.IWWS;
import net.skds.skds_core.util.Interface.IWorldExtended;

public class WWSGlobal {

	private static final Map<World, WWSGlobal> MAP = new HashMap<>();

	final World world;

	public Set<BlockPos> PLAYERS = new HashSet<>();

	public Map<Class<? extends IWWS>, IWWS> WWS = new HashMap<>();

	ConcurrentSet<Long> banPos = new ConcurrentSet<>();
	ConcurrentSet<Long> banPosOld = new ConcurrentSet<>();

	public WWSGlobal(World w) {
		world = w;
		MinecraftForge.EVENT_BUS.post(new OnWWSAttachEvent(w, this));
	}

	void bpClean() {
		banPos.forEach(l -> {
			if (banPosOld.contains(l)) {
				banPos.remove(l);
				// System.out.println(BlockPos.fromLong(l) + " Pizdos");
			}
		});

		banPosOld.clear();
		banPosOld.addAll(banPos);
	}

	public void addWWS(IWWS wws) {
		WWS.put(wws.getClass(), wws);
	}

	public boolean isPosReady(BlockPos pos) {
		return !banPos.contains(pos.toLong());
	}

	public boolean banPos(BlockPos pos) {
		long lp = pos.toLong();
		boolean ss = banPos.add(lp);
		return ss;
	}

	public boolean banPos(long pos) {
		boolean ss = banPos.add(pos);
		return ss;
	}

	public boolean banPoses(Set<BlockPos> poses) {
		Set<Long> blocked = new HashSet<>();
		for (BlockPos pos : poses) {
			long lp = pos.toLong();
			boolean ss = banPos.add(lp);
			if (!ss) {
				banPos.removeAll(blocked);
				return false;
			}
			blocked.add(lp);
		}
		return true;
	}

	public void unbanPoses(Set<BlockPos> poses) {
		for (BlockPos pos : poses) {
			long l = pos.toLong();
			banPos.remove(l);
			banPosOld.remove(l);
		}
	}

	public void unbanPosesL(Set<Long> poses) {
		for (long pos : poses) {
			banPos.remove(pos);
			banPosOld.remove(pos);
		}
	}

	public void stop() {
		WWS.forEach((s, wws) -> {
			wws.close();
		});
	}

	public void tickIn() {
		updatePlayers();
		bpClean();
		WWS.forEach((s, wws) -> {
			wws.tickIn();
		});
	}

	public void tickOut() {
		WWS.forEach((s, wws) -> {
			wws.tickOut();
		});
	}

	private void updatePlayers() {
		List<? extends PlayerEntity> players = world.getPlayers();
		Set<BlockPos> np = new HashSet<>();
		for (PlayerEntity p : players) {
			BlockPos pos = p.getPosition();
			np.add(pos);
		}
		PLAYERS = np;
	}

	public double getSqDistToNBP(BlockPos pos) {
		double dist = Double.MAX_VALUE;
		for (BlockPos pos2 : PLAYERS) {
			double dx = (pos.getX() - pos2.getX());
			double dz = (pos.getZ() - pos2.getZ());

			dist = Math.min(dist, (dx * dx) + (dz * dz));
		}
		return dist;
	}

	public IWWS getTyped(Class<? extends IWWS> type) {
		return WWS.get(type);
	}

	//public static WWSGlobal get(ServerWorld w) {
	//	return MAP.get(w);
	//}

	public static WWSGlobal get(World w) {
		return MAP.get(w);
	}

	public static void loadWorld(World w) {
		WWSGlobal wwsg = new WWSGlobal(w);
		((IWorldExtended) w).addWWS(wwsg);
		MAP.put(w, wwsg);
	}

	public static void unloadWorld(World w) {
		WWSGlobal wwsg = MAP.get(w);
		if (wwsg != null) {
			wwsg.stop();
			MAP.remove(w);
		}
	}

	public static void tickInG() {
		MAP.forEach((w, wwsg) -> {
			wwsg.tickIn();
		});
	}

	public static void tickOutG() {		
		MAP.forEach((w, wwsg) -> {
			wwsg.tickOut();
		});
	}
}