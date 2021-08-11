package net.skds.core.debug;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.skds.core.util.other.collision.OBB;
import net.skds.core.util.other.collision.OBBBodyEntity;

public class DebugEntity extends OBBBodyEntity {

	public static EntityType<? extends Entity> getForReg(String id) {
		EntityType<? extends Entity> type = EntityType.Builder.create(DebugEntity::new, EntityClassification.MISC)
				.size(1.0F, 1.0F).setTrackingRange(4).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true)
				.build(id);
		return type;
	}

	public DebugEntity(EntityType<? extends Entity> t, World w) {
		super(t, w);
	}

	public DebugEntity(World w, double x, double y, double z) {
		super(RegisterDebug.DEBUG_ENTITY.get(), w);
		setPosition(x, y, z);
	}

	@Override
	public void tick() {
		tickBody();
	}

	@Override
	protected void registerData() {

	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public List<OBB> getBoxes() {
		List<OBB> list = new ArrayList<>();
		list.add(new OBB(getBoundingBox()));
		return list;
	}
}
