package sortbenchmark;

import java.util.Arrays;
import java.util.Comparator;

public class GenericSequentialMergeSort<T> {

    public void sort(T[] array, Comparator<T> comp) {
        if (array == null || array.length <= 1) return;
        T[] aux = Arrays.copyOf(array, array.length);
        mergeSort(array, aux, 0, array.length - 1, comp);
    }

    private void mergeSort(T[] a, T[] aux, int left, int right, Comparator<T> comp) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        mergeSort(a, aux, left, mid, comp);
        mergeSort(a, aux, mid + 1, right, comp);
        if (comp.compare(a[mid], a[mid + 1]) <= 0) return;
        merge(a, aux, left, mid, right, comp);
    }

    private void merge(T[] a, T[] aux, int left, int mid, int right, Comparator<T> comp) {
        System.arraycopy(a, left, aux, left, right - left + 1);
        int i = left, j = mid + 1, k = left;
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
