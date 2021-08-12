package net.skds.core;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.skds.core.api.IWorldExtended;
import net.skds.core.util.blockupdate.BlockUpdataer;
import net.skds.core.util.blockupdate.WWSGlobal;
import net.skds.core.util.configs.UniversalJsonReader;
import net.skds.core.util.data.ChunkSectionAdditionalData;

public class Events {

	private static long inTickTime = System.nanoTime();
	private static int lastTickTime = 0;

	//@SubscribeEvent
	//public void debug(OnCSLoadEvent e) {
	//	e.data.addData(new ExampleData());
	//}

	@SubscribeEvent
	public void onChunkSave(ChunkDataEvent.Save e) {
		CompoundNBT nbt = e.getData();
		CompoundNBT list = nbt.getCompound("SKDSCSDL");
		for (ChunkSection cs : e.getChunk().getSections()) {
			if (cs == null) {
				continue;
			}
			String n = (cs.getYLocation() >> 4) + "";
			CompoundNBT nbt2 = list.getCompound(n);
			ChunkSectionAdditionalData.getFromSection(cs).serialize(nbt2);
			list.put(n, nbt2);
		}
		nbt.put("SKDSCSDL", list);
	}

	@SubscribeEvent
	public void onChunkLoad(ChunkDataEvent.Load e) {
		CompoundNBT nbt = e.getData();
		CompoundNBT list = nbt.getCompound("SKDSCSDL");
		for (ChunkSection cs : e.getChunk().getSections()) {
			if (cs == null) {
				continue;
			}
			String n = (cs.getYLocation() >> 4) + "";
			ChunkSectionAdditionalData.getFromSection(cs).deserialize(list.getCompound(n));
		}
	}

	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload e) {

		World w = (World) e.getWorld();
		WWSGlobal wwsg = ((IWorldExtended) w).getWWS();
		wwsg.unloadWorld(w);
		if (!w.isRemote) {
			BlockUpdataer.onWorldUnload((ServerWorld) e.getWorld());
		}
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {

		World w = (World) e.getWorld();
		((IWorldExtended) w).addWWS();
		if (!w.isRemote) {
			BlockUpdataer.onWorldLoad((ServerWorld) w);
		}
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