package sortbenchmark.generic;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class GenericParallelMergeSort<T> {

    private final ForkJoinPool pool;
    private final int threshold;

    public GenericParallelMergeSort(int threshold, int parallelism) {
        this.threshold = Math.max(1, threshold);
        if (parallelism > 0) this.pool = new ForkJoinPool(parallelism);
        else this.pool = ForkJoinPool.commonPool();
    }

    public GenericParallelMergeSort(int threshold) {
        this(threshold, 0);
    }

    public void sort(T[] array, Comparator<T> comp) {
        if (array == null || array.length <= 1) return;
        @SuppressWarnings("unchecked")
        T[] aux = (T[]) Arrays.copyOf(array, array.length);
        pool.invoke(createMergeSortTask(array, aux, 0, array.length - 1, threshold, comp));
    }

    protected MergeSortTask createMergeSortTask(T[] array, T[] aux, int left, int right, int threshold, Comparator<T> comp) {
        return new MergeSortTask(array, aux, left, right, threshold, comp);
    }

    protected static class MergeSortTask<T> extends RecursiveAction {
        protected final T[] a;
        protected final T[] aux;
        protected final int left;
        protected final int right;
        protected final int threshold;
        protected final Comparator<T> comp;

        public MergeSortTask(T[] a, T[] aux, int left, int right, int threshold, Comparator<T> comp) {
            this.a = a;
            this.aux = aux;
            this.left = left;
            this.right = right;
            this.threshold = threshold;
            this.comp = comp;
        }

        @Override
        protected void compute() {
            if (left >= right) return;
            int length = right - left + 1;
            if (length <= threshold) {
                // sequential on small segment
                sequentialMergeSort(a, aux, left, right, comp);
                return;
            }
            int mid = left + (right - left) / 2;
            MergeSortTask<T> leftTask = new MergeSortTask<>(a, aux, left, mid, threshold, comp);
            MergeSortTask<T> rightTask = new MergeSortTask<>(a, aux, mid + 1, right, threshold, comp);
            invokeAll(leftTask, rightTask);
            // optimization: if halves already ordered, skip merging
            if (comp.compare(a[mid], a[mid + 1]) <= 0) return;
            merge(a, aux, left, mid, right, comp);
        }

        protected void sequentialMergeSort(T[] a, T[] aux, int left, int right, Comparator<T> comp) {
            if (left >= right) return;
            int mid = left + (right - left) / 2;
            sequentialMergeSort(a, aux, left, mid, comp);
            sequentialMergeSort(a, aux, mid + 1, right, comp);
            if (comp.compare(a[mid], a[mid + 1]) <= 0) return;
            merge(a, aux, left, mid, right, comp);
        }

        protected void merge(T[] a, T[] aux, int left, int mid, int right, Comparator<T> comp) {
            System.arraycopy(a, left, aux, left, right - left + 1);
            int i = left;
            int j = mid + 1;
            int k = left;
            while (i <= mid && j <= right) {
                if (comp.compare(aux[i], aux[j]) <= 0)
                    a[k++] = aux[i++];
                else
                    a[k++] = aux[j++];
            }
            while (i <= mid) a[k++] = aux[i++];
            while (j <= right) a[k++] = aux[j++];
        }
    }
}

