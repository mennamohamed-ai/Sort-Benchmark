package sortbenchmark.generic;

import java.util.Arrays;

public class TestEmployeeSorting {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Employee Sorting Performance Test");
        System.out.println("========================================\n");

        // Pre-defined employee data
        Employee[] employees = {
            new Employee("Ali", 30, 5000),
            new Employee("Sara", 25, 7000),
            new Employee("Mona", 28, 6500),
            new Employee("Omar", 35, 4500),
            new Employee("Ahmed", 32, 8000),
            new Employee("Fatima", 27, 5500),
            new Employee("Hassan", 29, 6000),
            new Employee("Layla", 26, 7500),
            new Employee("Mohamed", 33, 4800),
            new Employee("Nour", 24, 7200),
            new Employee("Youssef", 31, 5800),
            new Employee("Zeinab", 28, 6800),
            new Employee("Khaled", 36, 5200),
            new Employee("Dina", 25, 6400),
            new Employee("Tarek", 34, 5900),
            new Employee("Rania", 27, 7100),
            new Employee("Karim", 29, 5600),
            new Employee("Salma", 26, 6900),
            new Employee("Amr", 32, 5100),
            new Employee("Heba", 28, 7300)
        };
        
        // Comparator to sort by salary (using lambda instead of separate class)
        java.util.Comparator<Employee> comp = (e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary());
        
        System.out.println("Array size: " + employees.length + " employees");
        System.out.println("CPU cores: " + Runtime.getRuntime().availableProcessors() + "\n");

        // Sequential Sort
        Employee[] employeesSeq = Arrays.copyOf(employees, employees.length);
        GenericSequentialMergeSort<Employee> sequentialSorter = new GenericSequentialMergeSort<>();
        
        long startTime = System.nanoTime();
        sequentialSorter.sort(employeesSeq, comp);
        long sequentialTime = System.nanoTime() - startTime;

        // Parallel Sort
        Employee[] employeesPar = Arrays.copyOf(employees, employees.length);
        int threshold = 1000;
        GenericParallelMergeSort<Employee> parallelSorter = new GenericParallelMergeSort<>(threshold, 0);
        
        startTime = System.nanoTime();
        parallelSorter.sort(employeesPar, comp);
        long parallelTime = System.nanoTime() - startTime;

        // Verify correctness
        if (!Arrays.equals(employeesSeq, employeesPar)) {
            System.out.println("ERROR: Results differ!\n");
            return;
        }

        // Performance comparison
        double speedup = (double) sequentialTime / parallelTime;
        
        System.out.println("Sequential Merge Sort: " + formatNano(sequentialTime));
        System.out.println("Parallel Merge Sort:   " + formatNano(parallelTime));
        System.out.println("----------------------------------------");
        System.out.printf("Speedup: %.2fx", speedup);
        
        if (speedup > 1.0) {
            System.out.println(" (Parallel is faster)");
        } else {
            System.out.println(" (Sequential is faster)");
        }
    }


    private static String formatNano(long nanos) {
        if (nanos < 1_000_000) return nanos + " ns";
        long ms = nanos / 1_000_000;
        double sec = nanos / 1_000_000_000.0;
        return ms + " ms (" + String.format("%.3f", sec) + " s)";
    }
}

