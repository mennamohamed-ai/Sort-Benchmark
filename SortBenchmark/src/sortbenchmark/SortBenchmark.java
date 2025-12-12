package sortbenchmark;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class SortBenchmark {

    private int size;
    private int threshold;
    private int runs;
    private String pattern;
    private int[] base;
    private ParallelMergeSortVisualizer visualizer;

    // Constructor for GUI
    public SortBenchmark(int size, int threshold, int runs, String pattern,
            int[] base, ParallelMergeSortVisualizer visualizer) {
        this.size = size;
        this.threshold = threshold;
        this.runs = runs;
        this.pattern = pattern;
        this.base = base;
        this.visualizer = visualizer;
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
            int[] baseRun = ArrayGenerator.copy(base);

            // Reference using Arrays.sort
            int[] reference = ArrayGenerator.copy(baseRun);
            long tRefS = System.nanoTime();
            Arrays.sort(reference);
            long tRef = System.nanoTime() - tRefS;

            // Sequential
            int[] aSeq = ArrayGenerator.copy(baseRun);
            SortAlgorithm seq = new SequentialMergeSort();
            long tSeqS = System.nanoTime();
            seq.sort(aSeq);
            long tSeq = System.nanoTime() - tSeqS;
            if (!Arrays.equals(aSeq, reference)) {
                allCorrect = false;
            }
         
            // Parallel
            int[] aPar;
            if (run == 1 && visualizer != null) {
                aPar = base;
            } else {
                aPar = ArrayGenerator.copy(baseRun);
            }

            SortAlgorithm par = (visualizer != null)
                    ? new ParallelMergeSort(threshold, parallelism, visualizer)
                    : new ParallelMergeSort(threshold, parallelism);

            long tParS = System.nanoTime();
            par.sort(aPar);
            long tPar = System.nanoTime() - tParS;
            if (!Arrays.equals(aPar, reference)) {
                allCorrect = false;
            }

            // Arrays.sort
            int[] aArr = ArrayGenerator.copy(baseRun);
            long tArrS = System.nanoTime();
            Arrays.sort(aArr);
            long tArr = System.nanoTime() - tArrS;

            // Arrays.parallelSort
            int[] aArrPar = ArrayGenerator.copy(baseRun);
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

    private String formatNano(long nanos) {
        if (nanos < 1_000_000) {
            return nanos + " ns";
        }
        long ms = TimeUnit.NANOSECONDS.toMillis(nanos);
        double sec = nanos / 1_000_000_000.0;
        return ms + " ms (" + String.format("%.3f", sec) + " s)";
    }

   
    public static void main(String[] args) {

    }
}
