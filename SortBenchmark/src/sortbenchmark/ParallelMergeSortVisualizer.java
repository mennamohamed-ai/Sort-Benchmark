package sortbenchmark;

import javax.swing.*;
import java.awt.*;

public class ParallelMergeSortVisualizer extends JPanel implements SortListener {

    private int[] array;
    private final int threshold;
    private int highlight1 = -1;
    private int highlight2 = -1;
    private final int delay = 5;

    public ParallelMergeSortVisualizer(int[] data, int threshold) {
        this.array = data;
        this.threshold = threshold;
        setPreferredSize(new Dimension(array.length * 5, 400));
    }

    @Override
    public void onCompare(int i, int j) {
        setHighlight(i, j);
    }

    @Override
    public void onWrite(int index) {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (array == null || array.length == 0) {
            return;
        }

        int barWidth = Math.max(4, getWidth() / array.length);

        int max = 1;
        for (int v : array) {
            if (v > max) {
                max = v;
            }
        }

        int panelHeight = getHeight();
        double scaleY = (panelHeight - 5) / (double) max;

        for (int i = 0; i < array.length; i++) {
            int h = (int) (array[i] * scaleY);
            if (h < 1) {
                h = 1;
            }
            g.setColor((i == highlight1 || i == highlight2) ? Color.RED : Color.BLUE);
            int x = i * barWidth;
            int y = panelHeight - h;
            g.fillRect(x, y, barWidth - 1, h);
        }
    }

    public void startSort() {
        new Thread(() -> {

            ParallelMergeSort sorter = new ParallelMergeSort(threshold, 0, this);
            sorter.sort(array);
            highlight1 = highlight2 = -1;
            repaint();
        }).start();
    }

    public void setHighlight(int i, int j) {
        highlight1 = i;
        highlight2 = j;
        SwingUtilities.invokeLater(this::repaint);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
    }

}
