package net.skds.core.util;

import java.util.Arrays;
import java.util.function.Consumer;

public class Class2InstanceMap<T> {

	private static final Object[] EMPTY_V = {};
	private static final Class<?>[] EMPTY_K = {};

	private int size = 0;
	private T[] values;
	private Class<T>[] keys;

	@SuppressWarnings("unchecked")
	public Class2InstanceMap() {
		values = (T[]) EMPTY_V;
		keys = (Class<T>[]) EMPTY_K;
	}

	@SuppressWarnings("unchecked")
	public void put(T value) {
		int index = size;
		resize(size + 1);
		values[index] = value;
		keys[index] = (Class<T>) value.getClass();
	}

	@SuppressWarnings("unchecked")
	public <V extends T> V get(Class<V> c) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				if (keys[i] == c) {
					return (V) values[i];
				}
			}
		}
		return null;
	}

	private void resize(int newSize) {
		size = newSize;
		values = Arrays.copyOf(values, size);
		keys = Arrays.copyOf(keys, size);
	}

	@SuppressWarnings("unchecked")
	public void remove(Class<? extends T> key) {
		T[] newValues = (T[]) EMPTY_V;
		Class<T>[] newKeys = (Class<T>[]) EMPTY_K;
		for (int i = 0; i < size; i++) {
			if (keys[i] != key) {
				newValues = Arrays.copyOf(newValues, newValues.length + 1);
				newKeys = Arrays.copyOf(newKeys, newValues.length);
				newValues[newValues.length - 1] = values[i];
				newKeys[newKeys.length - 1] = keys[i];
			}
		}
		values = newValues;
		keys = newKeys;
		size = keys.length;
	}

	public void iterate(Consumer<T> consumer) {
		for (T val : values) {
			consumer.accept(val);
		}
	}

	public int size() {
		return size;
	}

	@Override
	public String toString() {
		String s = "[";
		for (int i = 0; i < size; i++) {
			s += String.format("{%s=%s} ", keys[i], values[i]);
		}
		s += "]";
		return s;
	}

	private class Entry {

		public Class<T> clazz;
		public T instance;

		Entry(Class<T> clazz, T instance) {
			this.clazz = clazz;
			this.instance = instance;
		}
	}
}
