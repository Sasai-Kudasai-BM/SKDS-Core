package net.skds.core.util;

import java.util.Arrays;
import java.util.function.BiConsumer;

public class Object2ObjectMap<K, V> {

	private Object[] values = {};

	public Object2ObjectMap() {
	}

	@SuppressWarnings("unchecked")
	public void put(K key, V value) {
		if (value == null) {
			return;
		}
		for (Object o : values) {
			Entry e = (Entry) o;
			if (e.key == value.getClass()) {
				e.value = value;
				return;
			}
		}
		int index = values.length;
		resize(index + 1);
		values[index] = new Entry(key, value);
	}

	public boolean isEmpty() {
		return values.length == 0;
	}

	@SuppressWarnings("unchecked")
	public int containIndex(K c) {
		for (int i = 0; i < values.length; i++) {
			if (((Entry) values[i]).key == c) {
				return i;
			}
		}
		return -1;
	}

	public boolean contains(K c) {
		return containIndex(c) != -1;
	}

	@SuppressWarnings("unchecked")
	public <T extends V> T get(K c) {
		if (values.length > 0) {
			for (Object o : values) {
				Entry e = (Entry) o;
				if (e.key == c) {
					return (T) e.value;
				}
			}
		}
		return null;
	}

	private void resize(int newSize) {
		values = Arrays.copyOf(values, newSize);
	}

	public void remove(K c) {
		if (values.length > 0) {
			int n = containIndex(c);
			if (n == -1) {
				return;
			}
			Object[] a1 = new Object[values.length - 1];
			int i2 = 0;
			for (int i = 0; i < values.length; i ++) {
				if (i != n) {
					a1[i2] = values[i];
					i2++;
				}
			}
			values = a1;
		}
	}

	@SuppressWarnings("unchecked")
	public void iterate(BiConsumer<K, V> consumer) {
		for (Object o : values) {
			Entry e = (Entry) o;
			consumer.accept(e.key, e.value);
		}
	}

	public int size() {
		return values.length;
	}

	@Override
	public String toString() {
		return Arrays.toString(values);
	}

	private class Entry {

		public K key;
		public V value;

		Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public String toString() {
			return String.format("{%s=%s} ", key, value);
		}
	}
}
