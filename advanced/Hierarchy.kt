package advanced

/**
 * A `Hierarchy` stores an ordered forest using parallel arrays of node IDs and depths.
 * Nodes are arranged in depth-first order where the depth of a node reflects how many
 * ancestors precede it. A root node has depth `0`, its children have depth `1`, and so on.
 */
interface Hierarchy {
  /** Number of nodes stored in this hierarchy. */
  val size: Int

  /** Returns the ID for the node located at [index]. */
  fun nodeId(index: Int): Int

  /** Returns the depth for the node located at [index]. */
  fun depth(index: Int): Int

  /**
   * Formats the hierarchy into a string of "node:depth" pairs to simplify test assertions.
   */
  fun formatString(): String = (0 until size).joinToString(
    separator = ", ",
    prefix = "[",
    postfix = "]"
  ) { i -> "${nodeId(i)}:${depth(i)}" }
}

/**
 * Returns a filtered hierarchy that keeps only the nodes accepted by [nodeIdPredicate] *and*
 * whose ancestors are also accepted. In other words, if a node is removed then its entire
 * subtree disappears from the result, preserving a valid forest structure.
 */
fun Hierarchy.filter(nodeIdPredicate: (Int) -> Boolean): Hierarchy {
  if (size == 0) {
    return ArrayBasedHierarchy(IntArray(0), IntArray(0))
  }

  val filteredIds = ArrayList<Int>(size)
  val filteredDepths = ArrayList<Int>(size)
  val inclusionStack = ArrayDeque<Boolean>()

  for (index in 0 until size) {
    val depth = depth(index)
    val id = nodeId(index)

    // The stack mirrors the currently traversed path. Pop nodes that are not ancestors
    // of the current node to keep the stack aligned with the traversal depth.
    while (inclusionStack.size > depth) {
      inclusionStack.removeLast()
    }

    val ancestorsIncluded = inclusionStack.lastOrNull() ?: true
    val isIncluded = nodeIdPredicate(id) && ancestorsIncluded

    inclusionStack.addLast(isIncluded)
    if (isIncluded) {
      filteredIds.add(id)
      filteredDepths.add(depth)
    }
  }

  return ArrayBasedHierarchy(
    filteredIds.toIntArray(),
    filteredDepths.toIntArray()
  )
}

/** Simple immutable hierarchy backed by arrays. */
class ArrayBasedHierarchy(
  private val myNodeIds: IntArray,
  private val myDepths: IntArray,
) : Hierarchy {
  init {
    require(myNodeIds.size == myDepths.size) {
      "Node and depth arrays must have the same length."
    }
  }

  override val size: Int = myNodeIds.size

  override fun nodeId(index: Int): Int = myNodeIds[index]

  override fun depth(index: Int): Int = myDepths[index]
}
