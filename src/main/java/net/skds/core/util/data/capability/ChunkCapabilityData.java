package net.skds.core.util.data.capability;

import java.util.Optional;
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
import net.skds.core.util.data.ChunkSectionAdditionalData;
import net.skds.core.util.interfaces.IChunkSectionExtended;

public class ChunkCapabilityData implements ICapabilitySerializable<CompoundNBT> {

	public final Chunk chunk;

	private final ChunkSectionAdditionalData[] data;

	public ChunkCapabilityData(Chunk chunk) {
		this.chunk = chunk;
		this.data = new ChunkSectionAdditionalData[chunk.getSections().length];
		World w = chunk.getWorld();
		if (!w.isRemote) {
			createDefault();
		}
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
	}

	public int getSize() {
		int i = data.length;
		for (ChunkSectionAdditionalData d : data) {
			if (d != null) {
				i += d.getSize();
			}
		}
		return i;
	}

	@OnlyIn(Dist.CLIENT)
	public void read(PacketBuffer buffer) {
		for (int i = 0; i < data.length; i++) {
			if (buffer.readByte() == 0x11) {
				getCSAD(i, true).read(buffer);
			}
		}
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
}
