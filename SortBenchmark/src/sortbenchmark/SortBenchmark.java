package sortbenchmark;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;

public class SortBenchmark {

    private int size;
    private int threshold;
    private int runs;
    private String pattern;

    // Constructor for GUI
    public SortBenchmark(int size, int threshold, int runs, String pattern) {
        this.size = size;
        this.threshold = threshold;
        this.runs = runs;
        this.pattern = pattern;
    }

    // Default constructor for Console
    public SortBenchmark() {
    }

    // Run benchmark and return results as String (for GUI)
    public String run() {
        StringBuilder sb = new StringBuilder();
        int parallelism = Runtime.getRuntime().availableProcessors();

        sb.append("Running benchmark for size=").append(size)
          .append(", pattern=").append(pattern)
          .append(", threshold=").append(threshold)
          .append(", runs=").append(runs)
          .append("\n");
        sb.append("--------------------------------------\n");

        long seqTotal = 0, parTotal = 0, arrTotal = 0, arrParTotal = 0;
        boolean allCorrect = true;

        for (int run = 1; run <= runs; run++) {
            int[] base = generateBase(size, pattern);

            // Reference using Arrays.sort
            int[] reference = ArrayGenerator.copy(base);
            long tRefS = System.nanoTime();
            Arrays.sort(reference);
            long tRef = System.nanoTime() - tRefS;

            // Sequential
            int[] aSeq = ArrayGenerator.copy(base);
            SortAlgorithm seq = new SequentialMergeSort();
            long tSeqS = System.nanoTime();
            seq.sort(aSeq);
            long tSeq = System.nanoTime() - tSeqS;
            if (!Arrays.equals(aSeq, reference)) allCorrect = false;

            // Parallel
            int[] aPar = ArrayGenerator.copy(base);
            SortAlgorithm par = new ParallelMergeSort(threshold, parallelism);
            long tParS = System.nanoTime();
            par.sort(aPar);
            long tPar = System.nanoTime() - tParS;
            if (!Arrays.equals(aPar, reference)) allCorrect = false;

            // Arrays.sort
            int[] aArr = ArrayGenerator.copy(base);
            long tArrS = System.nanoTime();
            Arrays.sort(aArr);
            long tArr = System.nanoTime() - tArrS;

            // Arrays.parallelSort
            int[] aArrPar = ArrayGenerator.copy(base);
            long tArrParS = System.nanoTime();
            Arrays.parallelSort(aArrPar);
            long tArrPar = System.nanoTime() - tArrParS;

            seqTotal += tSeq;
            parTotal += tPar;
            arrTotal += tArr;
            arrParTotal += tArrPar;

            sb.append(String.format("Run %d: seq=%s | par=%s | arr.sort=%s | arr.pSort=%s%n",
                    run, formatNano(tSeq), formatNano(tPar), formatNano(tArr), formatNano(tArrPar)));
        }

        if (!allCorrect) {
            sb.append("ERROR: One or more algorithms produced incorrect results.\n");
            return sb.toString();
        }

        double seqAvg = seqTotal / (double) runs;
        double parAvg = parTotal / (double) runs;
        double arrAvg = arrTotal / (double) runs;
        double arrParAvg = arrParTotal / (double) runs;
        double speedup = seqAvg / parAvg;

        sb.append("\nSUMMARY:\n");
        sb.append(String.format("Average Sequential: %s%n", formatNano((long) seqAvg)));
        sb.append(String.format("Average Parallel (ForkJoin): %s%n", formatNano((long) parAvg)));
        sb.append(String.format("Average Arrays.sort: %s%n", formatNano((long) arrAvg)));
        sb.append(String.format("Average Arrays.parallelSort: %s%n", formatNano((long) arrParAvg)));
        sb.append(String.format("Speedup (seq/par): %.2fx%n", speedup));
        sb.append("--------------------------------------\n");

        return sb.toString();
    }

    // For Console execution with user input
    public void runConsole() {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        
        System.out.println("-----------------------------------------");
        System.out.println("Parallel Merge Sort Benchmark");
        System.out.println("-----------------------------------------\n");
        
        int parallelism = Runtime.getRuntime().availableProcessors();
        System.out.printf("Detected %d CPU cores%n%n", parallelism);
        
        // Get array size
        System.out.print("Enter array size: ");
        int size = scanner.nextInt();
        
        // Get threshold
        System.out.print("Enter threshold: ");
        int threshold = scanner.nextInt();
        
        // Get number of runs
        System.out.print("Enter number of runs: ");
        int runs = scanner.nextInt();
        
        // Get pattern
        System.out.print("Enter pattern (Random/Reverse): ");
        String pattern = scanner.next().trim();
        
        // Automatically show parallel sort visualization with user's input
        showParallelVisualization(size, threshold);
        
        System.out.println("\n" + "=".repeat(40));
        System.out.println("Running benchmark...");
        System.out.println("=".repeat(40) + "\n");
        
        // Run benchmark
        SortBenchmark sb = new SortBenchmark(size, threshold, runs, pattern);
        System.out.print(sb.run());
        
        scanner.close();
    }
    

    private int[] generateBase(int size, String pattern) {
        if ("Reverse".equalsIgnoreCase(pattern)) return ArrayGenerator.reverseSortedArray(size);
        return ArrayGenerator.randomArray(size);
    }

    // Show parallel merge sort visualization with user's input
    private void showParallelVisualization(int size, int threshold) {
        // Limit size for visualization (too large arrays won't visualize well)
        int vizSize = Math.min(size, 100); // Max 100 elements for visualization
        
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Parallel Merge Sort Visualization - Size: " + size + ", Threshold: " + threshold);
            ParallelMergeSortVisualizer panel = new ParallelMergeSortVisualizer(vizSize, threshold);
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            // Ensure window is not minimized and appears on top
            frame.setExtendedState(JFrame.NORMAL);
            frame.setAlwaysOnTop(true);
            frame.toFront();
            frame.requestFocus();
            frame.setVisible(true);
            
            // Remove always on top after showing
            frame.setAlwaysOnTop(false);
            
            // Start sorting after window is visible
            panel.startSort();
        });
        
        System.out.println("Opening Parallel Merge Sort Visualization window...");
        System.out.println("(Visualization shows " + vizSize + " elements for better performance)\n");
    }

    private String formatNano(long nanos) {
        if (nanos < 1_000_000) return nanos + " ns";
        long ms = TimeUnit.NANOSECONDS.toMillis(nanos);
        double sec = nanos / 1_000_000_000.0;
        return ms + " ms (" + String.format("%.3f", sec) + " s)";
    }
    
    // Main method - choose between GUI and Console
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("gui")) {
            // Run GUI mode
            GUI.main(args);
        } else {
            // Run console with user input (default)
            new SortBenchmark().runConsole();
        }
    }
}



