package controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FixedSizeStackTest {
    public static final int SIZE = 500000;
    FixedSizeStack<Integer> emptyStack;
    FixedSizeStack<Integer> fullStack;

    @Before
    public void setUp() {
        fullStack = new FixedSizeStack<Integer>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            fullStack.push(i);
        }
        emptyStack = new FixedSizeStack<Integer>(SIZE);
    }

    @Test
    public void testDrop() {
        fullStack.push(SIZE);
        assertEquals(fullStack.getLast().intValue(), 1);
    }

    @Test
    public void testAdd() {
        emptyStack.push(SIZE);
        assertEquals(emptyStack.getFirst().intValue(), SIZE);
        fullStack.push(SIZE);
        assertEquals(fullStack.getFirst().intValue(), SIZE);
    }

}
