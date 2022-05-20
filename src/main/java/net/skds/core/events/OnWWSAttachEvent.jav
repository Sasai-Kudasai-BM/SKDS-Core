package net.skds.core.events;

import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event;
import net.skds.core.util.blockupdate.WWSGlobal;

public class OnWWSAttachEvent extends Event {
	private final World world;
	private final WWSGlobal wwsGlobal;

	public OnWWSAttachEvent(World w, WWSGlobal wws) {
		this.world = w;
		this.wwsGlobal = wws;
	}

	public World getWorld() {
		return world;
	}

	public WWSGlobal getWWS() {
		return wwsGlobal;
	}
}