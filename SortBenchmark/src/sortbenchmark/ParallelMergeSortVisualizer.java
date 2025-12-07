package sortbenchmark;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class ParallelMergeSortVisualizer extends JPanel {

    private int[] array;
    private int[] aux;
    private int highlight1 = -1;
    private int highlight2 = -1;
    private final int delay = 50; // ms
    private final int threshold;

    public ParallelMergeSortVisualizer(int size, int threshold) {
        this.array = new int[size];
        this.aux = new int[size];
        this.threshold = threshold;
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
            // Use ParallelMergeSort with visualization
            ParallelMergeSortVisualizable sorter = new ParallelMergeSortVisualizable(this, threshold);
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

    // Parallel Merge Sort with Visualization - extends ParallelMergeSort to use its protected methods
    private static class ParallelMergeSortVisualizable extends ParallelMergeSort {
        private final ParallelMergeSortVisualizer visualizer;

        public ParallelMergeSortVisualizable(ParallelMergeSortVisualizer visualizer, int threshold) {
            super(threshold, 0); // Use common pool
            this.visualizer = visualizer;
        }

        @Override
        protected MergeSortTask createMergeSortTask(int[] array, int[] aux, int left, int right, int threshold) {
            return new VisualizableMergeSortTask(array, aux, left, right, threshold);
        }

        // VisualizableMergeSortTask - extends MergeSortTask to add visualization
        private class VisualizableMergeSortTask extends MergeSortTask {
            public VisualizableMergeSortTask(int[] a, int[] aux, int left, int right, int threshold) {
                super(a, aux, left, right, threshold);
            }

            @Override
            protected void compute() {
                if (left >= right) return;
                int length = right - left + 1;
                if (length <= threshold) {
                    // sequential on small segment - uses parent's method
                    sequentialMergeSort(a, aux, left, right);
                    return;
                }
                int mid = left + (right - left) / 2;
                VisualizableMergeSortTask leftTask = new VisualizableMergeSortTask(a, aux, left, mid, threshold);
                VisualizableMergeSortTask rightTask = new VisualizableMergeSortTask(a, aux, mid + 1, right, threshold);
                invokeAll(leftTask, rightTask);
                // optimization: if halves already ordered, skip merging
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
                try {
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
                } catch (Exception e) {
                    // Handle interruption
                }
            }
        }
    }

    // Main method to run the GUI independently
    public static void main(String[] args) {
        JFrame frame = new JFrame("Parallel Merge Sort Visualization");
        int size = 50; // number of elements
        int threshold = 10; // threshold for parallelization
        ParallelMergeSortVisualizer panel = new ParallelMergeSortVisualizer(size, threshold);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start sorting after window is visible
        panel.startSort();
    }
}

