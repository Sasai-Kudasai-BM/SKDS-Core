package net.skds.core.util.blockupdate;

import java.util.ArrayList;
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
import net.skds.core.api.IWWS;
import net.skds.core.events.OnWWSAttachEvent;

public class WWSGlobal {

	private static List<WWSGlobal> INSTANCES = new ArrayList<>();
	public final World world;
	private Set<BlockPos> players = new HashSet<>();
	private Map<Class<? extends IWWS>, IWWS> WWS = new HashMap<>(4);

	private ConcurrentSet<Long> banPos = new ConcurrentSet<>();
	private ConcurrentSet<Long> banPosOld = new ConcurrentSet<>();

	public WWSGlobal(World w) {
		world = w;
		MinecraftForge.EVENT_BUS.post(new OnWWSAttachEvent(w, this));
		INSTANCES.add(this);
	}

	private void bpClean() {
		banPos.removeAll(banPosOld);

		banPosOld.clear();
		banPosOld.addAll(banPos);
	}

	public void addWWS(IWWS wws) {
		WWS.put(wws.getClass(), wws);
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
	}

	@SuppressWarnings("unchecked")
	public <T extends IWWS> T getTyped(Class<T> type) {
		return (T) WWS.get(type);
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
}