
package sortbenchmark;

import javax.swing.*;
import java.awt.*;
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
            try {
                mergeSort(0, array.length - 1);
                highlight1 = highlight2 = -1;
                repaint();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Public function: merge sort for visualization
    public void mergeSort(int left, int right) throws InterruptedException {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        mergeSort(left, mid);
        mergeSort(mid + 1, right);
        merge(left, mid, right);
    }

    private void merge(int left, int mid, int right) throws InterruptedException {
        System.arraycopy(array, left, aux, left, right - left + 1);
        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            highlight1 = i;
            highlight2 = j;
            repaint();
            Thread.sleep(delay);
            if (aux[i] <= aux[j]) array[k++] = aux[i++];
            else array[k++] = aux[j++];
        }
        while (i <= mid) {
            highlight1 = i;
            highlight2 = -1;
            repaint();
            Thread.sleep(delay);
            array[k++] = aux[i++];
        }
        while (j <= right) {
            highlight1 = j;
            highlight2 = -1;
            repaint();
            Thread.sleep(delay);
            array[k++] = aux[j++];
        }
        highlight1 = highlight2 = -1;
        repaint();
        Thread.sleep(delay);
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
