package net.skds.core.util;

import java.util.ArrayList;

import net.skds.core.multithreading.ITaskRunnable;

public class Cycler<T extends ITaskRunnable> {

	/* TODO
	private class CyclerEntry {
		private final T t;
		private final float wei;
		private float timer = 0;
		CyclerEntry(T t) {
			this.t = t;
			this.wei = t.getWeight();
		}
	}
	*/

	private ArrayList<T> list;
	private int index = 0;

	public Cycler() {
		list = new ArrayList<>();
	}

	public boolean isEmpty() {
		return list.isEmpty();
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
		index++;
		return e;
	}
	
	public void removeLast() {	
		int index2 = index - 1;
		if (index2 >= list.size()) {
			index2 = 0;
		}
		if (index2 < 0) {
			index2 = list.size();
		}
		list.remove(index2);
	}
}