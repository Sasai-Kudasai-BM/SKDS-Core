package net.skds.core.util;

import java.util.Arrays;
import java.util.function.Consumer;

public class Class2InstanceMap<T> {

	private Object[] values = {};

	public Class2InstanceMap() {
	}

	@SuppressWarnings("unchecked")
	public void put(T value) {
		if (value == null) {
			return;
		}
		for (Object o : values) {
			Entry e = (Entry) o;
			if (e.clazz == value.getClass()) {
				e.instance = value;
				return;
			}
		}
		int index = values.length;
		resize(index + 1);
		values[index] = new Entry(value);
	}

	public boolean isEmpty() {
		return values.length == 0;
	}

	@SuppressWarnings("unchecked")
	public int containIndex(Class<? extends T> c) {
		for (int i = 0; i < values.length; i++) {
			if (((Entry) values[i]).clazz == c) {
				return i;
			}
		}
		return -1;
	}

	public boolean contains(Class<? extends T> c) {
		return containIndex(c) != -1;
	}

	@SuppressWarnings("unchecked")
	public <V extends T> V get(Class<V> c) {
		if (values.length > 0) {
			for (Object o : values) {
				Entry e = (Entry) o;
				if (e.clazz == c) {
					return (V) e.instance;
				}
			}
		}
		return null;
	}

	private void resize(int newSize) {
		values = Arrays.copyOf(values, newSize);
	}

	public void remove(Class<? extends T> c) {
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
	public void iterate(Consumer<T> consumer) {
		for (Object o : values) {
			Entry e = (Entry) o;
			consumer.accept(e.instance);
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

		public Class<T> clazz;
		public T instance;

		@SuppressWarnings("unchecked")
		Entry(T instance) {
			this.clazz = (Class<T>) instance.getClass();
			this.instance = instance;
		}

		@Override
		public String toString() {
			return String.format("{%s=%s} ", clazz, instance);
		}
	}
}
