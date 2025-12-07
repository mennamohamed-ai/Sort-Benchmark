# Sort Benchmark - Parallel Merge Sort with Fork/Join

A comprehensive Java application for benchmarking and visualizing merge sort algorithms, featuring both sequential and parallel implementations using Java's Fork/Join framework.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Components](#components)
- [Usage](#usage)
- [Algorithms](#algorithms)
- [Performance Benchmarking](#performance-benchmarking)
- [Requirements](#requirements)
- [Build & Run](#build--run)

## 🎯 Overview

This project provides a complete benchmarking suite for comparing the performance of different merge sort implementations:
- **Sequential Merge Sort**: Traditional single-threaded implementation
- **Parallel Merge Sort**: Multi-threaded implementation using Fork/Join framework
- **Java Arrays.sort()**: Built-in sorting algorithm
- **Java Arrays.parallelSort()**: Built-in parallel sorting algorithm

The application includes both a graphical user interface (GUI) and console-based execution modes, along with a visual merge sort demonstration.

## ✨ Features

- **Performance Benchmarking**: Compare execution times of multiple sorting algorithms
- **Configurable Parameters**: Adjustable array size, threshold, number of runs, and data patterns
- **Graphical User Interface**: User-friendly Swing-based GUI for running benchmarks
- **Console Mode**: Command-line interface for automated testing
- **Visualization**: Animated merge sort visualizer showing the sorting process
- **Generic Sorting**: Support for sorting custom objects (e.g., Employee objects)
- **Multiple Data Patterns**: Test with random, sorted, and reverse-sorted arrays
- **Statistical Analysis**: Average execution times and speedup calculations

## 📁 Project Structure

```
SortBenchmark/
├── src/
│   └── sortbenchmark/
│       ├── SortBenchmark.java              # Main benchmark orchestrator
│       ├── GUI.java                        # Swing GUI for benchmarks
│       ├── SortAlgorithm.java              # Interface for sorting algorithms
│       ├── SequentialMergeSort.java        # Sequential merge sort implementation
│       ├── ParallelMergeSort.java          # Parallel merge sort with Fork/Join
│       ├── GenericSequentialMergeSort.java # Generic sequential merge sort for objects
│       ├── GenericParallelMergeSort.java   # Generic parallel merge sort for objects
│       ├── ArrayGenerator.java             # Array generation utilities
│       ├── MergeSortVisualizer.java        # Sequential merge sort visualization
│       ├── ParallelMergeSortVisualizer.java # Parallel merge sort visualization
│       ├── Employee.java                   # Example custom object
│       └── TestEmployeeSorting.java        # Test for generic sorting on Employee objects
├── build/                                  # Compiled classes
├── nbproject/                              # NetBeans project configuration
└── README.md                               # This file
```

## 🔧 Components

### 1. SortBenchmark.java
The main benchmark class that:
- Orchestrates benchmark execution
- Compares multiple sorting algorithms
- Calculates average execution times and speedup ratios
- Supports both GUI and console modes
- Validates sorting correctness

**Key Methods:**
- `run()`: Executes benchmark and returns formatted results (for GUI)
- `runConsole()`: Interactive console mode with user input for all parameters
- `showParallelVisualization()`: Automatically launches parallel sort visualization

### 2. GUI.java
Swing-based graphical interface featuring:
- Input fields for array size, threshold, and number of runs
- Pattern selection (Random, Reverse)
- Real-time output console
- Modern dark theme UI
- Asynchronous benchmark execution

### 3. SequentialMergeSort.java
Optimized sequential merge sort implementation:
- Handles edge cases (null, empty, single-element arrays)
- Optimized merge operation with auxiliary array
- Skips merge if subarrays are already ordered
- Time complexity: O(n log n)
- Space complexity: O(n)

### 4. ParallelMergeSort.java
Parallel merge sort using Java's Fork/Join framework:
- Configurable threshold for switching to sequential sorting
- Customizable parallelism level
- Uses `RecursiveAction` for parallel task execution
- Automatic work-stealing for load balancing
- Same optimizations as sequential version

**Key Features:**
- Threshold-based parallelization (small arrays sorted sequentially)
- Configurable thread pool size
- Efficient task splitting and merging

### 5. GenericSequentialMergeSort.java
Generic sequential merge sort implementation for any comparable type:
- Works with any object type using `Comparator<T>`
- Demonstrates sorting custom objects (Employee)
- Same algorithmic optimizations as primitive version

### 6. GenericParallelMergeSort.java
Generic parallel merge sort using Fork/Join framework:
- Works with any object type using `Comparator<T>`
- Configurable threshold for switching to sequential sorting
- Uses `RecursiveAction` for parallel task execution
- Demonstrates parallel sorting of custom objects

### 7. ArrayGenerator.java
Utility class for generating test arrays:
- `randomArray(size)`: Generates random integer arrays
- `sortedArray(size)`: Generates pre-sorted arrays
- `reverseSortedArray(size)`: Generates reverse-sorted arrays
- `copy(array)`: Safe array copying

### 8. MergeSortVisualizer.java
Visual demonstration of sequential merge sort algorithm:
- Animated bar chart representation
- Highlights elements being compared/merged
- Uses core SequentialMergeSort implementation
- Configurable delay for visualization speed
- Standalone executable component

### 9. ParallelMergeSortVisualizer.java
Visual demonstration of parallel merge sort algorithm:
- Animated bar chart representation
- Highlights elements being compared/merged during parallel execution
- Uses core ParallelMergeSort implementation
- Automatically launched from console mode
- Shows parallel divide-and-conquer process

### 10. Employee.java & TestEmployeeSorting.java
Example custom objects demonstrating generic sorting:
- Employee class with name, age, and salary
- TestEmployeeSorting: Performance comparison between GenericSequentialMergeSort and GenericParallelMergeSort
- Uses lambda expressions for Comparator (no separate Comparator class needed)
- Demonstrates real-world sorting use cases

## 🚀 Usage

### GUI Mode

1. Run the GUI application:
   ```bash
   java -cp build/classes sortbenchmark.GUI
   ```

2. Configure benchmark parameters:
   - **Array Size**: Number of elements to sort (e.g., 100000)
   - **Pattern**: Data distribution (Random or Reverse)
   - **Threshold**: Minimum array size for parallel sorting (e.g., 10000)
   - **Runs**: Number of benchmark iterations (e.g., 5)

3. Click "Run Benchmark" to execute

4. View results in the output console showing:
   - Individual run times
   - Average execution times
   - Speedup ratio (sequential vs parallel)

### Console Mode

Run the console benchmark with interactive input:
```bash
java -cp build/classes sortbenchmark.SortBenchmark
```

The console mode will prompt you to enter:
- **Array Size**: Number of elements to sort
- **Threshold**: Minimum array size for parallel sorting
- **Number of Runs**: How many times to run the benchmark
- **Pattern**: Data distribution (Random/Reverse/Sorted)

After entering parameters, the benchmark will:
1. Automatically launch parallel sort visualization window
2. Run the benchmark with your specified parameters
3. Display detailed results with timing comparisons

**Example Console Session:**
```
========================================
Parallel Merge Sort Benchmark
========================================

Detected 8 CPU cores

Enter array size: 100000
Enter threshold: 10000
Enter number of runs: 5
Enter pattern (Random/Reverse/Sorted): Random

Opening Parallel Merge Sort Visualization window...
(Visualization shows 100 elements for better performance)

========================================
Running benchmark...
========================================
...
```

### Visualization Mode

Run the merge sort visualizers:

**Sequential Merge Sort Visualizer:**
```bash
java -cp build/classes sortbenchmark.MergeSortVisualizer
```

**Parallel Merge Sort Visualizer:**
```bash
java -cp build/classes sortbenchmark.ParallelMergeSortVisualizer
```

**Note:** The parallel visualizer is automatically launched when running console mode with user input.

### Generic Sorting Test

Test generic sorting on Employee objects:
```bash
java -cp build/classes sortbenchmark.TestEmployeeSorting
```

This will compare performance of GenericSequentialMergeSort vs GenericParallelMergeSort on Employee arrays.

## 📊 Algorithms

### Merge Sort Algorithm

**Algorithm Steps:**
1. Divide the array into two halves
2. Recursively sort both halves
3. Merge the sorted halves

**Optimizations Implemented:**
- Early termination if subarrays are already ordered
- Efficient merge using auxiliary array
- Parallel execution for large arrays (threshold-based)

### Performance Characteristics

| Algorithm | Time Complexity | Space Complexity | Parallelizable |
|-----------|----------------|------------------|----------------|
| Sequential Merge Sort | O(n log n) | O(n) | No |
| Parallel Merge Sort | O(n log n) | O(n) | Yes |
| Arrays.sort() | O(n log n) | O(log n) | No |
| Arrays.parallelSort() | O(n log n) | O(n) | Yes |

## 📈 Performance Benchmarking

The benchmark compares:
1. **Sequential Merge Sort**: Custom single-threaded implementation
2. **Parallel Merge Sort (Fork/Join)**: Custom multi-threaded implementation
3. **Arrays.sort()**: Java's built-in sorting
4. **Arrays.parallelSort()**: Java's built-in parallel sorting

**Metrics Calculated:**
- Individual run execution times
- Average execution times across multiple runs
- Speedup ratio: `sequential_time / parallel_time`
- Correctness validation (compares against reference sort)

**Example Output:**
```
Running benchmark for size=100000, pattern=Random, threshold=10000, runs=5
--------------------------------------
Run 1: seq=45 ms (0.045 s) | par=15 ms (0.015 s) | arr.sort=12 ms (0.012 s) | arr.pSort=8 ms (0.008 s)
Run 2: seq=43 ms (0.043 s) | par=14 ms (0.014 s) | arr.sort=11 ms (0.011 s) | arr.pSort=7 ms (0.007 s)
...

SUMMARY:
Average Sequential: 44 ms (0.044 s)
Average Parallel (ForkJoin): 15 ms (0.015 s)
Average Arrays.sort: 12 ms (0.012 s)
Average Arrays.parallelSort: 8 ms (0.008 s)
Speedup (seq/par): 2.93x
```

## 💻 Requirements

- **Java Development Kit (JDK)**: Version 8 or higher
- **Java Runtime Environment (JRE)**: Version 8 or higher
- **IDE**: NetBeans (recommended) or any Java IDE
- **Operating System**: Windows, Linux, or macOS

## 🔨 Build & Run

### Using NetBeans

1. Open the project in NetBeans IDE
2. Build the project: `Build > Build Project` (F11)
3. Run GUI: Right-click `GUI.java` > `Run File`
4. Run Console: Right-click `SortBenchmark.java` > `Run File`

### Using Command Line

1. Compile all source files:
   ```bash
   javac -d build/classes src/sortbenchmark/*.java
   ```

2. Run the application:
   ```bash
   # GUI Mode
   java -cp build/classes sortbenchmark.GUI
   
   # Console Mode
   java -cp build/classes sortbenchmark.SortBenchmark
   
   # Visualizer
   java -cp build/classes sortbenchmark.MergeSortVisualizer
   ```

### Using Ant (build.xml)

```bash
ant compile    # Compile the project
ant run        # Run the main class
```

## 🎓 Educational Value

This project demonstrates:
- **Algorithm Implementation**: Merge sort algorithm
- **Parallel Programming**: Fork/Join framework usage
- **Performance Analysis**: Benchmarking and profiling
- **GUI Development**: Swing-based user interfaces
- **Generic Programming**: Type-safe generic implementations
- **Software Engineering**: Clean code structure and design patterns

## 📝 Notes

- The threshold parameter is crucial for parallel performance: too small causes overhead, too large limits parallelism
- Optimal threshold typically ranges from 1,000 to 100,000 depending on array size and hardware
- Parallel speedup is most noticeable on multi-core systems with large arrays (>100,000 elements)
- The visualizer is best used with smaller arrays (50-100 elements) for clear visualization

## 🔍 Future Enhancements

Potential improvements:
- Additional sorting algorithms (QuickSort, HeapSort)
- More visualization options
- Export benchmark results to CSV/JSON
- Real-time performance graphs
- Multi-threaded visualization
- Additional data patterns (nearly sorted, duplicates)

---

**Project Type**: Educational / Benchmarking Tool  
**Language**: Java  
**Framework**: Java Swing, Fork/Join Framework  
**License**: Educational Use

