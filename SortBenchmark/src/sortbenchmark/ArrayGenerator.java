
package sortbenchmark;

// ArrayGenerator.java
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Helper to generate arrays: random, reverse-sorted, sorted.
 */
public class ArrayGenerator {

    /**
     * Random array with values roughly in [0, maxVal).
     * @param size
     * @param maxVal
     * @return 
     */
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

    /**
     * Already sorted ascending array (0..size-1).
     * @param size
     * @return 
     */
    public static int[] sortedArray(int size) {
        int[] a = new int[size];
        for (int i = 0; i < size; i++) a[i] = i;
        return a;
    }

    /**
     * Reverse sorted (descending) array
     * @param size
     * @return 
     */
    public static int[] reverseSortedArray(int size) {
        int[] a = new int[size];
        for (int i = 0; i < size; i++) a[i] = size - i;
        return a;
    }

    public static int[] copy(int[] source) {
        return source == null ? null : Arrays.copyOf(source, source.length);
    }
}

