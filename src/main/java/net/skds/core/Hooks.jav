package net.skds.core;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.skds.core.api.IWorldExtended;
import net.skds.core.config.UniversalJsonReader;
import net.skds.core.util.blockupdate.BlockUpdataer;
import net.skds.core.util.blockupdate.WWSGlobal;
import net.skds.core.util.data.capability.ChunkCapability;
import net.skds.core.util.data.capability.ChunkCapabilityData;

public class Hooks {

	private static long inTickTime = System.nanoTime();
	private static int lastTickTime = 0;

	//
	//public void debug(OnCSLoadEvent e) {
	//	e.data.addData(new ExampleData());
	//}

	
	public void onChunkSave(ChunkDataEvent.Save e) {
	}

	
	public void onChunkLoad(ChunkDataEvent.Load e) {
	}

	
	public void onChunkLoad(ChunkEvent.Load e) {
		//System.out.println(e.getChunk().getPos());
		//if (!e.getWorld().isRemote()) {
		//	Chunk chunk = (Chunk) e.getChunk();
		//	ChunkPos cp = chunk.getPos();
		//	Optional<ChunkCapabilityData> op = ChunkCapabilityData.getCap(chunk);
		//	if (op.isPresent()) {
		//		CSDLoadPacket packet = new CSDLoadPacket(op.get(), cp.x, cp.z);
		//		ServerChunkProvider scp = (ServerChunkProvider) e.getWorld().getChunkProvider();
		//		scp.chunkManager.getTrackingPlayers(cp, false).parallel().forEach(p -> PacketHandler.send(p, packet));
		//	}
		//} else {
		//	Chunk chunk = (Chunk) e.getChunk();
		//	ChunkPos cp = chunk.getPos();
		//	ByteBuf buff;
		//	if ((buff = ChunkCapabilityData.WATCHED_C.get(cp.asLong())) != null) {
		//		ChunkCapabilityData.apply(chunk, d -> d.load(buff));
		//	}
		//}
	}

	
	public void onChunkUnload(ChunkEvent.Unload e) {
		//if (e.getWorld().isRemote()) {
		//	ChunkPos cp = e.getChunk().getPos();
		//	ChunkCapabilityData.WATCHED_C.remove(cp.asLong());
		//}
	}

	
	public void onChunkWatch(ChunkWatchEvent.Watch e) {
		//ChunkPos cp = e.getPos();
		//Chunk chunk = (Chunk) e.getWorld().getChunkProvider().getChunkNow(cp.x, cp.z);
		//if (chunk != null && !chunk.isEmpty()) {
		//	Optional<ChunkCapabilityData> op = ChunkCapabilityData.getCap(chunk);
		//	if (op.isPresent()) {
		//		CSDLoadPacket packet = new CSDLoadPacket(op.get(), cp.x, cp.z);
		//		PacketHandler.send(e.getPlayer(), packet);
		//	}
		//}
	}

	
	public void attachCCap(AttachCapabilitiesEvent<Chunk> e) {
		e.addCapability(ChunkCapability.KEY, new ChunkCapabilityData(e.getObject()));
	}

	
	public void onWorldUnload(WorldEvent.Unload e) {
	}

	
	public void onWorldLoad(WorldEvent.Load e) {

		World w = (World) e.getWorld();
		((IWorldExtended) w).addWWS();
		BlockUpdataer.onWorldLoad(w);

	}

	
	public void tick(WorldTickEvent event) {

		boolean in = event.phase == Phase.START;
		World w = event.world;
		if (in) {
			WWSGlobal wwsg = ((IWorldExtended) w).getWWS();
			wwsg.tickIn();

		}
		if (!in) {
			WWSGlobal wwsg = ((IWorldExtended) w).getWWS();
			wwsg.tickOut();
			lastTickTime = (int) (System.nanoTime() - inTickTime);
		}
	}

	(priority = EventPriority.HIGHEST)
	public void tick(ServerTickEvent event) {
		boolean in = event.phase == Phase.START;
		if (in) {
			inTickTime = System.nanoTime();
		}
		BlockUpdataer.tick(in);
		if (!in) {
			lastTickTime = (int) (System.nanoTime() - inTickTime);
		}
	}

	
	public void addReloadListenerEvent(AddReloadListenerEvent e) {
		UniversalJsonReader.DATA_PACK_RREGISTRIES = e.getDataPackRegistries();
	}

	
	public void onServerStart(FMLServerStartedEvent e) {
		SKDSCore.SERVER = e.getServer();
	}

	public static int getLastTickTime() {
		return lastTickTime;
	}

	public static int getRemainingTickTimeNanos() {
		return 50_000_000 - (int) (System.nanoTime() - inTickTime);
	}

	public static int getRemainingTickTimeMicros() {
		return getRemainingTickTimeNanos() / 1000;
	}

	public static int getRemainingTickTimeMilis() {
		return getRemainingTickTimeNanos() / 1000_000;
	}
}