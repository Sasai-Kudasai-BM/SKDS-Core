package net.skds.core.util.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.chunk.ChunkSection;
import net.skds.core.api.IChunkSectionData;
import net.skds.core.util.interfaces.IChunkSectionExtended;

public class ChunkSectionAdditionalData {
	
	private static final Set<Supplier<IChunkSectionData>> REGISTER = new HashSet<>();
	public final ChunkSection section;
	private final Map<Class<? extends IChunkSectionData>, IChunkSectionData> DATA = new HashMap<>(2);

	public ChunkSectionAdditionalData(ChunkSection section) {
		this.section = section;
		for (Supplier<IChunkSectionData> sup : REGISTER) {
			IChunkSectionData dat = sup.get();
			DATA.put(dat.getClass(), dat);
		}
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

	public static boolean register(Supplier<IChunkSectionData> sup) {
		return REGISTER.add(sup);
	}
}