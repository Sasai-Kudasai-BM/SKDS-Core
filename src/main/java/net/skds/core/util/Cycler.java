package net.skds.core.util;

import java.util.ArrayList;

public class Cycler<T> {

	private ArrayList<T> list;
	private int index = 0;

	public Cycler() {
		list = new ArrayList<>();
	}

	public boolean addEntry(T entry) {
		return list.add(entry);
	}

	public boolean removeEntry(T entry) {
		return list.remove(entry);
	}

	public void clear() {
		list.clear();
	}

	public T next() {
		if (list.isEmpty()) {
			return null;
		}
		if (index >= list.size()) {
			index = 0;
		}
		T e = list.get(index);
		index ++;
		return e;
	}
}