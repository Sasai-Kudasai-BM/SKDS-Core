package net.skds.core.util.mat;

import net.minecraft.util.math.vector.Quaternion;

public class Quat {
	public static final Quat ONE = new Quat(0.0F, 0.0F, 0.0F, 1.0F);
	private double x;
	private double y;
	private double z;
	private double w;

	public Quat(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Quat(Vec3 axis, double angle, boolean degrees) {
		if (degrees) {
			angle *= (Math.PI / 180D);
		}

		double f = Math.sin(angle / 2.0F);
		this.x = axis.x * f;
		this.y = axis.y * f;
		this.z = axis.z * f;
		this.w = Math.cos(angle / 2.0F);
	}

	public Quat(Quat quaternionIn) {
		this.x = quaternionIn.x;
		this.y = quaternionIn.y;
		this.z = quaternionIn.z;
		this.w = quaternionIn.w;
	}

	public Quat(Quaternion quaternionIn) {
		this.x = quaternionIn.getX();
		this.y = quaternionIn.getY();
		this.z = quaternionIn.getZ();
		this.w = quaternionIn.getW();
	}

	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_) {
			return true;
		} else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
			Quat QuaternionC = (Quat) p_equals_1_;
			if (Double.compare(QuaternionC.x, this.x) != 0) {
				return false;
			} else if (Double.compare(QuaternionC.y, this.y) != 0) {
				return false;
			} else if (Double.compare(QuaternionC.z, this.z) != 0) {
				return false;
			} else {
				return Double.compare(QuaternionC.w, this.w) == 0;
			}
		} else {
			return false;
		}
	}

	public int hashCode() {
		int i = Float.floatToIntBits((float) this.x);
		i = 31 * i + Float.floatToIntBits((float) this.y);
		i = 31 * i + Float.floatToIntBits((float) this.z);
		return 31 * i + Float.floatToIntBits((float) this.w);
	}

	public String toString() {
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("Quat[").append(this.getW()).append(" + ");
		stringbuilder.append(this.getX()).append("i + ");
		stringbuilder.append(this.getY()).append("j + ");
		stringbuilder.append(this.getZ()).append("k]");
		return stringbuilder.toString();
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getZ() {
		return this.z;
	}

	public double getW() {
		return this.w;
	}

	public void multiply(Quat quaternionIn) {
		double f = this.getX();
		double f1 = this.getY();
		double f2 = this.getZ();
		double f3 = this.getW();
		double f4 = quaternionIn.getX();
		double f5 = quaternionIn.getY();
		double f6 = quaternionIn.getZ();
		double f7 = quaternionIn.getW();
		this.x = f3 * f4 + f * f7 + f1 * f6 - f2 * f5;
		this.y = f3 * f5 - f * f6 + f1 * f7 + f2 * f4;
		this.z = f3 * f6 + f * f5 - f1 * f4 + f2 * f7;
		this.w = f3 * f7 - f * f4 - f1 * f5 - f2 * f6;
	}

	public void multiply(float valueIn) {
		this.x *= valueIn;
		this.y *= valueIn;
		this.z *= valueIn;
		this.w *= valueIn;
	}

	public void conjugate() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
	}

	public void set(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}



	//=====================================

	public Quaternion getMojang() {
		return new Quaternion((float) x, (float) y, (float) z, (float) w);
	}
	
}