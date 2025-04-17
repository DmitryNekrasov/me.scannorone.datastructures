package me.scannorone.datastructures

import kotlin.test.*

class OrderedMapTest {

    @Test
    fun testBasicOperations() {
        val map = OrderedMap<Int, String>()
        
        // Test empty map
        assertTrue(map.isEmpty())
        assertEquals(0, map.size)
        
        // Test put and get
        assertNull(map.put(1, "one"))
        assertEquals("one", map.get(1))
        assertEquals(1, map.size)
        assertFalse(map.isEmpty())
        
        // Test update existing key
        assertEquals("one", map.put(1, "ONE"))
        assertEquals("ONE", map.get(1))
        assertEquals(1, map.size)
        
        // Test multiple entries
        assertNull(map.put(2, "two"))
        assertNull(map.put(3, "three"))
        assertEquals(3, map.size)
        assertEquals("ONE", map.get(1))
        assertEquals("two", map.get(2))
        assertEquals("three", map.get(3))
        
        // Test remove
        assertEquals("two", map.remove(2))
        assertEquals(2, map.size)
        assertNull(map.get(2))
        
        // Test clear
        map.clear()
        assertTrue(map.isEmpty())
        assertEquals(0, map.size)
        assertNull(map.get(1))
    }
    
    @Test
    fun testContainsKeyAndValue() {
        val map = OrderedMap<String, Int>()
        
        // Test on empty map
        assertFalse(map.containsKey("a"))
        assertFalse(map.containsValue(1))
        
        // Add entries
        map.put("a", 1)
        map.put("b", 2)
        map.put("c", 3)
        
        // Test containsKey
        assertTrue(map.containsKey("a"))
        assertTrue(map.containsKey("b"))
        assertTrue(map.containsKey("c"))
        assertFalse(map.containsKey("d"))
        
        // Test containsValue
        assertTrue(map.containsValue(1))
        assertTrue(map.containsValue(2))
        assertTrue(map.containsValue(3))
        assertFalse(map.containsValue(4))
        
        // Test after removal
        map.remove("b")
        assertTrue(map.containsKey("a"))
        assertFalse(map.containsKey("b"))
        assertTrue(map.containsKey("c"))
        
        assertTrue(map.containsValue(1))
        assertFalse(map.containsValue(2))
        assertTrue(map.containsValue(3))
    }
    
    @Test
    fun testPutAll() {
        val map1 = OrderedMap<Int, String>()
        map1.put(1, "one")
        map1.put(2, "two")
        
        val map2 = OrderedMap<Int, String>()
        map2.putAll(map1)
        
        assertEquals(2, map2.size)
        assertEquals("one", map2[1])
        assertEquals("two", map2[2])
        
        // Test putAll with regular map
        val regularMap = mapOf(3 to "three", 4 to "four")
        map2.putAll(regularMap)
        
        assertEquals(4, map2.size)
        assertEquals("one", map2[1])
        assertEquals("two", map2[2])
        assertEquals("three", map2[3])
        assertEquals("four", map2[4])
        
        // Test putAll with overlapping keys
        val overlappingMap = mapOf(2 to "TWO", 4 to "FOUR")
        map2.putAll(overlappingMap)
        
        assertEquals(4, map2.size)
        assertEquals("one", map2[1])
        assertEquals("TWO", map2[2])
        assertEquals("three", map2[3])
        assertEquals("FOUR", map2[4])
    }
    
    @Test
    fun testKeysValuesAndEntries() {
        val map = OrderedMap<Int, String>()
        map.put(3, "three")
        map.put(1, "one")
        map.put(2, "two")
        
        // Test keys
        val keys = map.keys
        assertEquals(3, keys.size)
        assertTrue(keys.contains(1))
        assertTrue(keys.contains(2))
        assertTrue(keys.contains(3))
        
        // Test values
        val values = map.values
        assertEquals(3, values.size)
        assertTrue(values.contains("one"))
        assertTrue(values.contains("two"))
        assertTrue(values.contains("three"))
        
        // Test entries
        val entries = map.entries
        assertEquals(3, entries.size)
        assertTrue(entries.any { it.key == 1 && it.value == "one" })
        assertTrue(entries.any { it.key == 2 && it.value == "two" })
        assertTrue(entries.any { it.key == 3 && it.value == "three" })
        
        // Test modification through entry
        val entry = entries.find { it.key == 2 }!!
        val oldValue = entry.setValue("TWO")
        assertEquals("two", oldValue)
        assertEquals("TWO", map[2])
    }
    
    @Test
    fun testIterationOrder() {
        val map = OrderedMap<Int, String>()
        
        // Add entries in random order
        map.put(5, "five")
        map.put(3, "three")
        map.put(1, "one")
        map.put(4, "four")
        map.put(2, "two")
        
        // Check keys are iterated in sorted order
        val keys = map.keys.iterator()
        assertEquals(1, keys.next())
        assertEquals(2, keys.next())
        assertEquals(3, keys.next())
        assertEquals(4, keys.next())
        assertEquals(5, keys.next())
        assertFalse(keys.hasNext())
        
        // Check values are iterated in key-sorted order
        val values = map.values.iterator()
        assertEquals("one", values.next())
        assertEquals("two", values.next())
        assertEquals("three", values.next())
        assertEquals("four", values.next())
        assertEquals("five", values.next())
        assertFalse(values.hasNext())
        
        // Check entries are iterated in key-sorted order
        val entries = map.entries.iterator()
        var entry = entries.next()
        assertEquals(1, entry.key)
        assertEquals("one", entry.value)
        
        entry = entries.next()
        assertEquals(2, entry.key)
        assertEquals("two", entry.value)
        
        entry = entries.next()
        assertEquals(3, entry.key)
        assertEquals("three", entry.value)
        
        entry = entries.next()
        assertEquals(4, entry.key)
        assertEquals("four", entry.value)
        
        entry = entries.next()
        assertEquals(5, entry.key)
        assertEquals("five", entry.value)
        
        assertFalse(entries.hasNext())
    }
    
    @Test
    fun testRemoveThroughIterators() {
        val map = OrderedMap<Int, String>()
        map.put(1, "one")
        map.put(2, "two")
        map.put(3, "three")
        map.put(4, "four")
        
        // Test remove through keys iterator
        val keysIterator = map.keys.iterator()
        keysIterator.next() // Skip 1
        keysIterator.next() // Skip 2
        keysIterator.remove() // Remove 2
        assertEquals(3, map.size)
        assertFalse(map.containsKey(2))
        
        // Test remove through values iterator
        val valuesIterator = map.values.iterator()
        valuesIterator.next() // Skip "one"
        valuesIterator.next() // Skip "three" (since 2 was removed)
        valuesIterator.remove() // Remove entry with value "three" (key 3)
        assertEquals(2, map.size)
        assertFalse(map.containsKey(3))
        
        // Test remove through entries iterator
        val entriesIterator = map.entries.iterator()
        entriesIterator.next() // Skip 1
        entriesIterator.remove() // Remove 1
        assertEquals(1, map.size)
        assertFalse(map.containsKey(1))
        assertTrue(map.containsKey(4))
    }
    
    @Test
    fun testEdgeCases() {
        val map = OrderedMap<String, Int?>()
        
        // Test with null values
        map.put("a", null)
        assertTrue(map.containsKey("a"))
        assertNull(map.get("a"))
        
        // Test updating null value
        assertNull(map.put("a", 1))
        assertEquals(1, map.get("a"))
        
        // Test with empty string key
        map.put("", 0)
        assertTrue(map.containsKey(""))
        assertEquals(0, map.get(""))
        
        // Test with large number of entries
        for (i in 1..1000) {
            map.put("key$i", i)
        }
        assertEquals(1002, map.size) // 1000 + "a" and ""
        
        // Verify some random entries
        assertEquals(42, map.get("key42"))
        assertEquals(999, map.get("key999"))
    }
    
    @Test
    fun testTreeBalancing() {
        val map = OrderedMap<Int, String>()
        
        // Insert elements in ascending order (worst case for unbalanced BST)
        for (i in 1..1000) {
            map.put(i, "value$i")
        }
        
        // Verify all elements are accessible with O(log n) complexity
        for (i in 1..1000) {
            assertEquals("value$i", map[i])
        }
        
        // Insert elements in descending order
        val map2 = OrderedMap<Int, String>()
        for (i in 1000 downTo 1) {
            map2.put(i, "value$i")
        }
        
        // Verify all elements are accessible
        for (i in 1..1000) {
            assertEquals("value$i", map2[i])
        }
        
        // Verify iteration order is still correct (ascending)
        val iterator = map2.keys.iterator()
        var prev = 0
        while (iterator.hasNext()) {
            val current = iterator.next()
            assertTrue(current > prev)
            prev = current
        }
    }
    
    @Test
    fun testWithCustomComparableObjects() {
        data class Person(val id: Int, val name: String) : Comparable<Person> {
            override fun compareTo(other: Person): Int = id.compareTo(other.id)
        }
        
        val map = OrderedMap<Person, String>()
        
        val p3 = Person(3, "Charlie")
        val p1 = Person(1, "Alice")
        val p2 = Person(2, "Bob")
        val p4 = Person(4, "Dave")
        
        map.put(p3, "Developer")
        map.put(p1, "Manager")
        map.put(p2, "Designer")
        map.put(p4, "Tester")
        
        // Verify correct order
        val iterator = map.keys.iterator()
        assertEquals(p1, iterator.next())
        assertEquals(p2, iterator.next())
        assertEquals(p3, iterator.next())
        assertEquals(p4, iterator.next())
        
        // Test with equal IDs but different names
        val p1Duplicate = Person(1, "Alex")
        assertEquals("Manager", map.put(p1Duplicate, "Director"))
        assertEquals(4, map.size) // Size shouldn't change as the key is considered equal
    }
    
    @Test
    fun testComparisonWithStandardSortedMap() {
        // Create both ordered map and standard sorted map
        val orderedMap = OrderedMap<Int, String>()
        val sortedMap = sortedMapOf<Int, String>()
        
        // Add same elements to both
        val entries = listOf(
            5 to "five",
            3 to "three",
            8 to "eight",
            1 to "one",
            2 to "two",
            7 to "seven",
            4 to "four",
            6 to "six",
            9 to "nine"
        )
        
        for ((key, value) in entries) {
            orderedMap[key] = value
            sortedMap[key] = value
        }
        
        // Compare sizes
        assertEquals(sortedMap.size, orderedMap.size)
        
        // Compare keys
        val orderedKeys = orderedMap.keys.toList()
        val sortedKeys = sortedMap.keys.toList()
        assertEquals(sortedKeys, orderedKeys)
        
        // Compare values (in key order)
        val orderedValues = orderedMap.entries.map { it.value }
        val sortedValues = sortedMap.entries.map { it.value }
        assertEquals(sortedValues, orderedValues)
        
        // Test after removals
        orderedMap.remove(3)
        sortedMap.remove(3)
        orderedMap.remove(7)
        sortedMap.remove(7)
        
        assertEquals(sortedMap.size, orderedMap.size)
        assertEquals(sortedMap.keys.toList(), orderedMap.keys.toList())
    }
}