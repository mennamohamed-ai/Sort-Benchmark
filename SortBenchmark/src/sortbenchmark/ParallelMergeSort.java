
package sortbenchmark;

// ParallelMergeSort.java
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Parallel merge sort using Fork/Join. Uses threshold to switch to sequential sorting.
 */
public class ParallelMergeSort implements SortAlgorithm {

    private final ForkJoinPool pool;
    private final int threshold;

    /**
     * @param threshold minimal segment length to keep splitting. When segment length <= threshold,
     *                  sorting is done sequentially.
     * @param parallelism number of worker threads; if <= 0 uses common pool.
     */
    public ParallelMergeSort(int threshold, int parallelism) {
        this.threshold = Math.max(1, threshold);
        if (parallelism > 0) this.pool = new ForkJoinPool(parallelism);
        else this.pool = ForkJoinPool.commonPool();
    }

    public ParallelMergeSort(int threshold) {
        this(threshold, 0);
    }

    @Override
    public void sort(int[] array) {
        if (array == null || array.length <= 1) return;
        int[] aux = Arrays.copyOf(array, array.length);
        pool.invoke(createMergeSortTask(array, aux, 0, array.length - 1, threshold));
    }

    protected MergeSortTask createMergeSortTask(int[] array, int[] aux, int left, int right, int threshold) {
        return new MergeSortTask(array, aux, left, right, threshold);
    }

    protected static class MergeSortTask extends RecursiveAction {
        protected final int[] a;
        protected final int[] aux;
        protected final int left;
        protected final int right;
        protected final int threshold;

        public MergeSortTask(int[] a, int[] aux, int left, int right, int threshold) {
            this.a = a;
            this.aux = aux;
            this.left = left;
            this.right = right;
            this.threshold = threshold;
        }

        @Override
        protected void compute() {
            if (left >= right) return;
            int length = right - left + 1;
            if (length <= threshold) {
                // sequential on small segment
                sequentialMergeSort(a, aux, left, right);
                return;
            }
            int mid = left + (right - left) / 2;
            MergeSortTask leftTask = new MergeSortTask(a, aux, left, mid, threshold);
            MergeSortTask rightTask = new MergeSortTask(a, aux, mid + 1, right, threshold);
            invokeAll(leftTask, rightTask);
            // optimization: if halves already ordered, skip merging
            if (a[mid] <= a[mid + 1]) return;
            merge(a, aux, left, mid, right);
        }

        protected void sequentialMergeSort(int[] a, int[] aux, int left, int right) {
            if (left >= right) return;
            int mid = left + (right - left) / 2;
            sequentialMergeSort(a, aux, left, mid);
            sequentialMergeSort(a, aux, mid + 1, right);
            if (a[mid] <= a[mid + 1]) return;
            merge(a, aux, left, mid, right);
        }

        protected void merge(int[] a, int[] aux, int left, int mid, int right) {
            System.arraycopy(a, left, aux, left, right - left + 1);
            int i = left;
            int j = mid + 1;
            int k = left;
            while (i <= mid && j <= right) {
                if (aux[i] <= aux[j]) a[k++] = aux[i++];
                else a[k++] = aux[j++];
            }
            while (i <= mid) a[k++] = aux[i++];
            while (j <= right) a[k++] = aux[j++];
        }
    }
}

