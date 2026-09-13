package ru.nsu.berdyugin;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;
import org.junit.jupiter.api.Test;


class MainTest {

    @Test
    void testHeapSort() {

        int[] array = {5, 4, 3, 2, 1};

        Main.heapsort(array);

        int[] expected = {1, 2, 3, 4, 5};

        assertArrayEquals(expected, array);
    }

    @Test
    void testReverseSorted() {

        int[] array = {10, 8, 6, 4, 2};

        Main.heapsort(array);

        int[] expected = {2, 4, 6, 8, 10};

        assertArrayEquals(expected, array);
    }

    @Test
    void testWithDuplicates() {

        int[] array = {4, 2, 4, 1, 2, 4};

        Main.heapsort(array);

        int[] expected = {1, 2, 2, 4, 4, 4};

        assertArrayEquals(expected, array);
    }

    @Test
    void testNegativeNumbers() {

        int[] array = {-5, 3, -1, -10, 7, 0};

        Main.heapsort(array);

        int[] expected = {-10, -5, -1, 0, 3, 7};

        assertArrayEquals(expected, array);
    }

    @Test
    void testLongArray() {

        int[] array = {15, 3, 27, 8, 1, 19, 42, 6, 12, 30, 5, 17, 9, 25, 2, 33, 11, 7, 21, 14};

        Main.heapsort(array);

        int[] expected = {1, 2, 3, 5, 6, 7, 8, 9, 11, 12, 14, 15, 17, 19, 21, 25, 27, 30, 33, 42};

        assertArrayEquals(expected, array);
    }

    @Test
    void testOneElement() {

        int[] array = {7};

        Main.heapsort(array);

        int[] expected = {7};

        assertArrayEquals(expected, array);
    }

    @Test
    void testTwoElements() {

        int[] array = {9, 3};

        Main.heapsort(array);

        int[] expected = {3, 9};

        assertArrayEquals(expected, array);
    }

    @Test
    void testAllEqual() {

        int[] array = {5, 5, 5, 5, 5};

        Main.heapsort(array);

        int[] expected = {5, 5, 5, 5, 5};

        assertArrayEquals(expected, array);
    }

    @Test
    void testMixedNumbers() {

        int[] array = {12, -4, 0, 8, -15, 3};

        Main.heapsort(array);

        int[] expected = {-15, -4, 0, 3, 8, 12};

        assertArrayEquals(expected, array);
    }

    @Test
    void testEmptyArray() {

        int[] array = {};

        Main.heapsort(array);

        int[] expected = {};

        assertArrayEquals(expected, array);
    }

    @Test
    void testOnlyNegativeNumbers() {

        int[] array = {-3, -10, -1, -7, -5};

        Main.heapsort(array);

        int[] expected = {-10, -7, -5, -3, -1};

        assertArrayEquals(expected, array);
    }

    @Test
    void testManyDuplicates() {

        int[] array = {3, 1, 3, 2, 1, 3, 2, 1, 3, 2};

        Main.heapsort(array);

        int[] expected = {1, 1, 1, 2, 2, 2, 3, 3, 3, 3};

        assertArrayEquals(expected, array);
    }

    @Test
    void testRandomArray() {

        Random random = new Random();

        int[] array = new int[100];

        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(201) - 100;
        }

        Main.heapsort(array);

        for (int i = 1; i < array.length; i++) {
            assertTrue(array[i - 1] <= array[i]);
        }
    }

    @Test
    void testExtremeValues() {

        int[] array = {Integer.MAX_VALUE, 0, Integer.MIN_VALUE, 1, -1};

        Main.heapsort(array);

        int[] expected = {Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE};

        assertArrayEquals(expected, array);

    }
}