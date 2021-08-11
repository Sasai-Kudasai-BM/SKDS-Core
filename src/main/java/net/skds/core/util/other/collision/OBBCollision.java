package net.skds.core.util.other.collision;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.util.math.AxisAlignedBB;
import net.skds.core.util.mat.Vec3;

public class OBBCollision {
	
	public static Vec3 OBB2AABBColide(OBB obb, AxisAlignedBB aabb) {
		return OBB2OBBColide(obb, new OBB(aabb));
	}

	public static Vec3 OBB2OBBColide(OBB obb1, OBB obb2) {
		Vec3[] normals = ArrayUtils.addAll(obb1.normals, obb2.normals);
		Vec3[] points1 = obb1.normals;
		Vec3[] points2 = obb2.normals;

		return IntersectionOfProj(points1, points2, normals);
	}

	private static void ProjAxis(Proj proj, Vec3[] points, Vec3 normal) {
		proj.max = points[0].ProjOnNormalized(normal);
		proj.min = proj.max;
		for (Vec3 point : points) {
			double tmp = point.ProjOnNormalized(normal);
			if (tmp > proj.max) {
				proj.max = tmp;
			}
			if (tmp < proj.min) {
				proj.min = tmp;
			}
		}
	}

	private static Vec3 IntersectionOfProj(Vec3[] a, Vec3[] b, Vec3[] normals) {

		Vec3 norm = new Vec3(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

		for (Vec3 normal : normals) {
			Proj projA = new Proj();
			ProjAxis(projA, a, normal);
			Proj projB = new Proj();
			ProjAxis(projB, a, normal);

			double[] points = { projA.min, projA.max, projB.min, projB.max };
			Arrays.sort(points);

			double sum = (projA.max - projA.min) + (projB.max - projB.min);
			double len = Math.abs(points[3] - points[0]);

			if (sum <= len) {
				return Vec3.ZERO;
			}

			double dl = Math.abs(points[2] - points[1]);
			if (dl < norm.length()) {
				norm = normal.scale(dl);
			}
		}
		return norm;
	}

	private static class Proj {
		public double max = 0;
		public double min = 0;
	}
}