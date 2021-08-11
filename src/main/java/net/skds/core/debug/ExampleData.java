package net.skds.core.debug;

import net.minecraft.nbt.CompoundNBT;
import net.skds.core.api.IChunkSectionData;

public class ExampleData implements IChunkSectionData {

	public byte[] prekol = new byte[4096];

	public ExampleData() {

	}

	@Override
	public void serialize(CompoundNBT nbt) {
		nbt.putInt("lol", 1488);
		nbt.putByteArray("kek", prekol);
	}

	@Override
	public void deserialize(CompoundNBT nbt) {
		byte[] bar = nbt.getByteArray("kek");
		if (bar.length == 4096) {
			prekol = bar;
		}
	}

	public void setValue(int x, int y, int z, byte value) {
		prekol[getIndex(x, y, z)] = value;
	}

	public byte getValue(int x, int y, int z) {
		return prekol[getIndex(x, y, z)];
	}

	private static int getIndex(int x, int y, int z) {
		return (y & 15) << 8 | (z & 15) << 4 | (x & 15);
	}
}