package net.skds.core.util.mat;
public class FastMath {

	public static double modInt(double a, int b) {
		int div = ((int) a) / b;
		//System.out.println(div);
		return a - (div * b);
	}

	public static double sin(double x) {
		x = aprSinDegr(x);
		double a;
		if (x > 180) {
			x = 360 - x;
			a = -1;
		} else {
			a = 1;
		}
		a *= 4 * x * (180 - x);
		double b = 40500 - (x * (180 - x));

		return a / b;
	}

	public static double cos(double x) {
		return sin(x + 90);
	}

	public static double aprSinDegr(double x) {
		x = modInt(x, 360);
		if (x < 0) {
			x = 360 + x;
		}
		return x;
	}
}
