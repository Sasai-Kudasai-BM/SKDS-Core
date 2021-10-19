package net.skds.core.debug;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.LongArrayNBT;
import net.minecraft.network.PacketBuffer;
import net.skds.core.api.IChunkSectionData;
import net.skds.core.util.SKDSUtils.Side;
import net.skds.core.util.data.ChunkSectionAdditionalData;

public class ExampleData implements IChunkSectionData {

	public final ChunkSectionAdditionalData sectionData;
	private final Side side;

	private long[] prikolData = new long[64];

	public ExampleData(ChunkSectionAdditionalData sectionData, Side side) {
		this.sectionData = sectionData;
		this.side = side;
	}

	@Override
	public void deserialize(CompoundNBT nbt) {
		long[] array = nbt.getLongArray("prikol");
		if (array.length == 64) {
			prikolData = array;
		}
	}

	@Override
	public void serialize(CompoundNBT nbt) {
		LongArrayNBT array = new LongArrayNBT(prikolData);
		nbt.put("prikol", array);
	}

	@Override
	public void onBlockAdded(int x, int y, int z, BlockState newState, BlockState oldState) {
		if (sectionData.isFinished() && newState != oldState && !sectionData.isClient()) {
			setPrikol(x, y, z, true);
		}
	}

	private int getIndex(int x, int y, int z) {
		int n = (x & 15) + ((y & 15) << 4) + ((z & 15) << 8);
		return n;
	}

	public boolean isPrikol(int x, int y, int z) {
		int n = getIndex(x, y, z);
		long l = prikolData[n / 64];
		long a = 1L << (n & 63);
		return (l & a) != 0;
	}

	private void setPrikol(int x, int y, int z, boolean value) {
		int n = getIndex(x, y, z);
		long a = 1L << (n & 63);
		if (value) {
			prikolData[n / 64] |= a;
		} else {
			prikolData[n / 64] &= ~a;			
		}
	}	

	@Override
	public void read(PacketBuffer buff) {
		prikolData = buff.readLongArray(prikolData);
	}

	@Override
	public void write(PacketBuffer buff) {
		buff.writeLongArray(prikolData);
	}

	@Override
	public Side getSide() {
		return side;
	}

	@Override
	public int getSize() {
		return 64 * 8 + PacketBuffer.getVarIntSize(64);
	}
}