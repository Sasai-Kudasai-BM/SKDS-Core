package net.skds.core.util.data;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.skds.core.api.IChunkSectionData;
import net.skds.core.util.Class2InstanceMap;
import net.skds.core.util.SKDSUtils;
import net.skds.core.util.SKDSUtils.Side;
import net.skds.core.util.data.capability.ChunkCapabilityData;

public class ChunkSectionAdditionalData {
	
	private static RegEntry[] REGISTER = {};

	private final Class2InstanceMap<IChunkSectionData> DATA = new Class2InstanceMap<IChunkSectionData>();

	public final World world;
	public final Chunk chunk;

	public final int secTndex;

	public ChunkSectionAdditionalData(Chunk chunk, int index) {
		this.secTndex = index;
		this.chunk = chunk;
		this.world = chunk.getWorld();
		Side actualSide = world.isRemote ? Side.CLIENT : Side.SERVER;
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

	public void serialize(CompoundNBT nbt) {
		DATA.iterate(d -> d.serialize(nbt));
	}

	public void deserialize(CompoundNBT nbt) {
		DATA.iterate(d -> d.deserialize(nbt));
	}

	public void read(PacketBuffer buff) {
		DATA.iterate(d -> d.read(buff));
	}

	public void write(PacketBuffer buff) {
		DATA.iterate(d -> d.write(buff));
	}

	public boolean isClient() {
		return world.isRemote;
	}
	
	public int getSize() {
		AtomicInteger s = new AtomicInteger();
		DATA.iterate(d -> s.addAndGet(d.getSize()));
		return s.get();
	}

	public static ChunkSectionAdditionalData get(Chunk chunk, int index) {
		Optional<ChunkCapabilityData> op = ChunkCapabilityData.getCap(chunk);
		if (op.isPresent()) {
			ChunkCapabilityData dat = op.get();
			return dat.getCSAD(index, false);
		}
		return null;
	}

	public static <T extends IChunkSectionData> T getTyped(Chunk chunk, int index, Class<T> type) {
		ChunkSectionAdditionalData csad = get(chunk, index);
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