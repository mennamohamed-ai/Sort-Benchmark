package sortbenchmark;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelMergeSort implements SortAlgorithm {

    private final ForkJoinPool pool;
    private final int threshold;
    private final SortListener listener; 

    public ParallelMergeSort(int threshold, int parallelism, SortListener listener) {
        this.threshold = Math.max(1, threshold);
        this.listener = listener;
        if (parallelism > 0) this.pool = new ForkJoinPool(parallelism);
        else this.pool = ForkJoinPool.commonPool();
    }

    public ParallelMergeSort(int threshold, int parallelism) {
        this(threshold, parallelism, null);
    }

    public ParallelMergeSort(int threshold) {
        this(threshold, 0, null);
    }

    @Override
    public void sort(int[] array) {
        if (array == null || array.length <= 1) return;
        int[] aux = Arrays.copyOf(array, array.length);
        pool.invoke(createMergeSortTask(array, aux, 0, array.length - 1, threshold));
    }

    protected MergeSortTask createMergeSortTask(int[] array, int[] aux, int left, int right, int threshold) {
        return new MergeSortTask(array, aux, left, right, threshold, listener);
    }

    protected static class MergeSortTask extends RecursiveAction {
        protected final int[] a;
        protected final int[] aux;
        protected final int left;
        protected final int right;
        protected final int threshold;
        protected final SortListener listener;  

        public MergeSortTask(int[] a, int[] aux, int left, int right, int threshold,
                             SortListener listener) {
            this.a = a;
            this.aux = aux;
            this.left = left;
            this.right = right;
            this.threshold = threshold;
            this.listener = listener;
        }

        @Override
        protected void compute() {
            if (left >= right) return;
            int length = right - left + 1;
            if (length <= threshold) {
                sequentialMergeSort(a, aux, left, right);
                return;
            }
            int mid = left + (right - left) / 2;
            MergeSortTask leftTask  = new MergeSortTask(a, aux, left, mid, threshold, listener);
            MergeSortTask rightTask = new MergeSortTask(a, aux, mid + 1, right, threshold, listener);
            invokeAll(leftTask, rightTask);
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
                if (listener != null) listener.onCompare(i, j);   
                if (aux[i] <= aux[j]) {
                    a[k] = aux[i];
                    if (listener != null) listener.onWrite(k);     
                    k++; i++;
                } else {
                    a[k] = aux[j];
                    if (listener != null) listener.onWrite(k);
                    k++; j++;
                }
            }
            while (i <= mid) {
                a[k] = aux[i];
                if (listener != null) listener.onWrite(k);
                k++; i++;
            }
            while (j <= right) {
                a[k] = aux[j];
                if (listener != null) listener.onWrite(k);
                k++; j++;
            }
        }
    }
}


