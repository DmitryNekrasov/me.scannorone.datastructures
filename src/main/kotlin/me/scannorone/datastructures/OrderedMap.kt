package me.scannorone.datastructures

/**
 * An implementation of MutableMap that maintains keys in sorted order.
 * This implementation is based on a Red-Black tree to ensure O(log n) complexity
 * for basic operations (get, put, remove) while maintaining tree balance.
 *
 * @param K the type of keys maintained by this map, must be comparable
 * @param V the type of mapped values
 */
class OrderedMap<K : Comparable<K>, V> : MutableMap<K, V> {
    // Red-Black tree node colors
    private enum class Color {
        RED, BLACK
    }

    // Node class for the Red-Black tree
    private inner class Node(
        var key: K?,
        var value: V?,
        var color: Color = Color.RED,
        var left: Node? = null,
        var right: Node? = null,
        var parent: Node? = null
    ) {
        // Helper method to check if this is a sentinel node
        fun isSentinel(): Boolean = key == null && value == null && color == Color.BLACK
    }

    // Root of the tree
    private var root: Node? = null

    // Size of the map
    private var _size: Int = 0

    // Sentinel NIL node (used for leaf nodes)
    private val NIL = Node(key = null, value = null, color = Color.BLACK)

    init {
        // Initialize root as NIL
        root = NIL
    }

    /**
     * Returns the number of key-value pairs in the map.
     */
    override val size: Int
        get() = _size

    /**
     * Returns true if the map contains no elements.
     */
    override fun isEmpty(): Boolean = _size == 0

    /**
     * Returns true if the map contains the specified key.
     */
    override fun containsKey(key: K): Boolean = findNode(key) != NIL

    /**
     * Returns true if the map maps one or more keys to the specified value.
     */
    override fun containsValue(value: V): Boolean {
        // Traverse the tree using the efficient iterator
        val iterator = TreeIterator()
        while (iterator.hasNext()) {
            if (iterator.next().value == value) {
                return true
            }
        }
        return false
    }

    /**
     * Returns the value corresponding to the given key, or null if such a key is not present in the map.
     */
    override fun get(key: K): V? {
        val node = findNode(key)
        return if (node != NIL) node.value else null
    }

    /**
     * Associates the specified value with the specified key in the map.
     * @return the previous value associated with the key, or null if the key was not present in the map
     */
    override fun put(key: K, value: V): V? {
        val oldValue = insertNode(key, value)
        return oldValue
    }

    /**
     * Removes the specified key and its corresponding value from this map.
     * @return the previous value associated with the key, or null if the key was not present in the map
     */
    override fun remove(key: K): V? {
        val node = findNode(key)
        if (node == NIL) return null

        val oldValue = node.value
        deleteNode(node)
        return oldValue
    }

    /**
     * Updates this map with key-value pairs from the specified map.
     */
    override fun putAll(from: Map<out K, V>) {
        from.forEach { (key, value) -> put(key, value) }
    }

    /**
     * Removes all elements from this map.
     */
    override fun clear() {
        root = NIL
        _size = 0
    }

    /**
     * Returns a MutableSet of all keys in this map.
     */
    override val keys: MutableSet<K>
        get() = object : AbstractMutableSet<K>() {
            override val size: Int
                get() = this@OrderedMap.size

            override fun add(element: K): Boolean {
                throw UnsupportedOperationException("Keys set doesn't support adding elements directly")
            }

            override fun iterator(): MutableIterator<K> {
                return object : MutableIterator<K> {
                    private val nodeIterator = TreeIterator()
                    private var lastNode: Node? = null

                    override fun hasNext(): Boolean = nodeIterator.hasNext()

                    override fun next(): K {
                        lastNode = nodeIterator.next()
                        return lastNode!!.key!!
                    }

                    override fun remove() {
                        checkNotNull(lastNode) { "Call next() before remove()" }
                        this@OrderedMap.remove(lastNode!!.key)
                        lastNode = null
                    }
                }
            }

            override fun contains(element: K): Boolean = this@OrderedMap.containsKey(element)

            override fun clear() {
                this@OrderedMap.clear()
            }

            override fun remove(element: K): Boolean {
                return this@OrderedMap.remove(element) != null
            }
        }

    /**
     * Returns a MutableCollection of all values in this map.
     */
    override val values: MutableCollection<V>
        get() = object : AbstractMutableCollection<V>() {
            override val size: Int
                get() = this@OrderedMap.size

            override fun add(element: V): Boolean {
                throw UnsupportedOperationException("Values collection doesn't support adding elements directly")
            }

            override fun iterator(): MutableIterator<V> {
                return object : MutableIterator<V> {
                    private val nodeIterator = TreeIterator()
                    private var lastNode: Node? = null

                    override fun hasNext(): Boolean = nodeIterator.hasNext()

                    override fun next(): V {
                        lastNode = nodeIterator.next()
                        return lastNode!!.value!!
                    }

                    override fun remove() {
                        checkNotNull(lastNode) { "Call next() before remove()" }
                        this@OrderedMap.remove(lastNode!!.key)
                        lastNode = null
                    }
                }
            }

            override fun contains(element: V): Boolean = this@OrderedMap.containsValue(element)

            override fun clear() {
                this@OrderedMap.clear()
            }
        }

    /**
     * Returns a MutableSet of all key-value pairs in this map.
     */
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = object : AbstractMutableSet<MutableMap.MutableEntry<K, V>>() {
            override val size: Int
                get() = this@OrderedMap.size

            override fun add(element: MutableMap.MutableEntry<K, V>): Boolean {
                val oldValue = this@OrderedMap.put(element.key, element.value)
                return oldValue != element.value
            }

            override fun iterator(): MutableIterator<MutableMap.MutableEntry<K, V>> {
                return object : MutableIterator<MutableMap.MutableEntry<K, V>> {
                    private val nodeIterator = TreeIterator()
                    private var lastNode: Node? = null

                    override fun hasNext(): Boolean = nodeIterator.hasNext()

                    override fun next(): MutableMap.MutableEntry<K, V> {
                        lastNode = nodeIterator.next()
                        return object : MutableMap.MutableEntry<K, V> {
                            override val key: K
                                get() = lastNode!!.key!!
                            override val value: V
                                get() = lastNode!!.value!!

                            override fun setValue(newValue: V): V {
                                val oldValue = lastNode!!.value!!
                                lastNode!!.value = newValue
                                return oldValue
                            }
                        }
                    }

                    override fun remove() {
                        checkNotNull(lastNode) { "Call next() before remove()" }
                        this@OrderedMap.remove(lastNode!!.key)
                        lastNode = null
                    }
                }
            }

            override fun clear() {
                this@OrderedMap.clear()
            }

            override fun contains(element: MutableMap.MutableEntry<K, V>): Boolean {
                val value = this@OrderedMap.get(element.key)
                return value != null && value == element.value
            }

            override fun remove(element: MutableMap.MutableEntry<K, V>): Boolean {
                if (contains(element)) {
                    this@OrderedMap.remove(element.key)
                    return true
                }
                return false
            }
        }

    // Helper method to find a node with the given key
    private fun findNode(key: K): Node {
        var current = root
        while (current != NIL) {
            val cmp = key.compareTo(current!!.key!!)
            when {
                cmp < 0 -> current = current.left
                cmp > 0 -> current = current.right
                else -> return current
            }
        }
        return NIL
    }

    // Helper method to insert a node with the given key and value
    private fun insertNode(key: K, value: V): V? {
        var y: Node? = NIL
        var x: Node? = root

        // Find the position to insert the new node
        while (x != NIL) {
            y = x
            val cmp = key.compareTo(x!!.key!!)
            when {
                cmp < 0 -> x = x.left
                cmp > 0 -> x = x.right
                else -> {
                    // Key already exists, update value and return old value
                    val oldValue = x.value
                    x.value = value
                    return oldValue
                }
            }
        }

        // Create new node
        val newNode = Node(key, value, Color.RED, NIL, NIL, y)

        // Insert the new node into the tree
        when {
            y == NIL -> root = newNode
            key.compareTo(y!!.key!!) < 0 -> y.left = newNode
            else -> y.right = newNode
        }

        // Fix the tree to maintain Red-Black properties
        if (y == NIL) {
            // Root node is always black
            newNode.color = Color.BLACK
        } else {
            fixInsert(newNode)
        }

        _size++
        return null
    }

    // Helper method to fix the tree after insertion
    private fun fixInsert(node: Node) {
        var current = node

        // Fix Red-Black tree properties
        while (current != root && current.parent?.color == Color.RED) {
            if (current.parent == current.parent?.parent?.left) {
                // Parent is left child of grandparent
                val uncle = current.parent?.parent?.right

                if (uncle?.color == Color.RED) {
                    // Case 1: Uncle is red
                    current.parent?.color = Color.BLACK
                    uncle.color = Color.BLACK
                    current.parent?.parent?.color = Color.RED
                    current = current.parent?.parent ?: break
                } else {
                    if (current == current.parent?.right) {
                        // Case 2: Current is right child
                        current = current.parent ?: break
                        leftRotate(current)
                    }
                    // Case 3: Current is left child
                    current.parent?.color = Color.BLACK
                    current.parent?.parent?.color = Color.RED
                    rightRotate(current.parent?.parent ?: break)
                }
            } else {
                // Parent is right child of grandparent
                val uncle = current.parent?.parent?.left

                if (uncle?.color == Color.RED) {
                    // Case 1: Uncle is red
                    current.parent?.color = Color.BLACK
                    uncle.color = Color.BLACK
                    current.parent?.parent?.color = Color.RED
                    current = current.parent?.parent ?: break
                } else {
                    if (current == current.parent?.left) {
                        // Case 2: Current is left child
                        current = current.parent ?: break
                        rightRotate(current)
                    }
                    // Case 3: Current is right child
                    current.parent?.color = Color.BLACK
                    current.parent?.parent?.color = Color.RED
                    leftRotate(current.parent?.parent ?: break)
                }
            }
        }

        // Ensure root is black
        root?.color = Color.BLACK
    }

    // Helper method to delete a node
    private fun deleteNode(node: Node) {
        var z = node
        var y = z
        var yOriginalColor = y.color
        var x: Node?

        // Fast path for leaf nodes or nodes with only one child
        when {
            z.left == NIL -> {
                // Case 1: Node has no left child
                x = z.right
                transplant(z, z.right!!)
            }
            z.right == NIL -> {
                // Case 2: Node has no right child
                x = z.left
                transplant(z, z.left!!)
            }
            else -> {
                // Case 3: Node has both children
                // Find the successor (minimum in right subtree)
                y = minimum(z.right!!)
                yOriginalColor = y.color
                x = y.right

                // Special case: successor is immediate right child
                if (y.parent == z) {
                    // Just set parent pointer for x
                    x?.parent = y
                } else {
                    // Replace successor with its right child
                    transplant(y, y.right!!)
                    // Update successor's right pointer
                    y.right = z.right
                    y.right?.parent = y
                }

                // Replace z with its successor
                transplant(z, y)
                // Update successor's left pointer
                y.left = z.left
                y.left?.parent = y
                // Preserve z's color
                y.color = z.color
            }
        }

        // If the original color was black, we need to fix the tree
        if (yOriginalColor == Color.BLACK) {
            fixDelete(x ?: NIL)
        }

        _size--
    }

    // Helper method to fix the tree after deletion
    private fun fixDelete(node: Node) {
        var x = node

        // If x is red, we can just color it black and be done
        if (x.color == Color.RED) {
            x.color = Color.BLACK
            return
        }

        // If x is the root, we're done (root is always black)
        if (x == root) {
            return
        }

        while (x != root && x.color == Color.BLACK) {
            val parent = x.parent ?: break

            if (x == parent.left) {
                // X is left child
                var w = parent.right ?: break

                if (w.color == Color.RED) {
                    // Case 1: Sibling is red
                    w.color = Color.BLACK
                    parent.color = Color.RED
                    leftRotate(parent)
                    w = parent.right ?: break
                }

                val wLeftBlack = w.left?.color == Color.BLACK
                val wRightBlack = w.right?.color == Color.BLACK

                if (wLeftBlack && wRightBlack) {
                    // Case 2: Sibling has two black children
                    w.color = Color.RED
                    x = parent
                } else {
                    if (wRightBlack) {
                        // Case 3: Sibling's right child is black
                        w.left?.color = Color.BLACK
                        w.color = Color.RED
                        rightRotate(w)
                        w = parent.right ?: break
                    }

                    // Case 4: Sibling's right child is red
                    w.color = parent.color
                    parent.color = Color.BLACK
                    w.right?.color = Color.BLACK
                    leftRotate(parent)
                    x = root ?: break
                }
            } else {
                // X is right child
                var w = parent.left ?: break

                if (w.color == Color.RED) {
                    // Case 1: Sibling is red
                    w.color = Color.BLACK
                    parent.color = Color.RED
                    rightRotate(parent)
                    w = parent.left ?: break
                }

                val wLeftBlack = w.left?.color == Color.BLACK
                val wRightBlack = w.right?.color == Color.BLACK

                if (wRightBlack && wLeftBlack) {
                    // Case 2: Sibling has two black children
                    w.color = Color.RED
                    x = parent
                } else {
                    if (wLeftBlack) {
                        // Case 3: Sibling's left child is black
                        w.right?.color = Color.BLACK
                        w.color = Color.RED
                        leftRotate(w)
                        w = parent.left ?: break
                    }

                    // Case 4: Sibling's left child is red
                    w.color = parent.color
                    parent.color = Color.BLACK
                    w.left?.color = Color.BLACK
                    rightRotate(parent)
                    x = root ?: break
                }
            }
        }

        x.color = Color.BLACK
    }

    // Helper method to perform left rotation
    private fun leftRotate(x: Node) {
        val y = x.right ?: return

        // Turn y's left subtree into x's right subtree
        x.right = y.left
        if (y.left != NIL) {
            y.left?.parent = x
        }

        // Link x's parent to y
        y.parent = x.parent
        when {
            x.parent == NIL -> root = y
            x == x.parent?.left -> x.parent?.left = y
            else -> x.parent?.right = y
        }

        // Put x on y's left
        y.left = x
        x.parent = y
    }

    // Helper method to perform right rotation
    private fun rightRotate(y: Node) {
        val x = y.left ?: return

        // Turn x's right subtree into y's left subtree
        y.left = x.right
        if (x.right != NIL) {
            x.right?.parent = y
        }

        // Link y's parent to x
        x.parent = y.parent
        when {
            y.parent == NIL -> root = x
            y == y.parent?.left -> y.parent?.left = x
            else -> y.parent?.right = x
        }

        // Put y on x's right
        x.right = y
        y.parent = x
    }

    // Helper method to replace one subtree with another
    private fun transplant(u: Node, v: Node) {
        when {
            u.parent == NIL -> root = v
            u == u.parent?.left -> u.parent?.left = v
            else -> u.parent?.right = v
        }
        v.parent = u.parent
    }

    // Helper method to find the minimum node in a subtree
    private fun minimum(node: Node): Node {
        var current = node
        // Fast path for leaf nodes
        if (current.left == NIL) return current

        // Traverse left until we reach the leftmost node
        while (current.left != NIL) {
            current = current.left!!
        }
        return current
    }

    // Helper method to perform in-order traversal of the tree
    private fun inOrderTraversal(node: Node?): List<Node> {
        if (node == null || node == NIL) return emptyList()

        val result = mutableListOf<Node>()
        result.addAll(inOrderTraversal(node.left))
        result.add(node)
        result.addAll(inOrderTraversal(node.right))
        return result
    }

    // Iterator class for efficient in-order traversal without building a full list
    private inner class TreeIterator : Iterator<Node> {
        private val stack = ArrayDeque<Node>()
        private var current: Node? = null

        init {
            // Initialize the stack with leftmost path
            pushLeftPath(root)
        }

        private fun pushLeftPath(node: Node?) {
            var current = node
            while (current != null && current != NIL) {
                stack.addLast(current)
                current = current.left
            }
        }

        override fun hasNext(): Boolean = stack.isNotEmpty()

        override fun next(): Node {
            if (!hasNext()) throw NoSuchElementException()

            // Get the next node in in-order traversal
            val node = stack.removeLast()
            current = node

            // If this node has a right child, push the left path starting from the right child
            if (node.right != null && node.right != NIL) {
                pushLeftPath(node.right)
            }

            return node
        }
    }
}
