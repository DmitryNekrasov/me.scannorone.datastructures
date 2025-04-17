package me.scannorone.datastructures

import kotlin.test.*

class OrderedMapSimpleTest {
    @Test
    fun testOrderedMapExists() {
        // Just check if the OrderedMap class exists and can be instantiated
        val map = OrderedMap<String, Int>()
        assertTrue(map.isEmpty())
    }
}