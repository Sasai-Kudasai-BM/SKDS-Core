package net.skds.core;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.skds.core.api.IWorldExtended;
import net.skds.core.util.blockupdate.BlockUpdataer;
import net.skds.core.util.blockupdate.WWSGlobal;
import net.skds.core.util.configs.UniversalJsonReader;
import net.skds.core.util.data.capability.ChunkCapability;
import net.skds.core.util.data.capability.ChunkCapabilityData;

public class Events {

	private static long inTickTime = System.nanoTime();
	private static int lastTickTime = 0;

	//@SubscribeEvent
	//public void debug(OnCSLoadEvent e) {
	//	e.data.addData(new ExampleData());
	//}

	@SubscribeEvent
	public void onChunkSave(ChunkDataEvent.Save e) {
	}

	@SubscribeEvent
	public void onChunkLoad(ChunkDataEvent.Load e) {
	}

	@SubscribeEvent
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

	@SubscribeEvent
	public void onChunkUnload(ChunkEvent.Unload e) {
		//if (e.getWorld().isRemote()) {
		//	ChunkPos cp = e.getChunk().getPos();
		//	ChunkCapabilityData.WATCHED_C.remove(cp.asLong());
		//}
	}

	@SubscribeEvent
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

	@SubscribeEvent
	public void attachCCap(AttachCapabilitiesEvent<Chunk> e) {
		e.addCapability(ChunkCapability.KEY, new ChunkCapabilityData(e.getObject()));
	}

	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload e) {
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {

		World w = (World) e.getWorld();
		((IWorldExtended) w).addWWS();
		BlockUpdataer.onWorldLoad(w);

	}

	@SubscribeEvent
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

	@SubscribeEvent(priority = EventPriority.HIGHEST)
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

	@SubscribeEvent
	public void addReloadListenerEvent(AddReloadListenerEvent e) {
		UniversalJsonReader.DATA_PACK_RREGISTRIES = e.getDataPackRegistries();
	}

	@SubscribeEvent
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