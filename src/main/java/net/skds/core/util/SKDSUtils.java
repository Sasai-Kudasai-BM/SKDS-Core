package net.skds.core.util;

public class SKDSUtils {

	public static enum Side {
		CLIENT((byte) 1),
		SERVER((byte) 2),
		BOTH((byte) 3);

		public final byte flag;

		Side(byte flag) {
			this.flag = flag;
		}
	}

	public static long microTime() {
		return System.nanoTime() / 1000;
	}

	/*
	private static final long[] BITS = new long[64];
	
	public static byte getByteBit(int i) {
		return (byte) BITS[i];
	}
	
	public static int getIntBit(int i) {
		return (int) BITS[i];
	}
	
	public static long getLingBit(int i) {
		return BITS[i];
	}
	
	public static FluidState readFluidState(CompoundNBT tag) {
		if (!tag.contains("Name", 8)) {
			return Fluids.EMPTY.getDefaultState();
		} else {
			Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(tag.getString("Name")));
			FluidState fluidState = fluid.getDefaultState();
			if (tag.contains("Properties", 10)) {
				CompoundNBT compoundnbt = tag.getCompound("Properties");
				StateContainer<Fluid, FluidState> statecontainer = fluid.getStateContainer();
	
				for (String s : compoundnbt.keySet()) {
					Property<?> property = statecontainer.getProperty(s);
					if (property != null) {
						fluidState = setValueHelper(fluidState, property, s, compoundnbt, tag);
					}
				}
			}
	
			return fluidState;
		}
	}
	
	public static CompoundNBT writeFluidState(FluidState fs) {
		CompoundNBT compoundnbt = new CompoundNBT();
		compoundnbt.putString("Name", ForgeRegistries.FLUIDS.getKey(fs.getFluid()).toString());
		ImmutableMap<Property<?>, Comparable<?>> immutablemap = fs.getValues();
		if (!immutablemap.isEmpty()) {
			CompoundNBT compoundnbt1 = new CompoundNBT();
	
			for (ImmutableMap.Entry<Property<?>, Comparable<?>> entry : immutablemap.entrySet()) {
				Property<?> property = entry.getKey();
				compoundnbt1.putString(property.getName(), getName(property, entry.getValue()));
			}
			compoundnbt.put("Properties", compoundnbt1);
		}
		return compoundnbt;
	}
	
	@SuppressWarnings("unchecked")
	private static <T extends Comparable<T>> String getName(Property<T> p, Comparable<?> c) {
		return p.getName((T) c);
	}
	
	private static <S extends StateHolder<?, S>, T extends Comparable<T>> S setValueHelper(S s, Property<T> p, String name, CompoundNBT nbt, CompoundNBT nbt2) {
		Optional<T> optional = p.parseValue(nbt.getString(name));
		if (optional.isPresent()) {
			return s.with(p, optional.get());
		} else {
			SKDSCore.LOGGER.warn("Unable to read property: {} with value: {} for blockstate: {}", name, nbt.getString(name),
					nbt2.toString());
			return s;
		}
	}
	
	static {
		for (int i = 0; i < 64; i++) {
			BITS[i] = 1L << i;
		}
	}
	*/
}
