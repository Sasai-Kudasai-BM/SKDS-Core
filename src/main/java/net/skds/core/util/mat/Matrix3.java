package net.skds.core.util.mat;


public class Matrix3 {

	public static final Matrix3 SINGLE = new Matrix3();

	public double m00 = 1D;
	public double m01 = 0D;
	public double m02 = 0D;
	public double m10 = 0D;
	public double m11 = 1D;
	public double m12 = 0D;
	public double m20 = 0D;
	public double m21 = 0D;
	public double m22 = 1D;

	public Matrix3() {
	}

	public Matrix3(Vec3[] normals) {
		m00 = normals[0].x;
		m01 = normals[1].x;
		m02 = normals[2].x;
		m10 = normals[0].y;
		m11 = normals[1].y;
		m12 = normals[2].y;
		m20 = normals[0].z;
		m21 = normals[1].z;
		m22 = normals[2].z;
	}


	public Matrix3(Quat quaternionIn) {
		double f = quaternionIn.x;
		double f1 = quaternionIn.y;
		double f2 = quaternionIn.z;
		double f3 = quaternionIn.w;
		double f4 = 2.0 * f * f;
		double f5 = 2.0 * f1 * f1;
		double f6 = 2.0 * f2 * f2;
		this.m00 = 1.0 - f5 - f6;
		this.m11 = 1.0 - f6 - f4;
		this.m22 = 1.0 - f4 - f5;
		double f7 = f * f1;
		double f8 = f1 * f2;
		double f9 = f2 * f;
		double f10 = f * f3;
		double f11 = f1 * f3;
		double f12 = f2 * f3;
		this.m10 = 2.0 * (f7 + f12);
		this.m01 = 2.0 * (f7 - f12);
		this.m20 = 2.0 * (f9 - f11);
		this.m02 = 2.0 * (f9 + f11);
		this.m21 = 2.0 * (f8 + f10);
		this.m12 = 2.0 * (f8 - f10);
	}

	public Matrix3 transpose() {
		double f = this.m01;
		this.m01 = this.m10;
		this.m10 = f;
		f = this.m02;
		this.m02 = this.m20;
		this.m20 = f;
		f = this.m12;
		this.m12 = this.m21;
		this.m21 = f;
		return this;
	}


	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_) {
			return true;
		} else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
			Matrix3 Matrix3d = (Matrix3) p_equals_1_;
			return Double.compare(Matrix3d.m00, this.m00) == 0 && Double.compare(Matrix3d.m01, this.m01) == 0
					&& Double.compare(Matrix3d.m02, this.m02) == 0 && Double.compare(Matrix3d.m10, this.m10) == 0
					&& Double.compare(Matrix3d.m11, this.m11) == 0 && Double.compare(Matrix3d.m12, this.m12) == 0
					&& Double.compare(Matrix3d.m20, this.m20) == 0 && Double.compare(Matrix3d.m21, this.m21) == 0
					&& Double.compare(Matrix3d.m22, this.m22) == 0;
		} else {
			return false;
		}
	}

	public int hashCode() {
		int i = this.m00 != 0.0F ? Float.floatToIntBits((float) this.m00) : 0;
		i = 31 * i + (this.m01 != 0.0F ? Float.floatToIntBits((float) this.m01) : 0);
		i = 31 * i + (this.m02 != 0.0F ? Float.floatToIntBits((float) this.m02) : 0);
		i = 31 * i + (this.m10 != 0.0F ? Float.floatToIntBits((float) this.m10) : 0);
		i = 31 * i + (this.m11 != 0.0F ? Float.floatToIntBits((float) this.m11) : 0);
		i = 31 * i + (this.m12 != 0.0F ? Float.floatToIntBits((float) this.m12) : 0);
		i = 31 * i + (this.m20 != 0.0F ? Float.floatToIntBits((float) this.m20) : 0);
		i = 31 * i + (this.m21 != 0.0F ? Float.floatToIntBits((float) this.m21) : 0);
		return 31 * i + (this.m22 != 0.0F ? Float.floatToIntBits((float) this.m22) : 0);
	}

	public String toString() {
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("Matrix3:\n");
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
		this.m00 = 1.0F;
		this.m01 = 0.0F;
		this.m02 = 0.0F;
		this.m10 = 0.0F;
		this.m11 = 1.0F;
		this.m12 = 0.0F;
		this.m20 = 0.0F;
		this.m21 = 0.0F;
		this.m22 = 1.0F;
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
		double f = this.adjugateAndDet();
		if (Math.abs(f) > 1.0E-6F) {
			this.mul(f);
			return true;
		} else {
			return false;
		}
	}

	public Matrix3 mul(Matrix3 m2) {
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
		return this;
	}

	public Matrix3 mul(Quat q) {
		return this.mul(new Matrix3(q));
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

	//https://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToEuler/index.htm
	public final Vec3 getYPRAngles() {
		double yaw = 0;
		double pitch = 0;
		double roll = 0;
		// Assuming the angles are in radians.
		if (m10 > 0.999999) { // singularity at north pole
			yaw = Math.atan2(m02, m22);
			pitch = Math.PI / 2;
			roll = 0;
		} else if (m10 < -0.999999) { // singularity at south pole
			yaw = Math.atan2(m02, m22);
			pitch = -Math.PI / 2;
			roll = 0;
		} else {
			yaw = Math.atan2(-m20, m00);
			pitch = Math.atan2(-m12, m11);
			roll = Math.asin(m10);
		}

		return new Vec3(yaw, pitch, roll);
	}
	/*
	
		double f = quaternionIn.x;
		double f1 = quaternionIn.y;
		double f2 = quaternionIn.z;
		double f3 = quaternionIn.w;
		double f4 = 2.0 * f * f;
		double f5 = 2.0 * f1 * f1;
		double f6 = 2.0 * f2 * f2;
		this.m00 = 1.0 - f5 - f6;
		this.m11 = 1.0 - f6 - f4;
		double f7 = f * f1;
		double f8 = f1 * f2;
		double f9 = f2 * f;
		double f10 = f * f3;
		double f11 = f1 * f3;
		double f12 = f2 * f3;
		this.m10 = 2.0 * (f7 + f12);
		this.m20 = 2.0 * (f9 - f11);
		this.m12 = 2.0 * (f8 - f10);
	*/

	// =====================================

	public Matrix3 copy() {
		Matrix3 m3 = new Matrix3();
		m3.m00 = this.m00;
		m3.m01 = this.m01;
		m3.m02 = this.m02;
		m3.m10 = this.m10;
		m3.m11 = this.m11;
		m3.m12 = this.m12;
		m3.m20 = this.m20;
		m3.m21 = this.m21;
		m3.m22 = this.m22;
		return m3;
	}

	public Vec3[] asNormals() {
		Vec3[] norms = new Vec3[3];
		norms[0] = new Vec3(m00, m10, m20);
		norms[1] = new Vec3(m01, m11, m21);
		norms[2] = new Vec3(m02, m12, m22);
		return norms;
	}
	
	public Vec3 getZYXAngles(boolean degrees) {
	
		double m20sq = Math.sqrt( 1 - (m20 * m20));

		double a = Math.atan2(m10, m00);
		double b = Math.atan2(m20, m20sq);
		double g = Math.atan2(m21, m22);

		Vec3 vec3 = new Vec3(g, -b, a);

		//System.out.println(m10 / m00);
	
		return degrees ? vec3.scale(180D / Math.PI) : vec3;
	}

}