package net.skds.core.debug;

import net.minecraft.nbt.CompoundNBT;
import net.skds.core.api.IChunkSectionData;

public class ExampleData implements IChunkSectionData {
	public byte[] prekol = new byte[4096];

	public void serialize(CompoundNBT nbt) {
		nbt.putInt("lol", 1488);
		nbt.putByteArray("kek", this.prekol);
	}

	public void deserialize(CompoundNBT nbt) {
		byte[] bar = nbt.getByteArray("kek");
		if (bar.length == 4096) {
			this.prekol = bar;
		}
	}

	public void setValue(int x, int y, int z, byte value) {
		this.prekol[getIndex(x, y, z)] = value;
	}

	public byte getValue(int x, int y, int z) {
		return this.prekol[getIndex(x, y, z)];
	}

	private static int getIndex(int x, int y, int z) {
		return (y & 0xF) << 8 | (z & 0xF) << 4 | x & 0xF;
	}
}
