package me.scannorone.datastructures

import org.openjdk.jmh.annotations.*
import java.util.TreeMap
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * Benchmarks comparing the performance of OrderedMap with Java's TreeMap.
 * These benchmarks measure the performance of various operations on both map implementations.
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
open class OrderedMapVsTreeMapBenchmark {

    private val random = Random(42) // Fixed seed for reproducibility
    
    // Small dataset (100 elements)
    private lateinit var smallOrderedMap: OrderedMap<Int, String>
    private lateinit var smallTreeMap: TreeMap<Int, String>
    private lateinit var smallKeysToLookup: List<Int>
    
    // Medium dataset (1,000 elements)
    private lateinit var mediumOrderedMap: OrderedMap<Int, String>
    private lateinit var mediumTreeMap: TreeMap<Int, String>
    private lateinit var mediumKeysToLookup: List<Int>
    
    // Large dataset (10,000 elements)
    private lateinit var largeOrderedMap: OrderedMap<Int, String>
    private lateinit var largeTreeMap: TreeMap<Int, String>
    private lateinit var largeKeysToLookup: List<Int>
    
    // Keys to insert during benchmarks
    private lateinit var keysToInsert: List<Int>
    
    @Setup
    fun setup() {
        // Initialize small dataset
        smallOrderedMap = OrderedMap()
        smallTreeMap = TreeMap()
        val smallKeys = List(100) { random.nextInt() }
        smallKeys.forEach { key ->
            val value = "value-$key"
            smallOrderedMap[key] = value
            smallTreeMap[key] = value
        }
        smallKeysToLookup = smallKeys.shuffled(random).take(50)
        
        // Initialize medium dataset
        mediumOrderedMap = OrderedMap()
        mediumTreeMap = TreeMap()
        val mediumKeys = List(1_000) { random.nextInt() }
        mediumKeys.forEach { key ->
            val value = "value-$key"
            mediumOrderedMap[key] = value
            mediumTreeMap[key] = value
        }
        mediumKeysToLookup = mediumKeys.shuffled(random).take(500)
        
        // Initialize large dataset
        largeOrderedMap = OrderedMap()
        largeTreeMap = TreeMap()
        val largeKeys = List(10_000) { random.nextInt() }
        largeKeys.forEach { key ->
            val value = "value-$key"
            largeOrderedMap[key] = value
            largeTreeMap[key] = value
        }
        largeKeysToLookup = largeKeys.shuffled(random).take(1_000)
        
        // Keys to insert during benchmarks
        keysToInsert = List(1_000) { random.nextInt() }
    }
    
    // Insertion benchmarks
    
    @Benchmark
    fun insertSmallOrderedMap(): OrderedMap<Int, String> {
        val map = OrderedMap<Int, String>()
        for (i in 0 until 100) {
            val key = keysToInsert[i]
            map[key] = "value-$key"
        }
        return map
    }
    
    @Benchmark
    fun insertSmallTreeMap(): TreeMap<Int, String> {
        val map = TreeMap<Int, String>()
        for (i in 0 until 100) {
            val key = keysToInsert[i]
            map[key] = "value-$key"
        }
        return map
    }
    
    @Benchmark
    fun insertMediumOrderedMap(): OrderedMap<Int, String> {
        val map = OrderedMap<Int, String>()
        for (i in 0 until 1_000) {
            val key = keysToInsert[i]
            map[key] = "value-$key"
        }
        return map
    }
    
    @Benchmark
    fun insertMediumTreeMap(): TreeMap<Int, String> {
        val map = TreeMap<Int, String>()
        for (i in 0 until 1_000) {
            val key = keysToInsert[i]
            map[key] = "value-$key"
        }
        return map
    }
    
    // Lookup benchmarks
    
    @Benchmark
    fun lookupSmallOrderedMap(): Int {
        var found = 0
        for (key in smallKeysToLookup) {
            if (smallOrderedMap.containsKey(key)) {
                found++
            }
        }
        return found
    }
    
    @Benchmark
    fun lookupSmallTreeMap(): Int {
        var found = 0
        for (key in smallKeysToLookup) {
            if (smallTreeMap.containsKey(key)) {
                found++
            }
        }
        return found
    }
    
    @Benchmark
    fun lookupMediumOrderedMap(): Int {
        var found = 0
        for (key in mediumKeysToLookup) {
            if (mediumOrderedMap.containsKey(key)) {
                found++
            }
        }
        return found
    }
    
    @Benchmark
    fun lookupMediumTreeMap(): Int {
        var found = 0
        for (key in mediumKeysToLookup) {
            if (mediumTreeMap.containsKey(key)) {
                found++
            }
        }
        return found
    }
    
    @Benchmark
    fun lookupLargeOrderedMap(): Int {
        var found = 0
        for (key in largeKeysToLookup) {
            if (largeOrderedMap.containsKey(key)) {
                found++
            }
        }
        return found
    }
    
    @Benchmark
    fun lookupLargeTreeMap(): Int {
        var found = 0
        for (key in largeKeysToLookup) {
            if (largeTreeMap.containsKey(key)) {
                found++
            }
        }
        return found
    }
    
    // Iteration benchmarks
    
    @Benchmark
    fun iterateSmallOrderedMap(): Int {
        var sum = 0
        for ((key, _) in smallOrderedMap) {
            sum += key
        }
        return sum
    }
    
    @Benchmark
    fun iterateSmallTreeMap(): Int {
        var sum = 0
        for ((key, _) in smallTreeMap) {
            sum += key
        }
        return sum
    }
    
    @Benchmark
    fun iterateMediumOrderedMap(): Int {
        var sum = 0
        for ((key, _) in mediumOrderedMap) {
            sum += key
        }
        return sum
    }
    
    @Benchmark
    fun iterateMediumTreeMap(): Int {
        var sum = 0
        for ((key, _) in mediumTreeMap) {
            sum += key
        }
        return sum
    }
    
    @Benchmark
    fun iterateLargeOrderedMap(): Int {
        var sum = 0
        for ((key, _) in largeOrderedMap) {
            sum += key
        }
        return sum
    }
    
    @Benchmark
    fun iterateLargeTreeMap(): Int {
        var sum = 0
        for ((key, _) in largeTreeMap) {
            sum += key
        }
        return sum
    }
    
    // Removal benchmarks
    
    @Benchmark
    fun removeSmallOrderedMap(): Int {
        val map = OrderedMap<Int, String>()
        map.putAll(smallOrderedMap)
        var removed = 0
        for (key in smallKeysToLookup) {
            if (map.remove(key) != null) {
                removed++
            }
        }
        return removed
    }
    
    @Benchmark
    fun removeSmallTreeMap(): Int {
        val map = TreeMap<Int, String>()
        map.putAll(smallTreeMap)
        var removed = 0
        for (key in smallKeysToLookup) {
            if (map.remove(key) != null) {
                removed++
            }
        }
        return removed
    }
    
    @Benchmark
    fun removeMediumOrderedMap(): Int {
        val map = OrderedMap<Int, String>()
        map.putAll(mediumOrderedMap)
        var removed = 0
        for (key in mediumKeysToLookup) {
            if (map.remove(key) != null) {
                removed++
            }
        }
        return removed
    }
    
    @Benchmark
    fun removeMediumTreeMap(): Int {
        val map = TreeMap<Int, String>()
        map.putAll(mediumTreeMap)
        var removed = 0
        for (key in mediumKeysToLookup) {
            if (map.remove(key) != null) {
                removed++
            }
        }
        return removed
    }
}