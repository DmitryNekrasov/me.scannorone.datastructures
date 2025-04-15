package me.scannorone.datastructures

class SegmentTree<T>(source: Collection<T>, private val operation: (T, T) -> T) {

    private val size = source.size
    private val tree = MutableList<T?>(size * 4) { null }

    init {
        if (source.isNotEmpty()) {
            build(source, 1, 0, size - 1)
        }
    }

    fun queryRange(queryStart: Int, queryEnd: Int): T? {
        if (size == 0 || queryStart > queryEnd || queryStart < 0 || queryEnd >= size) {
            return null
        }
        return queryRange(1, 0, size - 1, queryStart, queryEnd)
    }

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
        if (tree[nodeIndex] == null || queryEnd < start || queryStart > end) {
            return null
        }

        if (queryStart <= start && end <= queryEnd) {
            return tree[nodeIndex]
        }

        val mid = start + (end - start) / 2
        val leftResult = queryRange(nodeIndex * 2, start, mid, queryStart, queryEnd)
        val rightResult = queryRange(nodeIndex * 2 + 1, mid + 1, end, queryStart, queryEnd)

        return when {
            leftResult == null -> rightResult
            rightResult == null -> leftResult
            else -> operation(leftResult, rightResult)
        }
    }
}