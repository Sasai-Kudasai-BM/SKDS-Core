package net.skds.core.util.data.capability;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.skds.core.api.IChunkData;
import net.skds.core.util.Class2InstanceMap;
import net.skds.core.util.SKDSUtils;
import net.skds.core.util.SKDSUtils.Side;
import net.skds.core.util.data.ChunkSectionAdditionalData;
import net.skds.core.util.interfaces.IChunkSectionExtended;

public class ChunkCapabilityData implements ICapabilitySerializable<CompoundNBT> {

	private static RegEntry[] REGISTER = {};

	public final Chunk chunk;

	private final ChunkSectionAdditionalData[] data;

	private final Class2InstanceMap<IChunkData> DATA = new Class2InstanceMap<>();

	public ChunkCapabilityData(Chunk chunk) {
		this.chunk = chunk;
		this.data = new ChunkSectionAdditionalData[chunk.getSections().length];
		World w = chunk.getWorld();
		if (!w.isRemote) {
			createDefault();
		}
		for (RegEntry entry : REGISTER) {
			if (entry.side == Side.BOTH || (entry.side == Side.SERVER && !w.isRemote) || (entry.side == Side.CLIENT && w.isRemote)) {
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

	private void createDefault() {
		ChunkSection[] sections = chunk.getSections();
		for (int i = 0; i < data.length; i++) {
			if (sections[i] != null) {
				data[i] = getCSAD(i, true);
			}
		}
	}

	public void write(PacketBuffer buffer) {
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

	@OnlyIn(Dist.CLIENT)
	public void read(PacketBuffer buffer) {
		for (int i = 0; i < data.length; i++) {
			if (buffer.readByte() == 0x11) {
				getCSAD(i, true).read(buffer);
			}
		}		
		DATA.iterate(d -> d.read(buffer));
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();
		for (int i = 0; i < data.length; i++) {
			if (data[i] != null) {
				CompoundNBT nbt2 = new CompoundNBT();
				data[i].serialize(nbt2);
				nbt.put(Integer.toString(i), nbt2);
			}
		}
		DATA.iterate(d -> d.serialize(nbt));
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		for (int i = 0; i < data.length; i++) {
			CompoundNBT nbt2 = nbt.getCompound(Integer.toString(i));
			if (!nbt2.isEmpty()) {
				getCSAD(i, true).deserialize(nbt2);
			}
		}
		DATA.iterate(d -> d.deserialize(nbt));
	}

	public ChunkSectionAdditionalData getCSAD(int index, boolean create) {
		ChunkSection[] sections = chunk.getSections();
		if (sections[index] == null && create) {
			sections[index] = new ChunkSection(index << 4);
		}
		if (data[index] == null && create) {
			data[index] = new ChunkSectionAdditionalData(chunk, index);
			IChunkSectionExtended.setData(data[index], sections[index]);
		}
		//System.out.println(index);
		return data[index];
	}

	public int getCSADSize() {
		return data.length;
	}

	public <T extends IChunkData> IChunkData getCData(Class<T> type) {
		return DATA.get(type);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == ChunkCapability.CAPABILITY) {
			return LazyOptional.of(() -> this).cast();
		}
		return LazyOptional.empty();
	}

	public static Optional<ChunkCapabilityData> getCap(Chunk chunk) {
		return chunk.getCapability(ChunkCapability.CAPABILITY).resolve();
	}

	public static boolean apply(Chunk chunk, Consumer<ChunkCapabilityData> cons) {
		Optional<ChunkCapabilityData> op = getCap(chunk);
		if (op.isPresent()) {
			cons.accept(op.get());
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
		public IChunkData sypply(ChunkCapabilityData cap, SKDSUtils.Side side);
	}
}
