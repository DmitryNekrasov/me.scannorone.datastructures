package me.scannorone.datastructures

/**
 * A Disjoint Set (Union-Find) data structure implementation with path compression and union by rank optimizations.
 * This data structure efficiently tracks a set of elements partitioned into a number of disjoint (non-overlapping) subsets.
 *
 * @param size The number of elements in the disjoint set
 */
class DisjointSet(size: Int) {
    // Parent array where parent[i] represents the parent of element i
    private val parent = IntArray(size) { it }
    
    // Rank array to keep track of the approximate depth of each tree
    private val rank = IntArray(size) { 0 }
    
    /**
     * Finds the representative (root) of the set containing the given element.
     * Uses path compression to optimize future queries.
     *
     * @param x The element to find the representative for
     * @return The representative of the set containing x
     * @throws IndexOutOfBoundsException if the element is outside the valid range
     */
    fun find(x: Int): Int {
        if (x !in parent.indices) throw IndexOutOfBoundsException(
            "Element ($x) is out of disjoint set bounds: [0..${parent.size})"
        )
        
        // Path compression: make every examined node point directly to the root
        if (parent[x] != x) {
            parent[x] = find(parent[x])
        }
        return parent[x]
    }
    
    /**
     * Merges the sets containing elements x and y.
     * Uses union by rank to keep the tree balanced.
     *
     * @param x The first element
     * @param y The second element
     * @return True if x and y were in different sets and were successfully merged, false if they were already in the same set
     * @throws IndexOutOfBoundsException if either element is outside the valid range
     */
    fun union(x: Int, y: Int): Boolean {
        val rootX = find(x)
        val rootY = find(y)
        
        // If x and y are already in the same set, no need to merge
        if (rootX == rootY) {
            return false
        }
        
        // Union by rank: attach the smaller rank tree under the root of the higher rank tree
        when {
            rank[rootX] < rank[rootY] -> parent[rootX] = rootY
            rank[rootX] > rank[rootY] -> parent[rootY] = rootX
            else -> {
                // If ranks are the same, make one the parent and increment its rank
                parent[rootY] = rootX
                rank[rootX]++
            }
        }
        
        return true
    }
    
    /**
     * Checks if two elements are in the same set.
     *
     * @param x The first element
     * @param y The second element
     * @return True if x and y are in the same set, false otherwise
     * @throws IndexOutOfBoundsException if either element is outside the valid range
     */
    fun connected(x: Int, y: Int): Boolean {
        return find(x) == find(y)
    }
    
    /**
     * Returns the number of disjoint sets.
     *
     * @return The number of disjoint sets
     */
    fun count(): Int {
        return parent.indices.count { it == parent[it] }
    }
    
    /**
     * Resets the element to be in its own set.
     *
     * @param x The element to reset
     * @throws IndexOutOfBoundsException if the element is outside the valid range
     */
    fun makeSet(x: Int) {
        if (x !in parent.indices) throw IndexOutOfBoundsException(
            "Element ($x) is out of disjoint set bounds: [0..${parent.size})"
        )
        
        parent[x] = x
        rank[x] = 0
    }
}