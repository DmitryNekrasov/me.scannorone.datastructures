# OrderedMap vs TreeMap Benchmarks

This project includes benchmarks comparing the performance of `OrderedMap` (a custom implementation based on Red-Black tree) with Java's `TreeMap`.

## Running the Benchmarks

To run the benchmarks, use the following Gradle command:

```bash
./gradlew benchmark
```

This will execute all benchmarks and generate a report in the `build/reports/benchmarks/main` directory.

## Benchmark Details

The benchmarks compare the following operations:

1. **Insertion Performance**
   - Small dataset (100 elements)
   - Medium dataset (1,000 elements)

2. **Lookup Performance**
   - Small dataset (100 elements)
   - Medium dataset (1,000 elements)
   - Large dataset (10,000 elements)

3. **Iteration Performance**
   - Small dataset (100 elements)
   - Medium dataset (1,000 elements)
   - Large dataset (10,000 elements)

4. **Removal Performance**
   - Small dataset (100 elements)
   - Medium dataset (1,000 elements)

## Interpreting Results

The benchmark results are reported in microseconds (μs) and represent the average time taken to perform each operation. Lower values indicate better performance.

When comparing the results:
- Look at the "Score" column which shows the average time
- The "Error" column shows the margin of error
- Compare the scores between OrderedMap and TreeMap for each operation type and dataset size

## Implementation Details

- `OrderedMap`: A custom implementation of a sorted map based on a Red-Black tree
- `TreeMap`: Java's standard implementation of a sorted map based on a Red-Black tree

Both implementations should have similar asymptotic complexity (O(log n) for basic operations), but actual performance may differ due to implementation details.