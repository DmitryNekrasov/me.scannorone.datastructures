package me.scannorone.datastructures

import kotlin.test.*

class DisjointSetTest {

    @Test
    fun testInitialization() {
        val dsu = DisjointSet(5)
        
        // Initially, each element should be in its own set
        for (i in 0 until 5) {
            assertEquals(i, dsu.find(i))
        }
        
        // Initially, there should be 5 disjoint sets
        assertEquals(5, dsu.count())
    }
    
    @Test
    fun testUnion() {
        val dsu = DisjointSet(5)
        
        // Union elements 0 and 1
        assertTrue(dsu.union(0, 1))
        
        // Elements 0 and 1 should now be in the same set
        assertTrue(dsu.connected(0, 1))
        
        // Union elements 2 and 3
        assertTrue(dsu.union(2, 3))
        
        // Elements 2 and 3 should now be in the same set
        assertTrue(dsu.connected(2, 3))
        
        // Elements 0 and 2 should still be in different sets
        assertFalse(dsu.connected(0, 2))
        
        // Union elements 0 and 2
        assertTrue(dsu.union(0, 2))
        
        // Now elements 0, 1, 2, and 3 should all be in the same set
        assertTrue(dsu.connected(0, 2))
        assertTrue(dsu.connected(1, 3))
        
        // Element 4 should still be in its own set
        assertFalse(dsu.connected(0, 4))
        
        // There should now be 2 disjoint sets
        assertEquals(2, dsu.count())
    }
    
    @Test
    fun testUnionIdempotence() {
        val dsu = DisjointSet(5)
        
        // First union should succeed
        assertTrue(dsu.union(0, 1))
        
        // Second union of the same elements should fail (they're already in the same set)
        assertFalse(dsu.union(0, 1))
        assertFalse(dsu.union(1, 0))
    }
    
    @Test
    fun testPathCompression() {
        val dsu = DisjointSet(5)
        
        // Create a chain: 0 -> 1 -> 2 -> 3 -> 4
        dsu.union(0, 1)
        dsu.union(1, 2)
        dsu.union(2, 3)
        dsu.union(3, 4)
        
        // Find the representative of element 0
        // This should compress the path so that all elements point directly to the root
        val root = dsu.find(0)
        
        // All elements should now have the same representative
        for (i in 0 until 5) {
            assertEquals(root, dsu.find(i))
        }
        
        // There should be only 1 disjoint set
        assertEquals(1, dsu.count())
    }
    
    @Test
    fun testMakeSet() {
        val dsu = DisjointSet(5)
        
        // Union all elements into one set
        dsu.union(0, 1)
        dsu.union(1, 2)
        dsu.union(2, 3)
        dsu.union(3, 4)
        
        // There should be only 1 disjoint set
        assertEquals(1, dsu.count())
        
        // Reset element 2 to be in its own set
        dsu.makeSet(2)
        
        // Element 2 should now be in its own set
        assertFalse(dsu.connected(1, 2))
        assertFalse(dsu.connected(2, 3))
        
        // Elements 0, 1, 3, and 4 should still be connected
        assertTrue(dsu.connected(0, 1))
        assertTrue(dsu.connected(3, 4))
        assertTrue(dsu.connected(0, 4))
        
        // There should now be 2 disjoint sets
        assertEquals(2, dsu.count())
    }
    
    @Test
    fun testOutOfBoundsExceptions() {
        val dsu = DisjointSet(5)
        
        // Test find with negative index
        assertFailsWith<IndexOutOfBoundsException> {
            dsu.find(-1)
        }
        
        // Test find with index >= size
        assertFailsWith<IndexOutOfBoundsException> {
            dsu.find(5)
        }
        
        // Test union with negative index
        assertFailsWith<IndexOutOfBoundsException> {
            dsu.union(-1, 0)
        }
        
        // Test union with index >= size
        assertFailsWith<IndexOutOfBoundsException> {
            dsu.union(0, 5)
        }
        
        // Test connected with negative index
        assertFailsWith<IndexOutOfBoundsException> {
            dsu.connected(-1, 0)
        }
        
        // Test connected with index >= size
        assertFailsWith<IndexOutOfBoundsException> {
            dsu.connected(0, 5)
        }
        
        // Test makeSet with negative index
        assertFailsWith<IndexOutOfBoundsException> {
            dsu.makeSet(-1)
        }
        
        // Test makeSet with index >= size
        assertFailsWith<IndexOutOfBoundsException> {
            dsu.makeSet(5)
        }
    }
    
    @Test
    fun testLargeDisjointSet() {
        val size = 1000
        val dsu = DisjointSet(size)
        
        // Initially, there should be 'size' disjoint sets
        assertEquals(size, dsu.count())
        
        // Union elements in pairs (0,1), (2,3), etc.
        for (i in 0 until size step 2) {
            if (i + 1 < size) {
                dsu.union(i, i + 1)
            }
        }
        
        // There should now be ceil(size/2) disjoint sets
        val expectedSets = (size + 1) / 2
        assertEquals(expectedSets, dsu.count())
        
        // Union all even-indexed sets together
        for (i in 0 until size step 4) {
            if (i + 2 < size) {
                dsu.union(i, i + 2)
            }
        }
        
        // Check that elements that should be connected are indeed connected
        for (i in 0 until size step 4) {
            if (i + 3 < size) {
                assertTrue(dsu.connected(i, i + 1))
                assertTrue(dsu.connected(i + 2, i + 3))
                assertTrue(dsu.connected(i, i + 2))
                assertTrue(dsu.connected(i, i + 3))
                assertTrue(dsu.connected(i + 1, i + 3))
            }
        }
    }
}