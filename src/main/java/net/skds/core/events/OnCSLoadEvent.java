package net.skds.core.events;

import net.minecraftforge.eventbus.api.Event;
import net.skds.core.util.data.ChunkSectionAdditionalData;

public class OnCSLoadEvent extends Event {
	public final ChunkSectionAdditionalData data;

	public OnCSLoadEvent(ChunkSectionAdditionalData data) {
		this.data = data;
	}
}
