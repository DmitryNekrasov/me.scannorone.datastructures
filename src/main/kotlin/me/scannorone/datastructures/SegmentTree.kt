package me.scannorone.datastructures

import kotlin.math.max
import kotlin.math.min

/**
 * A generic segment tree implementation that supports range queries and single element updates.
 * @param source The initial collection of elements
 * @param operation The binary operation to be applied on tree nodes (e.g., min, max, sum)
 */
class SegmentTree<T>(source: Collection<T>, private val operation: (T, T) -> T) {

    private val size = source.size
    private val tree = MutableList<T?>(size * 4) { null }

    init {
        if (source.isNotEmpty()) {
            build(source, 1, 0, size - 1)
        }
    }

    /**
     * Queries the segment tree for a result in the given range.
     * @param range The range to query
     * @return The result of the operation applied to all elements in the range, or null if invalid range
     */
    operator fun get(range: IntRange): T? {
        if (size == 0 || range.start < 0 || range.endInclusive >= size) {
            return null
        }
        return queryRange(1, 0, size - 1, range.start, range.endInclusive)
    }


    /**
     * Queries the segment tree for a result in the given range, returning a default value if the range is invalid or empty.
     * @param range The range to query
     * @param default The default value to return if the query range is invalid or no result exists.
     * @return The result of the operation applied to all elements in the range, or the default value if invalid range.
     */
    fun getOrDefault(range: IntRange, default: T): T {
        return this[range] ?: default
    }

    /**
     * Updates the value at a specific index in the segment tree.
     * @param index The index to update
     * @param newValue The new value to set
     * @throws IndexOutOfBoundsException if the index is outside the valid range
     */
    fun update(index: Int, newValue: T) {
        if (index !in 0..<size) throw IndexOutOfBoundsException(
            "index ($index) is out of segment tree bounds: [0..$size)"
        )
        update(1, 0, size - 1, index, newValue)
    }

    private fun build(source: Collection<T>, nodeIndex: Int, start: Int, end: Int) {
        if (start == end) {
            tree[nodeIndex] = source.elementAt(start)
        } else {
            val mid = start + (end - start) / 2
            val leftNodeIndex = nodeIndex * 2
            val rightNodeIndex = nodeIndex * 2 + 1

            build(source, leftNodeIndex, start, mid)
            build(source, rightNodeIndex, mid + 1, end)

            tree[nodeIndex] = operation(tree[leftNodeIndex]!!, tree[rightNodeIndex]!!)
        }
    }

    private fun update(nodeIndex: Int, start: Int, end: Int, index: Int, newValue: T) {
        if (start == end) {
            tree[nodeIndex] = newValue
        } else {
            val mid = start + (end - start) / 2
            val leftNodeIndex = nodeIndex * 2
            val rightNodeIndex = nodeIndex * 2 + 1

            if (index <= mid) {
                update(leftNodeIndex, start, mid, index, newValue)
            } else {
                update(rightNodeIndex, mid + 1, end, index, newValue)
            }

            tree[nodeIndex] = operation(tree[leftNodeIndex]!!, tree[rightNodeIndex]!!)
        }
    }

    private fun queryRange(nodeIndex: Int, start: Int, end: Int, queryStart: Int, queryEnd: Int): T? {
        return when {
            queryStart > queryEnd -> null
            queryStart == start && queryEnd == end -> tree[nodeIndex]
            else -> {
                val mid = start + (end - start) / 2
                val leftResult = queryRange(nodeIndex * 2, start, mid, queryStart, min(queryEnd, mid))
                val rightResult = queryRange(nodeIndex * 2 + 1, mid + 1, end, max(queryStart, mid + 1), queryEnd)
                when {
                    leftResult == null -> rightResult
                    rightResult == null -> leftResult
                    else -> operation(leftResult, rightResult)
                }
            }
        }
    }
}