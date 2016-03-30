package controller;

import java.util.LinkedList;

public class FixedSizeStack<T> extends LinkedList<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int maxSize;
	private int numElements;

	public FixedSizeStack(int size) {
		super();
		numElements = 0;
		this.maxSize = size;
	}

	@Override
	public void push(T item) {
		if (this.numElements == this.maxSize) {
			super.removeLast();
		} else {
			this.numElements++;
		}
		super.addFirst(item);
	}
}
