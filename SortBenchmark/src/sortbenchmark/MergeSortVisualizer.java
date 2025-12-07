package sortbenchmark;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class MergeSortVisualizer extends JPanel {

    private int[] array;
    private int[] aux;
    private int highlight1 = -1;
    private int highlight2 = -1;
    private final int delay = 50; // ms

    public MergeSortVisualizer(int size) {
        this.array = new int[size];
        this.aux = new int[size];
        Random rnd = new Random();
        for (int i = 0; i < size; i++) array[i] = rnd.nextInt(300) + 10; // random heights
        setPreferredSize(new Dimension(size * 5, 400));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth() / array.length;
        for (int i = 0; i < array.length; i++) {
            if (i == highlight1 || i == highlight2) g.setColor(Color.RED);
            else g.setColor(Color.BLUE);
            g.fillRect(i * width, getHeight() - array[i], width - 2, array[i]);
        }
    }

    public void startSort() {
        new Thread(() -> {
            // Use SequentialMergeSort with visualization - extends SequentialMergeSort to use its methods
            SequentialMergeSortVisualizable sorter = new SequentialMergeSortVisualizable(this);
            sorter.sort(array);
            highlight1 = highlight2 = -1;
            repaint();
        }).start();
    }

    public void setHighlight(int i, int j) {
        highlight1 = i;
        highlight2 = j;
        SwingUtilities.invokeLater(() -> repaint());
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Sequential Merge Sort with Visualization - extends SequentialMergeSort to use its protected methods
    private static class SequentialMergeSortVisualizable extends SequentialMergeSort {
        private final MergeSortVisualizer visualizer;

        public SequentialMergeSortVisualizable(MergeSortVisualizer visualizer) {
            this.visualizer = visualizer;
        }

        @Override
        public void sort(int[] array) {
            if (array == null || array.length <= 1) return;
            int[] aux = Arrays.copyOf(array, array.length);
            mergeSort(array, aux, 0, array.length - 1);
        }

        // Override mergeSort to add visualization - uses parent's logic
        @Override
        protected void mergeSort(int[] a, int[] aux, int left, int right) {
            if (left >= right) return;
            int mid = left + (right - left) / 2;
            mergeSort(a, aux, left, mid);
            mergeSort(a, aux, mid + 1, right);
            // skip merge if already ordered (same optimization as SequentialMergeSort)
            if (a[mid] <= a[mid + 1]) return;
            merge(a, aux, left, mid, right);
        }

        // Override merge to add visualization - uses parent's logic with visualization
        @Override
        protected void merge(int[] a, int[] aux, int left, int mid, int right) {
            // Use parent's merge logic but add visualization
            System.arraycopy(a, left, aux, left, right - left + 1);
            int i = left;
            int j = mid + 1;
            int k = left;
            while (i <= mid && j <= right) {
                // Visualization: highlight elements being compared
                visualizer.setHighlight(i, j);
                if (aux[i] <= aux[j]) a[k++] = aux[i++];
                else a[k++] = aux[j++];
            }
            while (i <= mid) {
                visualizer.setHighlight(i, -1);
                a[k++] = aux[i++];
            }
            while (j <= right) {
                visualizer.setHighlight(j, -1);
                a[k++] = aux[j++];
            }
            visualizer.setHighlight(-1, -1);
        }
    }

    // Main method to run the GUI independently
    public static void main(String[] args) {
        JFrame frame = new JFrame("Merge Sort Visualization");
        MergeSortVisualizer panel = new MergeSortVisualizer(50); // 50 elements
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start sorting after window is visible
        panel.startSort();
    }
}
