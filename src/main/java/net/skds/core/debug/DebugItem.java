package net.skds.core.debug;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class DebugItem extends Item {

	public DebugItem(Properties builder) {
		super(builder);
	}

	public static DebugItem getForReg() {
		Properties prop = new Properties().maxStackSize(1);
		return new DebugItem(prop);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (!worldIn.isRemote) {
			click(worldIn, playerIn, handIn);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	private void click(World worldIn, PlayerEntity playerIn, Hand handIn) {
		DebugEntity e = new DebugEntity(worldIn, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ());
		worldIn.addEntity(e);
	}
}
