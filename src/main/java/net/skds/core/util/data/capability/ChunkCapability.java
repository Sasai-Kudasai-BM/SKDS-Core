package net.skds.core.util.data.capability;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.skds.core.SKDSCore;

public class ChunkCapability {
	
	@CapabilityInject(ChunkCapabilityData.class)
	public static final Capability<ChunkCapabilityData> CAPABILITY = null;
	public static final ResourceLocation KEY = new ResourceLocation(SKDSCore.MOD_ID, "csad");
	
}
