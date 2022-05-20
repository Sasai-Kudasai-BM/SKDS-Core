package net.skds.core.config;

import java.util.Deque;

import com.google.common.collect.Queues;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonConveyor {

	private Deque<JsonElement> conv = Queues.newArrayDeque();
	private JsonElement operating;

	public JsonConveyor(JsonObject json) {
		operating = json;
	}

	public void push(String key) {
		conv.add(operating);
		operating = operating.getAsJsonObject().get(key);
	}

	public void pop() {
		if (conv.isEmpty()) {
			throw new RuntimeException("Stack underflow");
		}
		operating = conv.poll();
	}

	public void index(int i) {
		conv.add(operating);
		operating = operating.getAsJsonArray().get(i);
	}

	public int len() {
		return operating.getAsJsonArray().size();
	}

	public String getString(String key) {
		return operating.getAsJsonObject().get(key).getAsString();
	}

	public byte getByte(String key) {
		return operating.getAsJsonObject().get(key).getAsByte();
	}

	public short getShort(String key) {
		return operating.getAsJsonObject().get(key).getAsShort();
	}

	public int getInt(String key) {
		return operating.getAsJsonObject().get(key).getAsInt();
	}

	public long getLong(String key) {
		return operating.getAsJsonObject().get(key).getAsLong();
	}

	public boolean getBoolean(String key) {
		return operating.getAsJsonObject().get(key).getAsBoolean();
	}

	public float getFloat(String key) {
		return operating.getAsJsonObject().get(key).getAsFloat();
	}

	public double getDouble(String key) {
		return operating.getAsJsonObject().get(key).getAsDouble();
	}

	public String[] getStringArray(String key) {
		JsonArray arr = operating.getAsJsonArray();
		String[] arr2 = new String[arr.size()];
		for (int i = 0; i < arr2.length; i++) {
			arr2[i] = arr.get(i).getAsString();
		}
		return arr2;
	}

	public byte[] getByteArray(String key) {
		JsonArray arr = operating.getAsJsonArray();
		byte[] arr2 = new byte[arr.size()];
		for (int i = 0; i < arr2.length; i++) {
			arr2[i] = arr.get(i).getAsByte();
		}
		return arr2;
	}

	public short[] getShortArray(String key) {
		JsonArray arr = operating.getAsJsonArray();
		short[] arr2 = new short[arr.size()];
		for (int i = 0; i < arr2.length; i++) {
			arr2[i] = arr.get(i).getAsShort();
		}
		return arr2;
	}

	public int[] getIntArray(String key) {
		JsonArray arr = operating.getAsJsonArray();
		int[] arr2 = new int[arr.size()];
		for (int i = 0; i < arr2.length; i++) {
			arr2[i] = arr.get(i).getAsInt();
		}
		return arr2;
	}

	public long[] getLongArray(String key) {
		JsonArray arr = operating.getAsJsonArray();
		long[] arr2 = new long[arr.size()];
		for (int i = 0; i < arr2.length; i++) {
			arr2[i] = arr.get(i).getAsLong();
		}
		return arr2;
	}

	public boolean[] getBooleanArray(String key) {
		JsonArray arr = operating.getAsJsonArray();
		boolean[] arr2 = new boolean[arr.size()];
		for (int i = 0; i < arr2.length; i++) {
			arr2[i] = arr.get(i).getAsBoolean();
		}
		return arr2;
	}

	public float[] getFloatArray(String key) {
		JsonArray arr = operating.getAsJsonArray();
		float[] arr2 = new float[arr.size()];
		for (int i = 0; i < arr2.length; i++) {
			arr2[i] = arr.get(i).getAsFloat();
		}
		return arr2;
	}

	public double[] getDoubleArray(String key) {
		JsonArray arr = operating.getAsJsonArray();
		double[] arr2 = new double[arr.size()];
		for (int i = 0; i < arr2.length; i++) {
			arr2[i] = arr.get(i).getAsDouble();
		}
		return arr2;
	}


	public JsonElement operating() {
		return operating;
	}
}
