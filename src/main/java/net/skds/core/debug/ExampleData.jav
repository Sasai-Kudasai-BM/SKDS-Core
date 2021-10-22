package net.skds.core.debug;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.LongArrayNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.palette.IPalette;
import net.minecraft.util.palette.IdentityPalette;
import net.minecraft.util.palette.PalettedContainer;
import net.skds.core.api.IChunkSectionData;
import net.skds.core.util.SKDSUtils;
import net.skds.core.util.SKDSUtils.Side;
import net.skds.core.util.data.ChunkSectionAdditionalData;

public class ExampleData implements IChunkSectionData {

	private static final IPalette<FluidState> REGISTRY_PALETTE = new IdentityPalette<>(Fluid.STATE_REGISTRY, Fluids.EMPTY.getDefaultState());

	private final PalettedContainer<FluidState> data;
	private final Side side;
	
	private boolean noData = true;
	private long[] prikolData = new long[64];

	public final ChunkSectionAdditionalData sectionData;

	public ExampleData(ChunkSectionAdditionalData sectionData, Side side) {
		this.sectionData = sectionData;
		this.side = side;
		this.data = new PalettedContainer<>(REGISTRY_PALETTE, Fluid.STATE_REGISTRY, SKDSUtils::readFluidState, SKDSUtils::writeFluidState, Fluids.EMPTY.getDefaultState());
	}

	@Override
	public void deserialize(CompoundNBT nbt) {
		long[] array = nbt.getLongArray("prikol");
		if (array.length == 64) {
			prikolData = array;
			noData = false;
		}

		data.readChunkPalette(nbt.getList("Fluids", 10), nbt.getLongArray("FluidStates"));
	}

	@Override
	public void serialize(CompoundNBT nbt) {
		LongArrayNBT array = new LongArrayNBT(prikolData);
		nbt.put("prikol", array);

		data.writeChunkPalette(nbt, "Fluids", "FluidStates");
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

	public void setPrikol(int x, int y, int z, boolean value) {
		int n = getIndex(x, y, z);
		long a = 1L << (n & 63);
		if (value) {
			prikolData[n / 64] |= a;
		} else {
			prikolData[n / 64] &= ~a;			
		}
	}

	public FluidState setFS(int x, int y, int z, FluidState value) {
		return data.swap(x & 15, y & 15, z & 15, value);
	}

	public FluidState getFS(int x, int y, int z) {
		return data.get(x & 15, y & 15, z & 15);
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
		return (8 * 64) + PacketBuffer.getVarIntSize(64);
	}
}