package net.skds.skds_core.mixins.custom;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.World;
import net.skds.skds_core.util.Interface.IWorldExtended;
import net.skds.skds_core.util.blockupdate.WWSGlobal;

@Mixin(World.class)
public class WorldMixin implements IWorldExtended {

	private WWSGlobal wwsg = null;

	@Override
	public WWSGlobal getWWS() {
		return wwsg;
	}

	@Override
	public void addWWS(WWSGlobal wws) {
		wwsg = wws;
	}

}