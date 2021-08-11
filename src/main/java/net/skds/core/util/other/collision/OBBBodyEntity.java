package net.skds.core.util.other.collision;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.skds.core.api.collision.IOBBEntity;
import net.skds.core.util.mat.Vec3;

public abstract class OBBBodyEntity extends Entity implements IOBBEntity {

	private OBBBody<OBBBodyEntity> body;

	private Vec3 momentum = Vec3.SINGLE;
	private Vec3 spin = Vec3.ZERO;
	private Vec3 rotation = Vec3.ZERO;
	private Vec3 com = Vec3.ZERO;

	private double mass = 1D;

	public OBBBodyEntity(EntityType<?> entityTypeIn, World worldIn) {
		super(entityTypeIn, worldIn);
	}

	@Override
	public List<OBB> getBoxes() {
		List<OBB> list = new ArrayList<>();
		list.add(new OBB(getBoundingBox()));
		return list;
	}

	@Override
	public OBBBody<?> getBody() {
		return body;
	}

	@Override
	public Vec3 getRotation() {
		return rotation;
	}

	@Override
	public Vec3 getSpin() {
		return spin;
	}

	@Override
	public Vec3 getMomentum() {
		return momentum;
	}

	@Override
	public Vec3 getCOM() {
		return com;
	}

	@Override
	public double getMass() {
		return mass;
	}

	@Override
	public void setRotation(Vec3 rotation) {
		this.rotation = rotation;
	}

	@Override
	public void setSpin(Vec3 spin) {
		this.spin = spin;
	}

	@Override
	public void setMomentum(Vec3 momentum) {
		this.momentum = momentum;
	}

	@Override
	public void setCOM(Vec3 com) {
		this.com = com;
	}

	@Override
	public void setMass(double mass) {
		this.mass = mass;
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
		if (compound.contains("Mass"))
			mass = compound.getDouble("Mass");

		ListNBT listnbt = compound.getList("Rotation3d", 6);
		rotation = new Vec3(OBBBody.approxAngle(listnbt.getDouble(0)), OBBBody.approxAngle(listnbt.getDouble(1)),
				OBBBody.approxAngle(listnbt.getDouble(2)));

		listnbt = compound.getList("Spin", 6);
		spin = new Vec3(listnbt.getDouble(0), listnbt.getDouble(1), listnbt.getDouble(2));

		listnbt = compound.getList("Momentum", 6);
		momentum = new Vec3(listnbt.getDouble(0), listnbt.getDouble(1), listnbt.getDouble(2));

		listnbt = compound.getList("COM", 6);
		com = new Vec3(listnbt.getDouble(0), listnbt.getDouble(1), listnbt.getDouble(2));

	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		compound.put("Rotation3d", this.newDoubleNBTList(rotation.x, rotation.y, rotation.z));
		compound.put("Spin", this.newDoubleNBTList(spin.x, spin.y, spin.z));
		compound.put("Momentum", this.newDoubleNBTList(momentum.x, momentum.y, momentum.z));
		compound.put("COM", this.newDoubleNBTList(com.x, com.y, com.z));

		compound.putDouble("Mass", mass);
	}

}
