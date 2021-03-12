package net.skds.core.mixins.custom;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.World;
import net.skds.core.api.IWorldExtended;
import net.skds.core.util.blockupdate.WWSGlobal;

@Mixin(World.class)
public class WorldMixin implements IWorldExtended {

	private WWSGlobal wwsg = null;;

	@Override
	public WWSGlobal getWWS() {
		return wwsg;
	}

	@Override
	public void addWWS() {
		wwsg = new WWSGlobal((World) (Object) this);
	}

}