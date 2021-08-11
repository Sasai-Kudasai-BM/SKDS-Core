package net.skds.core.util.mat;

import net.minecraft.util.math.vector.Matrix3f;

public class Matrix3 {
	protected double m00 = 1.0D;
	protected double m01 = 0.0D;
	protected double m02 = 0.0D;
	protected double m10 = 0.0D;
	protected double m11 = 1.0D;
	protected double m12 = 0.0D;
	protected double m20 = 0.0D;
	protected double m21 = 0.0D;
	protected double m22 = 1.0D;

	public Matrix3() {
	}

	public Matrix3(Quat quaternionIn) {
		double f = quaternionIn.getX();
		double f1 = quaternionIn.getY();
		double f2 = quaternionIn.getZ();
		double f3 = quaternionIn.getW();
		double f4 = 2.0D * f * f;
		double f5 = 2.0D * f1 * f1;
		double f6 = 2.0D * f2 * f2;
		this.m00 = 1.0D - f5 - f6;
		this.m11 = 1.0D - f6 - f4;
		this.m22 = 1.0D - f4 - f5;
		double f7 = f * f1;
		double f8 = f1 * f2;
		double f9 = f2 * f;
		double f10 = f * f3;
		double f11 = f1 * f3;
		double f12 = f2 * f3;
		this.m10 = 2.0D * (f7 + f12);
		this.m01 = 2.0D * (f7 - f12);
		this.m20 = 2.0D * (f9 - f11);
		this.m02 = 2.0D * (f9 + f11);
		this.m21 = 2.0D * (f8 + f10);
		this.m12 = 2.0D * (f8 - f10);
	}

	public void transpose() {
		double f = this.m01;
		this.m01 = this.m10;
		this.m10 = f;
		f = this.m02;
		this.m02 = this.m20;
		this.m20 = f;
		f = this.m12;
		this.m12 = this.m21;
		this.m21 = f;
	}

	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_)
			return true;
		if (p_equals_1_ != null && getClass() == p_equals_1_.getClass()) {
			Matrix3 Matrix3d = (Matrix3) p_equals_1_;
			return (Double.compare(Matrix3d.m00, this.m00) == 0 && Double.compare(Matrix3d.m01, this.m01) == 0
					&& Double.compare(Matrix3d.m02, this.m02) == 0 && Double.compare(Matrix3d.m10, this.m10) == 0
					&& Double.compare(Matrix3d.m11, this.m11) == 0 && Double.compare(Matrix3d.m12, this.m12) == 0
					&& Double.compare(Matrix3d.m20, this.m20) == 0 && Double.compare(Matrix3d.m21, this.m21) == 0
					&& Double.compare(Matrix3d.m22, this.m22) == 0);
		}
		return false;
	}

	public int hashCode() {
		int i = (this.m00 != 0.0D) ? Float.floatToIntBits((float) this.m00) : 0;
		i = 31 * i + ((this.m01 != 0.0D) ? Float.floatToIntBits((float) this.m01) : 0);
		i = 31 * i + ((this.m02 != 0.0D) ? Float.floatToIntBits((float) this.m02) : 0);
		i = 31 * i + ((this.m10 != 0.0D) ? Float.floatToIntBits((float) this.m10) : 0);
		i = 31 * i + ((this.m11 != 0.0D) ? Float.floatToIntBits((float) this.m11) : 0);
		i = 31 * i + ((this.m12 != 0.0D) ? Float.floatToIntBits((float) this.m12) : 0);
		i = 31 * i + ((this.m20 != 0.0D) ? Float.floatToIntBits((float) this.m20) : 0);
		i = 31 * i + ((this.m21 != 0.0D) ? Float.floatToIntBits((float) this.m21) : 0);
		return 31 * i + ((this.m22 != 0.0D) ? Float.floatToIntBits((float) this.m22) : 0);
	}

	public String toString() {
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("Matrix3d:\n");
		stringbuilder.append(this.m00);
		stringbuilder.append(" ");
		stringbuilder.append(this.m01);
		stringbuilder.append(" ");
		stringbuilder.append(this.m02);
		stringbuilder.append("\n");
		stringbuilder.append(this.m10);
		stringbuilder.append(" ");
		stringbuilder.append(this.m11);
		stringbuilder.append(" ");
		stringbuilder.append(this.m12);
		stringbuilder.append("\n");
		stringbuilder.append(this.m20);
		stringbuilder.append(" ");
		stringbuilder.append(this.m21);
		stringbuilder.append(" ");
		stringbuilder.append(this.m22);
		stringbuilder.append("\n");
		return stringbuilder.toString();
	}

	public void setIdentity() {
		this.m00 = 1.0D;
		this.m01 = 0.0D;
		this.m02 = 0.0D;
		this.m10 = 0.0D;
		this.m11 = 1.0D;
		this.m12 = 0.0D;
		this.m20 = 0.0D;
		this.m21 = 0.0D;
		this.m22 = 1.0D;
	}

	public double adjugateAndDet() {
		double f = this.m11 * this.m22 - this.m12 * this.m21;
		double f1 = -(this.m10 * this.m22 - this.m12 * this.m20);
		double f2 = this.m10 * this.m21 - this.m11 * this.m20;
		double f3 = -(this.m01 * this.m22 - this.m02 * this.m21);
		double f4 = this.m00 * this.m22 - this.m02 * this.m20;
		double f5 = -(this.m00 * this.m21 - this.m01 * this.m20);
		double f6 = this.m01 * this.m12 - this.m02 * this.m11;
		double f7 = -(this.m00 * this.m12 - this.m02 * this.m10);
		double f8 = this.m00 * this.m11 - this.m01 * this.m10;
		double f9 = this.m00 * f + this.m01 * f1 + this.m02 * f2;
		this.m00 = f;
		this.m10 = f1;
		this.m20 = f2;
		this.m01 = f3;
		this.m11 = f4;
		this.m21 = f5;
		this.m02 = f6;
		this.m12 = f7;
		this.m22 = f8;
		return f9;
	}

	public boolean invert() {
		double f = adjugateAndDet();
		if (Math.abs(f) > 9.999999974752427E-7D) {
			mul(f);
			return true;
		}
		return false;
	}

	public void mul(Matrix3 m2) {
		double f = this.m00 * m2.m00 + this.m01 * m2.m10 + this.m02 * m2.m20;
		double f1 = this.m00 * m2.m01 + this.m01 * m2.m11 + this.m02 * m2.m21;
		double f2 = this.m00 * m2.m02 + this.m01 * m2.m12 + this.m02 * m2.m22;
		double f3 = this.m10 * m2.m00 + this.m11 * m2.m10 + this.m12 * m2.m20;
		double f4 = this.m10 * m2.m01 + this.m11 * m2.m11 + this.m12 * m2.m21;
		double f5 = this.m10 * m2.m02 + this.m11 * m2.m12 + this.m12 * m2.m22;
		double f6 = this.m20 * m2.m00 + this.m21 * m2.m10 + this.m22 * m2.m20;
		double f7 = this.m20 * m2.m01 + this.m21 * m2.m11 + this.m22 * m2.m21;
		double f8 = this.m20 * m2.m02 + this.m21 * m2.m12 + this.m22 * m2.m22;
		this.m00 = f;
		this.m01 = f1;
		this.m02 = f2;
		this.m10 = f3;
		this.m11 = f4;
		this.m12 = f5;
		this.m20 = f6;
		this.m21 = f7;
		this.m22 = f8;
	}

	public void mul(Quat q) {
		mul(new Matrix3(q));
	}

	public void mul(double scale) {
		this.m00 *= scale;
		this.m01 *= scale;
		this.m02 *= scale;
		this.m10 *= scale;
		this.m11 *= scale;
		this.m12 *= scale;
		this.m20 *= scale;
		this.m21 *= scale;
		this.m22 *= scale;
	}

	public final Vec3 getYPRAngles() {
		double yaw = 0.0D;
		double pitch = 0.0D;
		double roll = 0.0D;

		if (this.m10 > 0.9999D) {
			yaw = Math.atan2(this.m02, this.m22);
			pitch = 1.5707963267948966D;
			roll = 0.0D;
		} else if (this.m10 < -0.9999D) {
			yaw = Math.atan2(this.m02, this.m22);
			pitch = -1.5707963267948966D;
			roll = 0.0D;
		} else {
			yaw = Math.atan2(-this.m20, this.m00);
			pitch = Math.atan2(-this.m12, this.m11);
			roll = Math.asin(this.m10);
		}

		return new Vec3(yaw, pitch, roll);
	}

	public Matrix3f getMojang() {
		Matrix3f m3f = new Matrix3f();
		m3f.func_232605_a_(0, 0, (float) this.m00);
		m3f.func_232605_a_(0, 1, (float) this.m01);
		m3f.func_232605_a_(0, 2, (float) this.m02);
		m3f.func_232605_a_(1, 0, (float) this.m10);
		m3f.func_232605_a_(1, 1, (float) this.m11);
		m3f.func_232605_a_(1, 2, (float) this.m12);
		m3f.func_232605_a_(2, 0, (float) this.m20);
		m3f.func_232605_a_(2, 1, (float) this.m21);
		m3f.func_232605_a_(2, 2, (float) this.m22);
		return m3f;
	}
}
