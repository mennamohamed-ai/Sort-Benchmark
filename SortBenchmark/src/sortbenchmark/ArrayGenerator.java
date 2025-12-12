package sortbenchmark;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class ArrayGenerator {
    public static int[] randomArray(int size, int maxVal) {
        if (size <= 0) return new int[0];
        int[] a = new int[size];
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i = 0; i < size; i++) a[i] = rnd.nextInt(Math.max(1, maxVal));
        return a;
    }

    public static int[] randomArray(int size) {
        return randomArray(size, Math.max(10, size * 2));
    }

    public static int[] reverseSortedArray(int size) {
        int[] a = new int[size];
        for (int i = 0; i < size; i++) a[i] = size - i;
        return a;
    }

    public static int[] copy(int[] source) {
        return source == null ? null : Arrays.copyOf(source, source.length);
    }
}

