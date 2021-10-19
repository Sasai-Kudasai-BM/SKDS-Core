package net.skds.core.util.data;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSection;
import net.skds.core.api.IChunkSectionData;
import net.skds.core.util.Class2InstanceMap;
import net.skds.core.util.SKDSUtils;
import net.skds.core.util.SKDSUtils.Side;
import net.skds.core.util.interfaces.IChunkSectionExtended;

public class ChunkSectionAdditionalData {
	
	private static RegEntry[] REGISTER = {};
	private boolean finished = false;
	private boolean isClient = false;
	public final ChunkSection section;
	//private final Map<Class<? extends IChunkSectionData>, IChunkSectionData> DATA = new HashMap<>(2);
	private final Class2InstanceMap<IChunkSectionData> DATA = new Class2InstanceMap<IChunkSectionData>();
	private World world;

	public ChunkSectionAdditionalData(ChunkSection section) {
		this.section = section;
	}

	public <T extends IChunkSectionData> T getData(Class<T> type) {
		return DATA.get(type);
	}

	public void tick() {
		DATA.iterate(d -> d.tickParallel());
	}

	public void serialize(CompoundNBT nbt) {
		DATA.iterate(d -> d.serialize(nbt));
	}

	public void deserialize(CompoundNBT nbt) {
		DATA.iterate(d -> d.deserialize(nbt));
		//finish();
	}

	public void read(PacketBuffer buff) {
		DATA.iterate(d -> d.read(buff));
	}

	public void write(PacketBuffer buff) {
		DATA.iterate(d -> d.write(buff));
	}

	public void onBlockAdded(int x, int y, int z, BlockState newState, BlockState oldState) {
		DATA.iterate(d -> d.onBlockAdded(x, y, z, newState, oldState));
	}

	public void finish(World world) {
		this.world = world;
		if (world != null) {
			isClient = world.isRemote;
		}
		for (RegEntry entry : REGISTER) {
			if (entry.side == Side.BOTH || (entry.side == Side.SERVER && !isClient) || (entry.side == Side.CLIENT && isClient)) {
				IChunkSectionData dat = entry.sup.sypply(this, entry.side);
				if (dat != null) {
					DATA.put(dat);
				}
			}
		}
		finished = true;
	}

	public boolean isFinished() {
		return finished;
	}

	public boolean isClient() {
		return isClient;
	}

	public World getWorld() {
		return world;
	}

	public int getSize() {
		AtomicInteger s = new AtomicInteger();
		DATA.iterate(d -> s.addAndGet(d.getSize()));
		return s.get();
	}

	public static ChunkSectionAdditionalData getFromSection(ChunkSection section) {
		return ((IChunkSectionExtended) section).getData();
	}

	public static <T extends IChunkSectionData> T getTypedFromSection(ChunkSection section, Class<T> type) {
		ChunkSectionAdditionalData csad = ((IChunkSectionExtended) section).getData();
		return csad.getData(type);
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
		public IChunkSectionData sypply(ChunkSectionAdditionalData csad, SKDSUtils.Side side );
	}
}