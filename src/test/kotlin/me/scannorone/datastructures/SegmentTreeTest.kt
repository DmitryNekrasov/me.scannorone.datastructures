package me.scannorone.datastructures

import kotlin.test.*

class SegmentTreeTest {

    @Test
    fun testSumSegmentTreeWithIntegers() {
        val nums = listOf(1, 3, 5, 7, 9, 11)
        val sumTree = SegmentTree(nums) { a, b -> a + b }

        assertEquals(36, sumTree[0..5])
        assertEquals(4, sumTree[0..1])
        assertEquals(15, sumTree[1..3])
        assertEquals(32, sumTree[2..5])
        assertEquals(20, sumTree[4..5])
        assertEquals(7, sumTree[3..3])
    }

    @Test
    fun testMaxSegmentTreeWithIntegers() {
        val nums = listOf(4, 2, 7, 1, 9, 5)
        val maxTree = SegmentTree(nums) { a, b -> maxOf(a, b) }

        assertEquals(9, maxTree[0..5])
        assertEquals(4, maxTree[0..1])
        assertEquals(7, maxTree[1..3])
        assertEquals(9, maxTree[2..5])
        assertEquals(9, maxTree[3..4])
        assertEquals(5, maxTree[5..5])
    }

    @Test
    fun testMinSegmentTreeWithIntegers() {
        val nums = listOf(4, 2, 7, 1, 9, 5)
        val minTree = SegmentTree(nums) { a, b -> minOf(a, b) }

        assertEquals(1, minTree[0..5])
        assertEquals(2, minTree[0..1])
        assertEquals(1, minTree[1..3])
        assertEquals(1, minTree[2..5])
        assertEquals(1, minTree[3..4])
        assertEquals(5, minTree[5..5])
    }

    @Test
    fun testStringConcatenationSegmentTree() {
        val strings = listOf("a", "b", "c", "d", "e")
        val concatTree = SegmentTree(strings) { a, b -> a + b }

        assertEquals("abcde", concatTree[0..4])
        assertEquals("ab", concatTree[0..1])
        assertEquals("bcd", concatTree[1..3])
        assertEquals("cde", concatTree[2..4])
        assertEquals("de", concatTree[3..4])
        assertEquals("c", concatTree[2..2])
    }

    @Test
    fun testSegmentTreeUpdate() {
        val nums = listOf(1, 3, 5, 7, 9)
        val sumTree = SegmentTree(nums) { a, b -> a + b }

        assertEquals(25, sumTree[0..4])

        sumTree.update(2, 10)

        assertEquals(30, sumTree[0..4])
        assertEquals(20, sumTree[1..3])
        assertEquals(10, sumTree[2..2])
    }

    @Test
    fun testSegmentTreeWithEmptyCollection() {
        val emptyTree = SegmentTree<Int>(emptyList()) { a, b -> a + b }

        assertNull(emptyTree[0..0])
    }

    @Test
    fun testSegmentTreeWithOutOfBoundsQuery() {
        val nums = listOf(1, 3, 5, 7, 9)
        val sumTree = SegmentTree(nums) { a, b -> a + b }

        assertNull(sumTree[-1..3])
        assertNull(sumTree[0..5])
        assertNull(sumTree[-1..5])
    }

    @Test
    fun testSegmentTreeWithInvalidRange() {
        val nums = listOf(1, 3, 5, 7, 9)
        val sumTree = SegmentTree(nums) { a, b -> a + b }

        assertNull(sumTree[3..1])
    }

    @Test
    fun testSegmentTreeUpdateWithOutOfBoundsIndex() {
        val nums = listOf(1, 3, 5, 7, 9)
        val sumTree = SegmentTree(nums) { a, b -> a + b }

        assertFailsWith<IndexOutOfBoundsException> {
            sumTree.update(-1, 10)
        }

        assertFailsWith<IndexOutOfBoundsException> {
            sumTree.update(5, 10)
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

        assertEquals(Point(16, 20), sumTree[0..3])
        assertEquals(Point(4, 6), sumTree[0..1])
        assertEquals(Point(8, 10), sumTree[1..2])
        assertEquals(Point(12, 14), sumTree[2..3])
    }

    @Test
    fun testSegmentTreeWithDifferentOperationsOnSameData() {
        val nums = listOf(3, 1, 4, 1, 5, 9, 2, 6)

        val sumTree = SegmentTree(nums) { a, b -> a + b }
        val productTree = SegmentTree(nums) { a, b -> a * b }
        val maxTree = SegmentTree(nums) { a, b -> maxOf(a, b) }
        val minTree = SegmentTree(nums) { a, b -> minOf(a, b) }

        assertEquals(31, sumTree[0..7])
        assertEquals(6480, productTree[0..7])
        assertEquals(9, maxTree[0..7])
        assertEquals(1, minTree[0..7])

        assertEquals(19, sumTree[2..5])
        assertEquals(180, productTree[2..5])
        assertEquals(9, maxTree[2..5])
        assertEquals(1, minTree[2..5])
    }

    @Test
    fun testComplexUpdatesOnSegmentTree() {
        val nums = listOf(1, 3, 5, 7, 9)
        val sumTree = SegmentTree(nums) { a, b -> a + b }

        assertEquals(25, sumTree[0..4])

        sumTree.update(0, 10)
        sumTree.update(2, 15)
        sumTree.update(4, 20)

        assertEquals(55, sumTree[0..4])
        assertEquals(28, sumTree[0..2])
        assertEquals(42, sumTree[2..4])
        assertEquals(25, sumTree[1..3])
    }

    @Test
    fun testSegmentTreeWithSingleElement() {
        val singleList = listOf(42)
        val sumTree = SegmentTree(singleList) { a, b -> a + b }

        assertEquals(42, sumTree[0..0])

        sumTree.update(0, 100)

        assertEquals(100, sumTree[0..0])
    }

    @Test
    fun testMultipleUpdatesAndQueriesInSequence() {
        val nums = listOf(1, 2, 3, 4, 5)
        val sumTree = SegmentTree(nums) { a, b -> a + b }

        assertEquals(15, sumTree[0..4])

        sumTree.update(1, 10)
        assertEquals(23, sumTree[0..4])
        assertEquals(14, sumTree[0..2])

        sumTree.update(3, 20)
        assertEquals(39, sumTree[0..4])
        assertEquals(38, sumTree[1..4])

        sumTree.update(0, 5)
        assertEquals(43, sumTree[0..4])
        assertEquals(15, sumTree[0..1])
    }

    @Test
    fun testBooleanOperationsWithSegmentTree() {
        val booleans = listOf(true, false, true, true, false)

        val orTree = SegmentTree(booleans) { a, b -> a || b }
        assertTrue(orTree[0..4]!!)
        assertTrue(orTree[0..1]!!)
        assertFalse(orTree[1..1]!!)
        assertTrue(orTree[2..3]!!)

        val andTree = SegmentTree(booleans) { a, b -> a && b }
        assertFalse(andTree[0..4]!!)
        assertFalse(andTree[0..1]!!)
        assertTrue(andTree[2..3]!!)
        assertFalse(andTree[3..4]!!)
    }

    private fun gcd(a: Int, b: Int): Int {
        return if (b == 0) a else gcd(b, a % b)
    }

    private fun lcm(a: Int, b: Int): Int {
        return if (a == 0 || b == 0) 0 else (a / gcd(a, b)) * b
    }

    @Test
    fun testGcdSegmentTree() {
        val nums = listOf(48, 36, 120, 24, 16)
        val gcdTree = SegmentTree(nums) { a, b -> gcd(a, b) }

        assertEquals(4, gcdTree[0..4])

        assertEquals(12, gcdTree[0..1])
        assertEquals(12, gcdTree[0..2])
        assertEquals(8, gcdTree[2..4])
        assertEquals(8, gcdTree[3..4])

        assertEquals(48, gcdTree[0..0])
        assertEquals(120, gcdTree[2..2])

        gcdTree.update(1, 18)
        assertEquals(6, gcdTree[0..1])
        assertEquals(6, gcdTree[0..2])

        gcdTree.update(3, 15)
        assertEquals(1, gcdTree[3..4])
    }

    @Test
    fun testLcmSegmentTree() {
        val nums = listOf(4, 6, 8, 10, 12)
        val lcmTree = SegmentTree(nums) { a, b -> lcm(a, b) }

        assertEquals(120, lcmTree[0..4])

        assertEquals(12, lcmTree[0..1])
        assertEquals(24, lcmTree[0..2])
        assertEquals(120, lcmTree[2..4])
        assertEquals(60, lcmTree[3..4])

        assertEquals(4, lcmTree[0..0])
        assertEquals(8, lcmTree[2..2])

        val numsWithZero = listOf(4, 0, 8, 10, 12)
        val lcmTreeWithZero = SegmentTree(numsWithZero) { a, b -> lcm(a, b) }

        assertEquals(0, lcmTreeWithZero[0..1])
        assertEquals(0, lcmTreeWithZero[0..4])
        assertEquals(120, lcmTreeWithZero[2..4])

        lcmTree.update(1, 9)
        assertEquals(36, lcmTree[0..1])

        lcmTree.update(3, 15)
        assertEquals(60, lcmTree[3..4])
    }

    @Test
    fun testCombinedGcdLcmOperations() {
        val nums = listOf(12, 18, 24, 36)

        val gcdTree = SegmentTree(nums) { a, b -> gcd(a, b) }
        val lcmTree = SegmentTree(nums) { a, b -> lcm(a, b) }

        // Test the mathematical identity: gcd(a,b) * lcm(a,b) = a * b (when a and b are positive)
        for (i in 0 until nums.size - 1) {
            for (j in i + 1 until nums.size) {
                val a = nums[i]
                val b = nums[j]
                val calculatedGcd = gcdTree[i..j]!!
                val calculatedLcm = lcmTree[i..j]!!

                if (j - i == 1) {
                    assertEquals(a * b, calculatedGcd * calculatedLcm)
                }
            }
        }

        val gcdOfAll = gcdTree[0..3]!!
        val lcmOfAll = lcmTree[0..3]!!

        for (num in nums) {
            assertEquals(0, lcmOfAll % num)
        }

        for (num in nums) {
            assertEquals(0, num % gcdOfAll)
        }
    }
}