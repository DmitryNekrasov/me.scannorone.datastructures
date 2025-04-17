package me.scannorone.datastructures

import kotlin.test.*
import java.util.TreeMap
import kotlin.system.measureNanoTime

/**
 * Test suite that compares the behavior of OrderedMap with Java's TreeMap.
 * These tests ensure that our OrderedMap implementation behaves consistently
 * with the standard TreeMap implementation.
 */
class OrderedMapVsTreeMapTest {

    @Test
    fun testBasicOperations() {
        val orderedMap = OrderedMap<Int, String>()
        val treeMap = TreeMap<Int, String>()
        
        // Test put and get
        for (i in 1..10) {
            assertNull(orderedMap.put(i, "value$i"))
            assertNull(treeMap.put(i, "value$i"))
        }
        
        // Compare sizes
        assertEquals(treeMap.size, orderedMap.size)
        
        // Compare get operations
        for (i in 1..10) {
            assertEquals(treeMap[i], orderedMap[i])
        }
        
        // Test update existing keys
        assertEquals(treeMap.put(5, "updated"), orderedMap.put(5, "updated"))
        assertEquals(treeMap[5], orderedMap[5])
        
        // Test remove
        for (i in listOf(2, 4, 6, 8)) {
            assertEquals(treeMap.remove(i), orderedMap.remove(i))
        }
        
        // Compare sizes after removal
        assertEquals(treeMap.size, orderedMap.size)
        
        // Compare remaining elements
        for (i in listOf(1, 3, 5, 7, 9, 10)) {
            assertEquals(treeMap[i], orderedMap[i])
        }
    }
    
    @Test
    fun testIterationOrder() {
        val orderedMap = OrderedMap<String, Int>()
        val treeMap = TreeMap<String, Int>()
        
        // Add elements in random order
        val keys = listOf("z", "a", "m", "c", "x", "b", "p", "d")
        for ((index, key) in keys.withIndex()) {
            orderedMap[key] = index
            treeMap[key] = index
        }
        
        // Compare key iteration order
        val orderedKeys = orderedMap.keys.iterator()
        val treeKeys = treeMap.keys.iterator()
        
        while (orderedKeys.hasNext() && treeKeys.hasNext()) {
            assertEquals(treeKeys.next(), orderedKeys.next())
        }
        
        // Ensure both iterators are exhausted
        assertFalse(orderedKeys.hasNext())
        assertFalse(treeKeys.hasNext())
        
        // Compare entry iteration order
        val orderedEntries = orderedMap.entries.iterator()
        val treeEntries = treeMap.entries.iterator()
        
        while (orderedEntries.hasNext() && treeEntries.hasNext()) {
            val orderedEntry = orderedEntries.next()
            val treeEntry = treeEntries.next()
            assertEquals(treeEntry.key, orderedEntry.key)
            assertEquals(treeEntry.value, orderedEntry.value)
        }
    }
    
    @Test
    fun testCollectionViews() {
        val orderedMap = OrderedMap<Int, String>()
        val treeMap = TreeMap<Int, String>()
        
        // Add same elements to both maps
        for (i in 1..10) {
            orderedMap[i] = "value$i"
            treeMap[i] = "value$i"
        }
        
        // Compare keys collection
        assertEquals(treeMap.keys.size, orderedMap.keys.size)
        assertEquals(treeMap.keys.toList(), orderedMap.keys.toList())
        
        // Compare values collection
        assertEquals(treeMap.values.size, orderedMap.values.size)
        assertEquals(treeMap.values.toList(), orderedMap.values.toList())
        
        // Compare entries collection
        assertEquals(treeMap.entries.size, orderedMap.entries.size)
        
        // Compare entry sets by converting to maps of key to value
        val orderedEntryMap = orderedMap.entries.associate { it.key to it.value }
        val treeEntryMap = treeMap.entries.associate { it.key to it.value }
        assertEquals(treeEntryMap, orderedEntryMap)
        
        // Test modification through entry
        val orderedEntry = orderedMap.entries.find { it.key == 5 }!!
        val treeEntry = treeMap.entries.find { it.key == 5 }!!
        
        assertEquals(treeEntry.setValue("modified"), orderedEntry.setValue("modified"))
        assertEquals(treeMap[5], orderedMap[5])
    }
    
    @Test
    fun testContainsOperations() {
        val orderedMap = OrderedMap<Int, String>()
        val treeMap = TreeMap<Int, String>()
        
        // Add same elements to both maps
        for (i in 1..10) {
            orderedMap[i] = "value$i"
            treeMap[i] = "value$i"
        }
        
        // Test containsKey
        for (i in 0..12) {
            assertEquals(treeMap.containsKey(i), orderedMap.containsKey(i))
        }
        
        // Test containsValue
        for (i in 1..12) {
            assertEquals(treeMap.containsValue("value$i"), orderedMap.containsValue("value$i"))
        }
        
        assertEquals(treeMap.containsValue("nonexistent"), orderedMap.containsValue("nonexistent"))
    }
    
    @Test
    fun testClearAndIsEmpty() {
        val orderedMap = OrderedMap<Int, String>()
        val treeMap = TreeMap<Int, String>()
        
        // Initially both should be empty
        assertEquals(treeMap.isEmpty(), orderedMap.isEmpty())
        
        // Add elements
        for (i in 1..5) {
            orderedMap[i] = "value$i"
            treeMap[i] = "value$i"
        }
        
        // Both should not be empty
        assertEquals(treeMap.isEmpty(), orderedMap.isEmpty())
        
        // Clear both maps
        orderedMap.clear()
        treeMap.clear()
        
        // Both should be empty again
        assertEquals(treeMap.isEmpty(), orderedMap.isEmpty())
        assertEquals(0, orderedMap.size)
        assertEquals(0, treeMap.size)
    }
    
    @Test
    fun testPutAll() {
        val orderedMap = OrderedMap<Int, String>()
        val treeMap = TreeMap<Int, String>()
        
        // Create a map to add to both
        val mapToAdd = mapOf(
            1 to "one",
            2 to "two",
            3 to "three",
            4 to "four"
        )
        
        // Add all entries
        orderedMap.putAll(mapToAdd)
        treeMap.putAll(mapToAdd)
        
        // Compare results
        assertEquals(treeMap.size, orderedMap.size)
        assertEquals(treeMap.keys.toList(), orderedMap.keys.toList())
        assertEquals(treeMap.values.toList(), orderedMap.values.toList())
        
        // Add more entries with some overlapping keys
        val moreEntries = mapOf(
            3 to "THREE",
            4 to "FOUR",
            5 to "five",
            6 to "six"
        )
        
        orderedMap.putAll(moreEntries)
        treeMap.putAll(moreEntries)
        
        // Compare results again
        assertEquals(treeMap.size, orderedMap.size)
        assertEquals(treeMap.keys.toList(), orderedMap.keys.toList())
        assertEquals(treeMap.values.toList(), orderedMap.values.toList())
    }
    
    @Test
    fun testWithCustomComparableObjects() {
        data class Person(val id: Int, val name: String) : Comparable<Person> {
            override fun compareTo(other: Person): Int = id.compareTo(other.id)
        }
        
        val orderedMap = OrderedMap<Person, String>()
        val treeMap = TreeMap<Person, String>()
        
        val people = listOf(
            Person(3, "Charlie"),
            Person(1, "Alice"),
            Person(4, "Dave"),
            Person(2, "Bob")
        )
        
        // Add people to both maps
        for (person in people) {
            orderedMap[person] = person.name
            treeMap[person] = person.name
        }
        
        // Compare sizes
        assertEquals(treeMap.size, orderedMap.size)
        
        // Compare keys in order
        val orderedKeys = orderedMap.keys.toList()
        val treeKeys = treeMap.keys.toList()
        assertEquals(treeKeys, orderedKeys)
        
        // Test with equal IDs but different names
        val duplicate = Person(1, "Alex")
        assertEquals(treeMap.put(duplicate, "Alex"), orderedMap.put(duplicate, "Alex"))
        assertEquals(treeMap.size, orderedMap.size)
    }
    
    @Test
    fun testPerformanceComparison() {
        // This test compares the performance of OrderedMap vs TreeMap
        // Note: This is not a strict performance test, just a rough comparison
        
        val orderedMap = OrderedMap<Int, String>()
        val treeMap = TreeMap<Int, String>()
        
        // Test insertion performance
        val insertionSizeOrderedMap = measureNanoTime {
            for (i in 1..1000) {
                orderedMap[i] = "value$i"
            }
        }
        
        val insertionSizeTreeMap = measureNanoTime {
            for (i in 1..1000) {
                treeMap[i] = "value$i"
            }
        }
        
        println("Insertion time (ns): OrderedMap=$insertionSizeOrderedMap, TreeMap=$insertionSizeTreeMap")
        
        // Test lookup performance
        val lookupTimeOrderedMap = measureNanoTime {
            for (i in 1..1000) {
                orderedMap[i]
            }
        }
        
        val lookupTimeTreeMap = measureNanoTime {
            for (i in 1..1000) {
                treeMap[i]
            }
        }
        
        println("Lookup time (ns): OrderedMap=$lookupTimeOrderedMap, TreeMap=$lookupTimeTreeMap")
        
        // Test iteration performance
        val iterationTimeOrderedMap = measureNanoTime {
            orderedMap.entries.forEach { (_, _) -> }
        }
        
        val iterationTimeTreeMap = measureNanoTime {
            treeMap.entries.forEach { (_, _) -> }
        }
        
        println("Iteration time (ns): OrderedMap=$iterationTimeOrderedMap, TreeMap=$iterationTimeTreeMap")
        
        // We don't assert on performance as it can vary, but we log the results
    }
    
    @Test
    fun testEdgeCases() {
        val orderedMap = OrderedMap<Int, String?>()
        val treeMap = TreeMap<Int, String?>()
        
        // Test with null values
        orderedMap[1] = null
        treeMap[1] = null
        
        assertEquals(treeMap[1], orderedMap[1])
        assertEquals(treeMap.size, orderedMap.size)
        
        // Test replacing null with value
        orderedMap[1] = "not null anymore"
        treeMap[1] = "not null anymore"
        
        assertEquals(treeMap[1], orderedMap[1])
        
        // Test with extreme values
        val extremeKeys = listOf(Int.MIN_VALUE, -1000, 0, 1000, Int.MAX_VALUE)
        for (key in extremeKeys) {
            orderedMap[key] = "extreme$key"
            treeMap[key] = "extreme$key"
        }
        
        for (key in extremeKeys) {
            assertEquals(treeMap[key], orderedMap[key])
        }
        
        // Compare iteration order with extreme values
        assertEquals(treeMap.keys.toList(), orderedMap.keys.toList())
    }
}