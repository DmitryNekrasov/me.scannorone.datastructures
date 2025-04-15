package me.scannorone.datastructures

import kotlin.test.*

class SegmentTreeTest {

    @Test
    fun testSumSegmentTreeWithIntegers() {
        val nums = listOf(1, 3, 5, 7, 9, 11)
        val sumTree = SegmentTree(nums) { a, b -> a + b }

        assertEquals(36, sumTree[0..5])  // Sum of all elements
        assertEquals(4, sumTree[0..1])   // 1 + 3
        assertEquals(15, sumTree[1..3])  // 3 + 5 + 7
        assertEquals(32, sumTree[2..5])  // 5 + 7 + 9 + 11
        assertEquals(20, sumTree[4..5])   // 9 + 11
        assertEquals(7, sumTree[3..3])   // Single element
    }

    @Test
    fun testMaxSegmentTreeWithIntegers() {
        val nums = listOf(4, 2, 7, 1, 9, 5)
        val maxTree = SegmentTree(nums) { a, b -> maxOf(a, b) }

        assertEquals(9, maxTree[0..5])  // Max of all elements
        assertEquals(4, maxTree[0..1])  // Max of 4 and 2
        assertEquals(7, maxTree[1..3])  // Max of 2, 7, and 1
        assertEquals(9, maxTree[2..5])  // Max of 7, 1, 9, and 5
        assertEquals(9, maxTree[3..4])  // Max of 1 and 9
        assertEquals(5, maxTree[5..5])  // Single element
    }

    @Test
    fun testMinSegmentTreeWithIntegers() {
        val nums = listOf(4, 2, 7, 1, 9, 5)
        val minTree = SegmentTree(nums) { a, b -> minOf(a, b) }

        assertEquals(1, minTree[0..5])  // Min of all elements
        assertEquals(2, minTree[0..1])  // Min of 4 and 2
        assertEquals(1, minTree[1..3])  // Min of 2, 7, and 1
        assertEquals(1, minTree[2..5])  // Min of 7, 1, 9, and 5
        assertEquals(1, minTree[3..4])  // Min of 1 and 9
        assertEquals(5, minTree[5..5])  // Single element
    }

    @Test
    fun testStringConcatenationSegmentTree() {
        val strings = listOf("a", "b", "c", "d", "e")
        val concatTree = SegmentTree(strings) { a, b -> a + b }

        assertEquals("abcde", concatTree[0..4])  // Concat all elements
        assertEquals("ab", concatTree[0..1])     // a + b
        assertEquals("bcd", concatTree[1..3])    // b + c + d
        assertEquals("cde", concatTree[2..4])    // c + d + e
        assertEquals("de", concatTree[3..4])     // d + e
        assertEquals("c", concatTree[2..2])      // Single element
    }

    @Test
    fun testSegmentTreeUpdate() {
        val nums = listOf(1, 3, 5, 7, 9)
        val sumTree = SegmentTree(nums) { a, b -> a + b }

        assertEquals(25, sumTree[0..4])  // Initial sum

        sumTree.update(2, 10)  // Update 5 to 10 (increase by 5)

        assertEquals(30, sumTree[0..4])  // New sum
        assertEquals(20, sumTree[1..3])  // New partial sum
        assertEquals(10, sumTree[2..2])  // Check single updated element
    }

    @Test
    fun testSegmentTreeWithEmptyCollection() {
        val emptyTree = SegmentTree<Int>(emptyList()) { a, b -> a + b }

        assertNull(emptyTree[0..0])  // Should return null for any range
    }

    @Test
    fun testSegmentTreeWithOutOfBoundsQuery() {
        val nums = listOf(1, 3, 5, 7, 9)
        val sumTree = SegmentTree(nums) { a, b -> a + b }

        assertNull(sumTree[-1..3])     // Start out of bounds
        assertNull(sumTree[0..5])      // End out of bounds
        assertNull(sumTree[-1..5])     // Both out of bounds
    }

    @Test
    fun testSegmentTreeWithInvalidRange() {
        val nums = listOf(1, 3, 5, 7, 9)
        val sumTree = SegmentTree(nums) { a, b -> a + b }

        assertNull(sumTree[3..1])  // Start > End
    }

    @Test
    fun testSegmentTreeUpdateWithOutOfBoundsIndex() {
        val nums = listOf(1, 3, 5, 7, 9)
        val sumTree = SegmentTree(nums) { a, b -> a + b }

        assertFailsWith<IndexOutOfBoundsException> {
            sumTree.update(-1, 10)  // Negative index
        }

        assertFailsWith<IndexOutOfBoundsException> {
            sumTree.update(5, 10)   // Index >= size
        }
    }

    @Test
    fun testSegmentTreeWithCustomObjects() {
        data class Point(val x: Int, val y: Int)

        val points = listOf(
            Point(1, 2),
            Point(3, 4),
            Point(5, 6),
            Point(7, 8)
        )

        val sumTree = SegmentTree(points) { a, b -> Point(a.x + b.x, a.y + b.y) }

        assertEquals(Point(16, 20), sumTree[0..3])  // Sum of all points
        assertEquals(Point(4, 6), sumTree[0..1])    // Sum of first two points
        assertEquals(Point(8, 10), sumTree[1..2])   // Sum of middle points
        assertEquals(Point(12, 14), sumTree[2..3])  // Sum of last two points
    }

    @Test
    fun testSegmentTreeWithDifferentOperationsOnSameData() {
        val nums = listOf(3, 1, 4, 1, 5, 9, 2, 6)

        val sumTree = SegmentTree(nums) { a, b -> a + b }
        val productTree = SegmentTree(nums) { a, b -> a * b }
        val maxTree = SegmentTree(nums) { a, b -> maxOf(a, b) }
        val minTree = SegmentTree(nums) { a, b -> minOf(a, b) }

        // Test entire range
        assertEquals(31, sumTree[0..7])
        assertEquals(6480, productTree[0..7])
        assertEquals(9, maxTree[0..7])
        assertEquals(1, minTree[0..7])

        // Test partial range
        assertEquals(19, sumTree[2..5])
        assertEquals(180, productTree[2..5])
        assertEquals(9, maxTree[2..5])
        assertEquals(1, minTree[2..5])
    }

    @Test
    fun testComplexUpdatesOnSegmentTree() {
        val nums = listOf(1, 3, 5, 7, 9)
        val sumTree = SegmentTree(nums) { a, b -> a + b }

        assertEquals(25, sumTree[0..4])  // Initial sum

        // Multiple updates
        sumTree.update(0, 10)  // Update 1 to 10
        sumTree.update(2, 15)  // Update 5 to 15
        sumTree.update(4, 20)  // Update 9 to 20

        assertEquals(55, sumTree[0..4])  // New total sum
        assertEquals(28, sumTree[0..2])  // 10 + 3 + 15
        assertEquals(42, sumTree[2..4])  // 15 + 7 + 20
        assertEquals(25, sumTree[1..3])  // 3 + 15 + 7
    }

    @Test
    fun testSegmentTreeWithSingleElement() {
        val singleList = listOf(42)
        val sumTree = SegmentTree(singleList) { a, b -> a + b }

        assertEquals(42, sumTree[0..0])  // Single element query

        sumTree.update(0, 100)

        assertEquals(100, sumTree[0..0])  // After update
    }

    @Test
    fun testMultipleUpdatesAndQueriesInSequence() {
        val nums = listOf(1, 2, 3, 4, 5)
        val sumTree = SegmentTree(nums) { a, b -> a + b }

        assertEquals(15, sumTree[0..4])

        sumTree.update(1, 10)  // Change 2 to 10
        assertEquals(23, sumTree[0..4])
        assertEquals(14, sumTree[0..2])

        sumTree.update(3, 20)  // Change 4 to 20
        assertEquals(39, sumTree[0..4])
        assertEquals(38, sumTree[1..4])

        sumTree.update(0, 5)   // Change 1 to 5
        assertEquals(43, sumTree[0..4])
        assertEquals(15, sumTree[0..1])
    }

    @Test
    fun testBooleanOperationsWithSegmentTree() {
        val booleans = listOf(true, false, true, true, false)

        // OR operation
        val orTree = SegmentTree(booleans) { a, b -> a || b }
        assertTrue(orTree[0..4]!!)  // At least one true
        assertTrue(orTree[0..1]!!)  // true || false
        assertFalse(orTree[1..1]!!)  // Single false
        assertTrue(orTree[2..3]!!)  // true || true

        // AND operation
        val andTree = SegmentTree(booleans) { a, b -> a && b }
        assertFalse(andTree[0..4]!!)  // Not all true
        assertFalse(andTree[0..1]!!)  // true && false
        assertTrue(andTree[2..3]!!)  // true && true
        assertFalse(andTree[3..4]!!)  // true && false
    }
}