package net.skds.core.util.blockupdate;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class BlockUpdataer {

	private static final ConcurrentLinkedQueue<Entity> entitiesToAdd = new ConcurrentLinkedQueue<>();

	public static void tick(boolean in) {

		if (in) {
			addEntities();
		}
	}
	
	public static void onWorldLoad(World w) {
	}

	public static void onWorldUnload(World w) {
		addEntities();
	}	

	private static void addEntities() {
		Entity e;	  
		while ((e = entitiesToAdd.poll()) != null) {
			e.world.addEntity(e);
		}
	}

	public static void addEntity(Entity e) {
		entitiesToAdd.add(e);
	}
}