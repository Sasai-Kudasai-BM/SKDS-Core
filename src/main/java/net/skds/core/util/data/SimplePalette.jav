package net.skds.core.util.data;

import java.util.ArrayList;
import java.util.IdentityHashMap;

import net.skds.core.util.SKDSUtils;

public class SimplePalette<T> {

	private final IdentityHashMap<T, Integer> identityMap;

	private int bitBySlot;
	private int size = 1;
	private byte[] data;
	private ArrayList<T> elements = new ArrayList<>(2);
	
	public SimplePalette(int capacity, IdentityHashMap<T, Integer> map) {
		this.identityMap = map;
		this.bitBySlot = getSlotSize(map.size());
		int len = (int) Math.ceil((float) (bitBySlot * capacity) / 8);
		this.data = new byte[len];
	}

	private static int getSlotSize(int values) {
		int k = 2;
		int i = 1;
		for (i = 1; k <= values; i++) {
			k *= 2;
		}
		return i;
	}

	private void checkResize(int newSize) {
		if (newSize > size) {
			resize(size * 2);
		} else if ((float) newSize / size < 0.5f) {			
			resize(size - (size / 3));
		}
	}

	private void resize(int newSize) {
		size = newSize;
		bitBySlot = getSlotSize(size);

		data & 1;

	}

	private int getValue(int index) {
		int bit0 = bitBySlot * index;
		int bit1 = (bitBySlot * index) + bitBySlot;

		int val = 0;
		for (int i = bit0; i <= bit1; i++) {
			val <<= 1;
			val |= getBit(i);
		}
		return val;
	}

	private int setValue(int index, int value) {
		int bit0 = bitBySlot * index;
		int bit1 = (bitBySlot * index) + bitBySlot;

		int val = 0;
		int n = 0;
		for (int i = bit0; i <= bit1; i++) {
			val <<= 1;
			val |= swapBit(i, (value & SKDSUtils.getByteBit(n)) != 0);
			n++;
		}
		return val;
	}

	private byte getBit(int bitIndex) {
		int bit0s = bitIndex % 8;
		int byte0 = bitIndex / 8;
		byte b = (byte) (data[byte0] & SKDSUtils.getByteBit(bit0s));
		return b;
	}

	private byte swapBit(int bitIndex, boolean value) {
		int bit0s = bitIndex % 8;
		int byte0 = bitIndex / 8;
		byte b = (byte) (data[byte0] & SKDSUtils.getByteBit(bit0s));
		if (value) {
			data[byte0] = (byte) (b | (1 << bit0s));
		} else {
			data[byte0] = (byte) (b & ~(1 << bit0s));
		}
		return b;
	}
}
