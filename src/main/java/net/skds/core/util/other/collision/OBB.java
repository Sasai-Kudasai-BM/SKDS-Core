package net.skds.core.util.other.collision;

import net.minecraft.util.math.AxisAlignedBB;
import net.skds.core.util.mat.Matrix3;
import net.skds.core.util.mat.Vec3;

public class OBB {
	public final Vec3 center;

	public final Vec3[] points;
	public final Vec3[] normals;

	public final AxisAlignedBB aabb;

	public OBB(AxisAlignedBB aabb, Matrix3 matrix) {
		this.center = new Vec3(aabb.getCenter());

		double xs2 = (aabb.maxX - aabb.minX) / 2;
		double ys2 = (aabb.maxY - aabb.minY) / 2;
		double zs2 = (aabb.maxZ - aabb.minZ) / 2;

		Vec3 a = new Vec3(aabb.maxX - aabb.minX, 0, 0).transform(matrix);
		Vec3 b = new Vec3(0, aabb.maxY - aabb.minX, 0).transform(matrix);
		Vec3 c = new Vec3(0, 0, aabb.maxZ - aabb.minZ).transform(matrix);

		Vec3[] points = new Vec3[8];
		points[0] = new Vec3(xs2, ys2, zs2).transform(matrix).add(center);
		points[1] = new Vec3(-xs2, ys2, zs2).transform(matrix).add(center);
		points[2] = new Vec3(-xs2, -ys2, zs2).transform(matrix).add(center);
		points[3] = new Vec3(-xs2, -ys2, -zs2).transform(matrix).add(center);

		points[4] = new Vec3(xs2, ys2, -zs2).transform(matrix).add(center);
		points[5] = new Vec3(xs2, -ys2, -zs2).transform(matrix).add(center);
		points[6] = new Vec3(-xs2, ys2, -zs2).transform(matrix).add(center);
		points[7] = new Vec3(xs2, -ys2, zs2).transform(matrix).add(center);
		this.points = points;

		Vec3[] norms = new Vec3[6];
		norms[0] = a.crossProduct(b).normalize();
		norms[1] = b.crossProduct(c).normalize();
		norms[2] = c.crossProduct(a).normalize();
		norms[3] = norms[0].inverse();
		norms[4] = norms[1].inverse();
		norms[5] = norms[2].inverse();
		normals = norms;
		//for (Vec3 v : points)
		//System.out.println(v);
		this.aabb = getAABB();
	}

	public OBB(AxisAlignedBB aabb) {
		this.center = new Vec3(aabb.getCenter());
		
		Vec3[] points = new Vec3[8];
		points[0] = new Vec3(aabb.maxX, aabb.maxY, aabb.maxZ);
		points[1] = new Vec3(aabb.minX, aabb.maxY, aabb.maxZ);
		points[2] = new Vec3(aabb.minX, aabb.minY, aabb.maxZ);
		points[3] = new Vec3(aabb.minX, aabb.minY, aabb.minZ);

		points[4] = new Vec3(aabb.maxX, aabb.minY, aabb.minZ);
		points[5] = new Vec3(aabb.maxX, aabb.maxY, aabb.minZ);
		points[6] = new Vec3(aabb.minX, aabb.maxY, aabb.minZ);
		points[7] = new Vec3(aabb.maxX, aabb.minY, aabb.maxZ);
		this.points = points;

		Vec3[] norms = new Vec3[6];
		norms[0] = Vec3.ZP;
		norms[1] = Vec3.XP;
		norms[2] = Vec3.YP;
		norms[3] = Vec3.ZN;
		norms[4] = Vec3.XN;
		norms[5] = Vec3.YN;
		normals = norms;
		//for (Vec3 v : points)
		//System.out.println(v);
		//System.out.println(aabb.maxX);
		this.aabb = aabb;
	}

	private AxisAlignedBB getAABB() {
		double minX = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		double minZ = Double.POSITIVE_INFINITY;
		double maxZ = Double.NEGATIVE_INFINITY;

		for (Vec3 point : points) {
			minX = Math.min(minX, point.x);
			minY = Math.min(minY, point.y);
			minZ = Math.min(minZ, point.z);
			maxX = Math.max(maxX, point.x);
			maxY = Math.max(maxY, point.y);
			maxZ = Math.max(maxZ, point.z);
		}

		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}
}