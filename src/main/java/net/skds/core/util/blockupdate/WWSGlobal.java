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
import net.skds.core.api.IWWSG;
import net.skds.core.events.OnWWSAttachEvent;

public class WWSGlobal implements IWWSG {

	private static List<WWSGlobal> INSTANCES = new ArrayList<>();
	// private static final Set<UniversalWorkerThread> THREADS =
	// UniversalWorkerThread.create(Runtime.getRuntime().availableProcessors());
	// private static final Set<UniversalWorkerThread> THREADS =
	// UniversalWorkerThread.create(8);

	//private static final Comparator<ITaskRunnable> comp = new Comparator<ITaskRunnable>() {
	//	@Override
	//	public int compare(ITaskRunnable k1, ITaskRunnable k2) {
	//		double dcomp = (k1.getPriority() - k2.getPriority());
	//		int comp = (int) dcomp;
	//		if (comp == 0) {
	//			comp = dcomp > 0 ? 1 : -1;
	//		}
	//		return comp;
	//	}
	//};

	//private static ConcurrentSkipListSet<ITaskRunnable> TASKS = new ConcurrentSkipListSet<>(comp);

	public final World world;
	private Set<BlockPos> players = new HashSet<>();
	private Map<Class<? extends IWWS>, IWWS> WWS = new HashMap<>();

	private ConcurrentSet<Long> banPos = new ConcurrentSet<>();
	private ConcurrentSet<Long> banPosOld = new ConcurrentSet<>();

	public WWSGlobal(World w) {
		world = w;
		MinecraftForge.EVENT_BUS.post(new OnWWSAttachEvent(w, this));
		INSTANCES.add(this);
	}

	private void bpClean() {
		banPos.removeAll(banPosOld);
		// banPos.clear();

		banPosOld.clear();
		banPosOld.addAll(banPos);
	}

	@Override
	public void addWWS(IWWS wws) {
		WWS.put(wws.getClass(), wws);
	}

	public void stop() {
		WWS.forEach((s, wws) -> {
			wws.close();
		});
	}

	public void tickIn() {
		// System.out.println(TASKS.size());
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

	@Override
	public double getSqDistToNBP(BlockPos pos) {
		double dist = Double.MAX_VALUE;
		for (BlockPos pos2 : players) {
			double dx = (pos.getX() - pos2.getX());
			double dz = (pos.getZ() - pos2.getZ());

			dist = Math.min(dist, (dx * dx) + (dz * dz));
		}
		return dist;
	}

	@Override
	public IWWS getTyped(Class<? extends IWWS> type) {
		return WWS.get(type);
	}

	public void unloadWorld(World w) {
		INSTANCES.remove(this);
		stop();
		//TASKS.forEach(task -> {
		//	if (task.revoke(w)) {
		//		TASKS.remove(task);
		//		task = null;
		//	}
		//});
	}

	//public static ITaskRunnable nextTask() {
	//	if (MTHooks.COUNTS > 0 || Events.getRemainingTickTimeMilis() > MTHooks.TIME) {
	//		MTHooks.COUNTS--;
	//		return TASKS.pollFirst();
	//	}
	//	return null;
	//}

	@Override
	public boolean unbanPos(long pos) {
		return banPos.remove(pos);
	}

	@Override
	public boolean banPos(long pos) {
		boolean ss = banPos.add(pos);
		// System.out.println(BlockPos.fromLong(pos) + " " + ss);
		return ss;
	}

	@Override
	public boolean isPosReady(long pos) {
		return !banPos.contains(pos);
	}

	//public static void pushTask(ITaskRunnable task) {
	//	// if (task instanceof FluidTask) {
	//	// FluidTask t = (FluidTask) task;
	//	// System.out.println(BlockPos.fromLong(t.pos));
	//	// }
	//	// System.out.println(TASKS.add(task));
	//
	//	TASKS.add(task);
	//}
}