package net.skds.core.util.data;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;
import net.skds.core.api.IChunkData;
import net.skds.core.mixinglue.WorldChinkGlue;
import net.skds.core.util.Class2InstanceMap;
import net.skds.core.util.SKDSUtils;
import net.skds.core.util.SKDSUtils.Side;

public class ChunkData {

	private static RegEntry[] REGISTER = {};

	public final WorldChunk chunk;
	private final ChunkSectionAdditionalData[] data;
	private final Class2InstanceMap<IChunkData> DATA = new Class2InstanceMap<>();
	public final int minY;
	public final int maxY;
	public final int sections;

	public ChunkData(WorldChunk chunk) {
		this.chunk = chunk;
		World w = chunk.getWorld();
		this.minY = w.getBottomY();
		this.maxY = minY + w.getHeight();
		this.sections = w.getHeight();
		this.data = new ChunkSectionAdditionalData[sections];
		//if (!w.isClient) {
		//	createDefault();
		//}
		for (RegEntry entry : REGISTER) {
			if (entry.side == Side.BOTH || (entry.side == Side.SERVER && !w.isClient)
					|| (entry.side == Side.CLIENT && w.isClient)) {
				IChunkData dat = entry.sup.sypply(this, entry.side);
				if (dat != null) {
					DATA.put(dat);
				}
			}
		}
	}

	public void onUnload() {
		for (ChunkSectionAdditionalData c : data) {
			c.onUnload();
		}
		DATA.iterate(d -> d.onUnload());
	}

	//private void createDefault() {
	//	ChunkSection[] sections = chunk.getSections();
	//	for (int i = 0; i < data.length; i++) {
	//		if (sections[i] != null) {
	//			data[i] = getCSAD(i, true);
	//		}
	//	}
	//}

	public void write(PacketByteBuf buffer) {
		for (int i = 0; i < data.length; i++) {
			if (data[i] != null) {
				buffer.writeByte(0x11);
				data[i].write(buffer);
			} else {
				buffer.writeByte(0x22);
			}
		}
		DATA.iterate(d -> d.write(buffer));
	}

	public int getSize() {
		int i = data.length;
		for (ChunkSectionAdditionalData d : data) {
			if (d != null) {
				i += d.getSize();
			}
		}
		AtomicInteger ai = new AtomicInteger(i);
		DATA.iterate(d -> ai.addAndGet(d.getSize()));
		return ai.get();
	}

	public void read(PacketByteBuf buffer) {
		for (int i = 0; i < data.length; i++) {
			if (buffer.readByte() == 0x11) {
				getCSAD(i, true).read(buffer);
			}
		}
		DATA.iterate(d -> d.read(buffer));
	}

	public NbtCompound serializeNBT() {
		NbtCompound nbt = new NbtCompound();
		for (int i = 0; i < data.length; i++) {
			if (data[i] != null) {
				NbtCompound nbt2 = new NbtCompound();
				data[i].serialize(nbt2);
				nbt.put(Integer.toString(i), nbt2);
			}
		}
		DATA.iterate(d -> d.serialize(nbt));
		return nbt;
	}

	public void deserializeNBT(NbtCompound nbt) {
		for (int i = 0; i < data.length; i++) {
			NbtCompound nbt2 = nbt.getCompound(Integer.toString(i));
			if (!nbt2.isEmpty()) {
				getCSAD(i, true).deserialize(nbt2);
			}
		}
		DATA.iterate(d -> d.deserialize(nbt));
	}

	public ChunkSectionAdditionalData getCSAD(int y, boolean create) {
		int index = y + (minY >> 4);
		ChunkSection[] sections = chunk.getSectionArray();

		if (data[index] == null && create && sections[index] != null) {
			data[index] = new ChunkSectionAdditionalData(chunk, index);
		}

		return data[index];
	}

	public int getCSADSize() {
		return data.length;
	}

	public <T extends IChunkData> IChunkData getCData(Class<T> type) {
		return DATA.get(type);
	}

	public static ChunkData getData(WorldChunk chunk) {
		return ((WorldChinkGlue) chunk).getDataSKDS();
	}

	public static boolean apply(WorldChunk chunk, Consumer<ChunkData> cons) {
		ChunkData dat = getData(chunk);
		if (dat != null) {
			cons.accept(dat);
			return true;
		}
		return false;
	}

	public static void register(IChunkDataSupplyer func, SKDSUtils.Side side) {
		RegEntry entry = new RegEntry(func, side);
		REGISTER = Arrays.copyOf(REGISTER, REGISTER.length + 1);
		REGISTER[REGISTER.length - 1] = entry;
	}

	private static class RegEntry {
		public final SKDSUtils.Side side;
		public final IChunkDataSupplyer sup;

		RegEntry(IChunkDataSupplyer func, SKDSUtils.Side side) {
			this.side = side;
			this.sup = func;
		}

	}

	public interface IChunkDataSupplyer {
		public IChunkData sypply(ChunkData cap, SKDSUtils.Side side);
	}
}
