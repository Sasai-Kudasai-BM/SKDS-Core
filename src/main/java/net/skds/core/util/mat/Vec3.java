package net.skds.core.util.mat;

import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class Vec3 {

	public static Vec3 XN = new Vec3(-1.0F, 0.0F, 0.0F);
	public static Vec3 XP = new Vec3(1.0F, 0.0F, 0.0F);
	public static Vec3 YN = new Vec3(0.0F, -1.0F, 0.0F);
	public static Vec3 YP = new Vec3(0.0F, 1.0F, 0.0F);
	public static Vec3 ZN = new Vec3(0.0F, 0.0F, -1.0F);
	public static Vec3 ZP = new Vec3(0.0F, 0.0F, 1.0F);

	public static final Vec3 ZERO = new Vec3(0.0D, 0.0D, 0.0D);
	public static final Vec3 SINGLE = new Vec3(1.0D, 1.0D, 1.0D);
	public final double x;
	public final double y;
	public final double z;

	public Vec3(double xIn, double yIn, double zIn) {
		this.x = xIn;
		this.y = yIn;
		this.z = zIn;
	}

	public Vec3(Vector3d v3d) {
		this.x = v3d.x;
		this.y = v3d.y;
		this.z = v3d.z;
	}

	public Vec3(Vector3f v3f) {
		this.x = v3f.getX();
		this.y = v3f.getY();
		this.z = v3f.getZ();
	}

	public Vector3d to3d() {
		return new Vector3d(x, y, z);
	}
	public Vector3f to3f() {
		return new Vector3f((float) x, (float) y, (float) z);
	}

	public Vec3 transform(Matrix3 matrixIn) {
		double f = this.x;
		double f1 = this.y;
		double f2 = this.z;
		double nx = matrixIn.m00 * f + matrixIn.m01 * f1 + matrixIn.m02 * f2;
		double ny = matrixIn.m10 * f + matrixIn.m11 * f1 + matrixIn.m12 * f2;
		double nz = matrixIn.m20 * f + matrixIn.m21 * f1 + matrixIn.m22 * f2;
		return new Vec3(nx, ny, nz);
	}

	public Vec3 transform(Quat quaternionIn) {
		Quat quaternion = new Quat(quaternionIn);
		quaternion.multiply(new Quat(this.getX(), this.getY(), this.getZ(), 0.0F));
		Quat quaternion1 = new Quat(quaternionIn);
		quaternion1.conjugate();
		quaternion.multiply(quaternion1);
		return new Vec3(quaternion.getX(), quaternion.getY(), quaternion.getZ());
	}

	public Quat rotationDegrees(Double valueIn) {
		return new Quat(this, valueIn, true);
	}

	public Vec3 lerp(Vec3 vectorIn, float pctIn) {
		float f = 1.0F - pctIn;
		double nx = this.x * f + vectorIn.x * pctIn;
		double ny = this.y * f + vectorIn.y * pctIn;
		double nz = this.z * f + vectorIn.z * pctIn;
		return new Vec3(nx, ny, nz);
	}

	public Vec3 subtractReverse(Vec3 vec) {
		return new Vec3(vec.x - this.x, vec.y - this.y, vec.z - this.z);
	}

	public Vec3 normalize() {
		double d0 = (double) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
		return d0 < 1.0E-4D ? ZERO : new Vec3(this.x / d0, this.y / d0, this.z / d0);
	}

	public double dotProduct(Vec3 vec) {
		return this.x * vec.x + this.y * vec.y + this.z * vec.z;
	}

	public Vec3 crossProduct(Vec3 vec) {
		return new Vec3(this.y * vec.z - this.z * vec.y, this.z * vec.x - this.x * vec.z,
				this.x * vec.y - this.y * vec.x);
	}

	public Vec3 subtract(Vec3 vec) {
		return this.subtract(vec.x, vec.y, vec.z);
	}

	public Vec3 subtract(double x, double y, double z) {
		return this.add(-x, -y, -z);
	}

	public Vec3 add(Vec3 vec) {
		return this.add(vec.x, vec.y, vec.z);
	}

	public Vec3 add(double x, double y, double z) {
		return new Vec3(this.x + x, this.y + y, this.z + z);
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
		return new Vec3(this.x * factorX, this.y * factorY, this.z * factorZ);
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
		return new Vec3(d0, d1, d2);
	}

	public Vec3 rotateYaw(float yaw) {
		double f = Math.cos(yaw);
		double f1 = Math.sin(yaw);
		double d0 = this.x * f + this.z * f1;
		double d1 = this.y;
		double d2 = this.z * f - this.x * f1;
		return new Vec3(d0, d1, d2);
	}

	public Vec3 rotateRoll(float roll) {
		double f = Math.cos(roll);
		double f1 = Math.sin(roll);
		double d0 = this.x * f + this.y * f1;
		double d1 = this.y * f - this.x * f1;
		double d2 = this.z;
		return new Vec3(d0, d1, d2);
	}

	public double getCoordinate(Direction.Axis axis) {
		return axis.getCoordinate(this.x, this.y, this.z);
	}

	public final double getX() {
		return this.x;
	}

	public final double getY() {
		return this.y;
	}

	public final double getZ() {
		return this.z;
	}

	public double ProjOn(Vec3 a) {
		return ProjOnNormalized(a.normalize());

	}

	public double ProjOnNormalized(Vec3 a) {		
		return dotProduct(a);
	}

	public Vector3d getMojangD() {
		return new Vector3d(x, y, z);
	}

	public Vector3f getMojangF() {
		return new Vector3f((float) x, (float) y, (float) z);
	}
}