package net.skds.core.util.other.collision;

import java.util.stream.Stream;

import net.minecraft.entity.Entity;
import net.minecraft.util.ReuseableStream;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.skds.core.api.collision.IOBBEntity;
import net.skds.core.util.mat.Vec3;

public class OBBBody<E extends Entity & IOBBEntity> {
	public final E entity;
	private Vec3 motion = Vec3.ZERO;

	public OBBBody(E entity) {
		this.entity = entity;
	}

	public void tick() {
		motion = new Vec3(entity.getMotion());
		AxisAlignedBB largeBox = entity.getBoundingBox();

		for (OBB obb : entity.getBoxes()) {
			largeBox = largeBox.union(obb.aabb);
		}

		Vec3 maxMove = getAllowedMovementV3(motion.getMojangD(), largeBox);

		if (maxMove.equals(motion)) {
			move(motion);
		}

	}

	@SuppressWarnings("unused")
	private void setMotion() {
		entity.setMotion(motion.getMojangD());
	}

	private Vec3 getAllowedMovementV3(Vector3d vec, AxisAlignedBB axisalignedbb) {

		ISelectionContext iselectioncontext = ISelectionContext.forEntity(entity);
		VoxelShape voxelshape = entity.world.getWorldBorder().getShape();
		Stream<VoxelShape> stream = VoxelShapes.compare(voxelshape, VoxelShapes.create(axisalignedbb),
				IBooleanFunction.AND) ? Stream.empty() : Stream.of(voxelshape);

		ReuseableStream<VoxelShape> reuseablestream = new ReuseableStream<>(stream);

		Vector3d vector3d = vec.lengthSquared() == 0.0D ? vec
				: collideCustom(vec, axisalignedbb, entity.world, iselectioncontext, reuseablestream);

		return new Vec3(vector3d);
	}
	
	private Vector3d collideCustom(Vector3d vec, AxisAlignedBB collisionBox, World world, ISelectionContext context,
			ReuseableStream<VoxelShape> potentialHits) {
		Stream<VoxelShape> stream = world.getCollisionShapes(entity, collisionBox.expand(vec));

		ReuseableStream<VoxelShape> reuseablestream = new ReuseableStream<>(Stream.concat(potentialHits.createStream(), stream));
		return Entity.collideBoundingBox(vec, collisionBox, reuseablestream);
	}

	private void move(Vec3 move) {
		entity.setPosition(entity.getPosX() + move.x, entity.getPosY() + move.y, entity.getPosZ() + move.z);
	}

	public static double approxAngle(double d) {
		double out = d % 360.0;
		if (out > 180.0D) {
			out -= 360.0;
		} else if (out < -180.0D) {
			out += 360.0;
		}
		return out;
	}
}
