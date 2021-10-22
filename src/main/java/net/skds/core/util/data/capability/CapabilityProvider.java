package net.skds.core.util.data.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityProvider {

	private static <T> IStorage<T> getIS(Class<T> c) {
		return new IStorage<T>() {

			@Override
			public INBT writeNBT(Capability<T> capability, T instance, Direction side) {
				return null;
			}

			@Override
			public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {
			}
		};
	}

	public static void init() {
		registerCapability(ChunkCapabilityData.class);
	}

	public static <T> void registerCapability(Class<T> c) {
		CapabilityManager.INSTANCE.register(c, getIS(c), () -> {
			throw new UnsupportedOperationException("Don't do this shit!");
		});
	}
}