package net.skds.core.util.mat;

import net.minecraft.util.math.Vec3d;

public class Vec3 {

	private static final Vec3 XN = new Vec3(-1.0F, 0.0F, 0.0F);
	private static final Vec3 XP = new Vec3(1.0F, 0.0F, 0.0F);
	private static final Vec3 YN = new Vec3(0.0F, -1.0F, 0.0F);
	private static final Vec3 YP = new Vec3(0.0F, 1.0F, 0.0F);
	private static final Vec3 ZN = new Vec3(0.0F, 0.0F, -1.0F);
	private static final Vec3 ZP = new Vec3(0.0F, 0.0F, 1.0F);
	private static final Vec3 SINGLE = new Vec3(1.0D, 1.0D, 1.0D);
	
	public double x;
	public double y;
	public double z;

	public static Vec3 XP() {
		return XP.copy();
	}

	public static Vec3 XN() {
		return XN.copy();
	}

	public static Vec3 YP() {
		return YP.copy();
	}

	public static Vec3 YN() {
		return YN.copy();
	}

	public static Vec3 ZP() {
		return ZP.copy();
	}

	public static Vec3 ZN() {
		return ZN.copy();
	}

	public static Vec3 ZERO() {
		return new Vec3();
	}

	public static Vec3 SINGLE() {
		return SINGLE.copy();
	}

	public Vec3() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Vec3(Vec3d moj) {
		this.x = moj.x;
		this.y = moj.y;
		this.z = moj.z;
	}

	public Vec3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3 transform(Matrix3 matrixIn) {
		double f = this.x;
		double f1 = this.y;
		double f2 = this.z;
		this.x = matrixIn.m00 * f + matrixIn.m01 * f1 + matrixIn.m02 * f2;
		this.y = matrixIn.m10 * f + matrixIn.m11 * f1 + matrixIn.m12 * f2;
		this.z = matrixIn.m20 * f + matrixIn.m21 * f1 + matrixIn.m22 * f2;
		return this;
	}

	//public Vec3 transform(Quat quaternionIn) {
	//	Quat quaternion = new Quat(quaternionIn);
	//	quaternion.multiply(new Quat(this.x, this.y, this.z, 0.0F));
	//	Quat quaternion1 = new Quat(quaternionIn);
	//	quaternion1.conjugate();
	//	quaternion.multiply(quaternion1);
	//	return new Vec3(quaternion.x, quaternion.y, quaternion.z);
	//}

	public Quat rotationDegrees(Double valueIn) {
		return new Quat(this, valueIn, true);
	}

	public Vec3 lerp(Vec3 vectorIn, float pctIn) {
		this.x += vectorIn.x * pctIn;
		this.y += vectorIn.y * pctIn;
		this.z += vectorIn.z * pctIn;
		return this;
	}

	public Vec3 set(Vec3 vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
		return this;
	}
	
	public Vec3 set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vec3 sub(Vec3 vec) {
		this.x -= vec.x;
		this.y -= vec.y;
		this.z -= vec.z;
		return this;
	}

	public Vec3 sub(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	public Vec3 normalize() {
		double d0 = (double) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
		if (d0 < 1E-60) {
			this.x = 0;
			this.y = 1;
			this.z = 0;
			return this;
		}
		this.x /= d0;
		this.y /= d0;
		this.z /= d0;
		return this;
	}

	public static double staticDot(double x, double y, double z, Vec3 vec) {
		return x * vec.x + y * vec.y + z * vec.z;
	}

	public double dot(Vec3 vec) {
		return this.x * vec.x + this.y * vec.y + this.z * vec.z;
	}

	public Vec3 cross(Vec3 vec) {
		double x = this.y * vec.z - this.z * vec.y;
		double y = this.z * vec.x - this.x * vec.z;
		double z = this.x * vec.y - this.y * vec.x;
		this.y = y;
		this.z = z;
		this.x = x;
		return this;
	}

	public Vec3 add(Vec3 vec) {
		return add(vec.x, vec.y, vec.z);
	}

	public Vec3 add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public double distanceTo(Vec3 vec) {
		double d0 = vec.x - this.x;
		double d1 = vec.y - this.y;
		double d2 = vec.z - this.z;
		return Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
	}

	public double squareDistanceTo(Vec3 vec) {
		double d0 = vec.x - this.x;
		double d1 = vec.y - this.y;
		double d2 = vec.z - this.z;
		return d0 * d0 + d1 * d1 + d2 * d2;
	}

	public double squareDistanceTo(double xIn, double yIn, double zIn) {
		double d0 = xIn - this.x;
		double d1 = yIn - this.y;
		double d2 = zIn - this.z;
		return d0 * d0 + d1 * d1 + d2 * d2;
	}

	public Vec3 scale(double factor) {
		return this.mul(factor, factor, factor);
	}

	public Vec3 inverse() {
		return this.scale(-1.0D);
	}

	public Vec3 mul(Vec3 vec) {
		return this.mul(vec.x, vec.y, vec.z);
	}

	public Vec3 mul(double factorX, double factorY, double factorZ) {
		this.x *= factorX;
		this.y *= factorY;
		this.z *= factorZ;
		return this;
	}

	public double length() {
		return (double) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}

	public double lengthSquared() {
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof Vec3)) {
			return false;
		} else {
			Vec3 Vec3d = (Vec3) o;
			if (Double.compare(Vec3d.x, this.x) != 0) {
				return false;
			} else if (Double.compare(Vec3d.y, this.y) != 0) {
				return false;
			} else {
				return Double.compare(Vec3d.z, this.z) == 0;
			}
		}
	}

	public int hashCode() {
		long j = Double.doubleToLongBits(this.x);
		int i = (int) (j ^ j >>> 32);
		j = Double.doubleToLongBits(this.y);
		i = 31 * i + (int) (j ^ j >>> 32);
		j = Double.doubleToLongBits(this.z);
		return 31 * i + (int) (j ^ j >>> 32);
	}

	public String toString() {
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}

	public Vec3 rotatePitch(float pitch) {
		double f = Math.cos(pitch);
		double f1 = Math.sin(pitch);
		double d0 = this.x;
		double d1 = this.y * f + this.z * f1;
		double d2 = this.z * f - this.y * f1;
		this.x = d0;
		this.y = d1;
		this.z = d2;
		return this;
	}

	public Vec3 rotateYaw(float yaw) {
		double f = Math.cos(yaw);
		double f1 = Math.sin(yaw);
		double d0 = this.x * f + this.z * f1;
		double d1 = this.y;
		double d2 = this.z * f - this.x * f1;
		this.x = d0;
		this.y = d1;
		this.z = d2;
		return this;
	}

	public Vec3 rotateRoll(float roll) {
		double f = Math.cos(roll);
		double f1 = Math.sin(roll);
		double d0 = this.x * f + this.y * f1;
		double d1 = this.y * f - this.x * f1;
		double d2 = this.z;
		this.x = d0;
		this.y = d1;
		this.z = d2;
		return this;
	}

	public double ProjOn(Vec3 a) {
		return ProjOnNormalized(a.copy().normalize());

	}

	public double ProjOnNormalized(Vec3 a) {
		return dot(a);
	}

	public Vec3 copy() {
		return new Vec3(x, y, z);
	}

	public Vec3 flip() {
		x = x != 0 ? 1d / x : maxim(x);
		y = y != 0 ? 1d / y : maxim(y);
		z = z != 0 ? 1d / z : maxim(z);
		return this;
	}

	private static double maxim(double d) {
		final double max = Double.MAX_VALUE / 4;
		if (d > 0) {
			return max;
		} else {
			return - max;
		}
	}

	public Vec3 aprZero(double lim) {

		if (Math.abs(x) < lim) {
			x = 0;
		}
		if (Math.abs(y) < lim) {
			y = 0;
		}
		if (Math.abs(z) < lim) {
			z = 0;
		}

		return this;
	}

	public double[] asArray() {
		return new double[]{x, y, z};
	}

	public Vec3d getMoj() {
		return new Vec3d(x, y, z);
	}

}