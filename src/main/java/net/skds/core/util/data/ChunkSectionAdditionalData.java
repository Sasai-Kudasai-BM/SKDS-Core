package net.skds.core.util.data;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraftforge.common.MinecraftForge;
import net.skds.core.api.IChunkSectionData;
import net.skds.core.events.OnCSLoadEvent;
import net.skds.core.util.interfaces.IChunkSectionExtended;

public class ChunkSectionAdditionalData {
	
	public final ChunkSection section;
	private final Map<Class<? extends IChunkSectionData>, IChunkSectionData> DATA = new HashMap<>(2);

	public ChunkSectionAdditionalData(ChunkSection section) {
		this.section = section;
		MinecraftForge.EVENT_BUS.post(new OnCSLoadEvent(this));
	}

	public void addData(IChunkSectionData data) {
		DATA.put(data.getClass(), data);
	}

	@SuppressWarnings("unchecked")
	public <T extends IChunkSectionData> T getData(Class<T> type) {
		return (T) DATA.get(type);
	}

	public void serialize(CompoundNBT nbt) {
		DATA.forEach((k, d) -> {
			d.serialize(nbt);
		});
	}

	public void deserialize(CompoundNBT nbt) {
		DATA.forEach((k, d) -> {
			d.deserialize(nbt);
		});
	}

	public static ChunkSectionAdditionalData getFromSection(ChunkSection section) {
		return ((IChunkSectionExtended) section).getData();
	}
}