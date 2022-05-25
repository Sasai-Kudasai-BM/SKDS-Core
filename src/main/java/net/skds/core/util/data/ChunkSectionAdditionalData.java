package net.skds.core.util.data;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.skds.core.api.IChunkSectionData;
import net.skds.core.util.Class2InstanceMap;
import net.skds.core.util.SKDSUtils;
import net.skds.core.util.SKDSUtils.Side;

public class ChunkSectionAdditionalData {
	
	private static RegEntry[] REGISTER = {};

	private final Class2InstanceMap<IChunkSectionData> DATA = new Class2InstanceMap<IChunkSectionData>();

	public final World world;
	public final WorldChunk chunk;

	public final int secTndex;

	public ChunkSectionAdditionalData(WorldChunk chunk, int index) {
		this.secTndex = index;
		this.chunk = chunk;
		this.world = chunk.getWorld();
		Side actualSide = world.isClient ? Side.CLIENT : Side.SERVER;
		for (RegEntry entry : REGISTER) {
			if (entry.side == Side.BOTH || (entry.side == actualSide)) {
				IChunkSectionData dat = entry.sup.sypply(this, actualSide);
				if (dat != null) {
					DATA.put(dat);
				}
			}
		}
	}

	public <T extends IChunkSectionData> T getData(Class<T> type) {
		return DATA.get(type);
	}

	public void onUnload() {
		DATA.iterate(d -> d.onUnload());
	}

	public void tick() {
		DATA.iterate(d -> d.tick());
	}

	public void serialize(NbtCompound nbt) {
		DATA.iterate(d -> d.serialize(nbt));
	}

	public void deserialize(NbtCompound nbt) {
		DATA.iterate(d -> d.deserialize(nbt));
	}

	public void read(PacketByteBuf buff) {
		DATA.iterate(d -> d.read(buff));
	}

	public void write(PacketByteBuf buff) {
		DATA.iterate(d -> d.write(buff));
	}

	public boolean isClient() {
		return world.isClient;
	}
	
	public int getSize() {
		AtomicInteger s = new AtomicInteger();
		DATA.iterate(d -> s.addAndGet(d.getPackSize()));
		return s.get();
	}

	public static ChunkSectionAdditionalData get(WorldChunk chunk, int y) {
		ChunkData cd = ChunkData.getData(chunk);
		if (cd != null) {
			return cd.getCSAD(y, false);
		}
		return null;
	}

	public static <T extends IChunkSectionData> T getTyped(WorldChunk chunk, int y, Class<T> type) {
		ChunkSectionAdditionalData csad = get(chunk, y);
		if (csad != null) {
			return csad.getData(type);
		}
		return null;
	}

	public static void register(IChunkSectionDataSupplyer func, SKDSUtils.Side side) {
		RegEntry entry = new RegEntry(func, side);
		REGISTER = Arrays.copyOf(REGISTER, REGISTER.length + 1);
		REGISTER[REGISTER.length - 1] = entry;
	}

	private static class RegEntry {
		public final SKDSUtils.Side side;
		public final IChunkSectionDataSupplyer sup;

		RegEntry(IChunkSectionDataSupplyer func, SKDSUtils.Side side) {
			this.side = side;
			this.sup = func;
		}

	}

	public interface IChunkSectionDataSupplyer {
		public IChunkSectionData sypply(ChunkSectionAdditionalData csad, SKDSUtils.Side side);
	}
}