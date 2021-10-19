package net.skds.core.util;

import net.minecraftforge.fml.common.thread.SidedThreadGroups;

public class SKDSUtils {

	public static boolean isClient() {
		//System.out.println(Thread.currentThread().getThreadGroup() + " " + Thread.currentThread());
		//if (Thread.currentThread().getThreadGroup() != SidedThreadGroups.SERVER && !Thread.currentThread().getName().contains("Render")) {
		//	String s = Arrays.toString(Thread.currentThread().getStackTrace()[0].);
		//	System.out.println(s);
		//}

		return Thread.currentThread().getThreadGroup() != SidedThreadGroups.SERVER;
	}

	public static boolean mergeSides(Side side) {
		if (side == Side.BOTH) {
			return true;
		}
		if (isClient()) {
			return side == Side.CLIENT;
		} else {
			return side == Side.SERVER;
		}
	}

	public static enum Side {
		CLIENT, SERVER, BOTH;
	}
}
