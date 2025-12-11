package sortbenchmark;

import java.util.Arrays;

public class SequentialMergeSort implements SortAlgorithm {

    @Override
    public void sort(int[] array) {
        if (array == null || array.length <= 1) return;
        int[] aux = Arrays.copyOf(array, array.length);
        mergeSort(array, aux, 0, array.length - 1);
    }

    // inclusive bounds
    protected void mergeSort(int[] a, int[] aux, int left, int right) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        mergeSort(a, aux, left, mid);
        mergeSort(a, aux, mid + 1, right);
        // skip merge if already ordered
        if (a[mid] <= a[mid + 1]) return;
        merge(a, aux, left, mid, right);
    }

    protected void merge(int[] a, int[] aux, int left, int mid, int right) {
        // copy to aux
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
