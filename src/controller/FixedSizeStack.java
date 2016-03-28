package controller;

import java.util.LinkedList;

public class FixedSizeStack<T> extends LinkedList<T> {
	private int maxSize;
	private int numElements;
	//investigate dequeue instead of linked list

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
