package net.marcoreis.lucene.capitulo_03;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

public class List<T> {

	private int size;
	private T[] data;
	private int position;

	@SuppressWarnings("unchecked")
	public List(int size) {
		this.size = size;
		// data = new T[size];
		data = (T[]) Array.newInstance(
				data.getClass().getComponentType(), size);
	}

	public List(T[] data) {
		this.position = 0;
		this.data = data;
		this.size = data.length;
	}

	public void add(T t) {
		data[position++] = t;
	}

	public T get(int i) {
		if (i >= 0 && i < size) {
			return data[i];
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	public T[] clone() {
		return Arrays.copyOf(data, data.length);
	}

	/**
	 * 1. Because of the recursion, this solution has O(n) complexity
	 * 
	 * @return The reversed array
	 */
	public T[] reverse() {
		int len = this.data.length;
		return reverseData(0, len - 1);
	}

	private T[] reverseData(int i, int j) {
		if (i < j) {
			T temp = data[i];
			data[i] = data[j];
			data[j] = temp;
			reverseData(i + 1, j - 1);
		}
		return data;
	}

	public T[] filter(Predicate<T> pred) {
		int newSize = 0;
		T[] newData = clone();
		for (T t : this.data) {
			if (pred.test(t)) {
				newData[newSize] = t;
				newSize++;
			}
		}
		return Arrays.copyOf(newData, newSize);
	}

	public Collection<T> map(Predicate<T> predicate) {
		T[] newData = clone();
		for (T t : data) {
			
		}
		return null;
	}

	public void foldLeft() {

	}

	public int getSize() {
		return size;
	}

}
