package sortbenchmark;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class SortBenchmark {

    private int size;
    private int threshold;
    private int runs;
    private String pattern;

    private static final int[] DEFAULT_SIZES = {10_000, 100_000, 1_000_000};
    private static final int DEFAULT_RUNS = 5;

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

    // For Console execution
    public void runConsole() {
        int threshold = 10_000;
        int parallelism = Runtime.getRuntime().availableProcessors();

        System.out.println("Parallel Merge Sort Benchmark");
        System.out.printf("Default threshold=%d, parallelism=%d cores%n%n", threshold, parallelism);

        for (int size : DEFAULT_SIZES) {
            System.out.println("=== Array Size: " + size + " ===");
            runForPatternConsole(size, "Random", threshold, parallelism);
            runForPatternConsole(size, "Reverse", threshold, parallelism);
            System.out.println();
        }
    }

    private void runForPatternConsole(int size, String pattern, int threshold, int parallelism) {
        SortBenchmark sb = new SortBenchmark(size, threshold, DEFAULT_RUNS, pattern);
        System.out.print(sb.run());
    }

    private int[] generateBase(int size, String pattern) {
        if ("Reverse".equalsIgnoreCase(pattern)) return ArrayGenerator.reverseSortedArray(size);
        if ("Sorted".equalsIgnoreCase(pattern)) return ArrayGenerator.sortedArray(size);
        return ArrayGenerator.randomArray(size);
    }

    private String formatNano(long nanos) {
        if (nanos < 1_000_000) return nanos + " ns";
        long ms = TimeUnit.NANOSECONDS.toMillis(nanos);
        double sec = nanos / 1_000_000_000.0;
        return ms + " ms (" + String.format("%.3f", sec) + " s)";
    }

    public void testSortingEmployees() {
    System.out.println("\n--- Employee Sorting  ---");

    Employee[] employees = {
        new Employee("Ali", 30, 5000),
        new Employee("Sara", 25, 7000),
        new Employee("Mona", 28, 6500),
        new Employee("Omar", 35, 4500)
    };

    System.out.println("Before sorting:");
    for (Employee e : employees) System.out.println(e);

    GenericSequentialMergeSort<Employee> sorter = new GenericSequentialMergeSort<>();
    sorter.sort(employees, new EmployeeSalaryComparator());

    System.out.println("\nAfter sorting by salary:");
    for (Employee e : employees) System.out.println(e);
}

    // Main method for console
    public static void main(String[] args) {
        new SortBenchmark().runConsole();
        new SortBenchmark().testSortingEmployees();
    }
}



