package advanced

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class HierarchyTest {
  private fun sampleHierarchy(): Hierarchy = ArrayBasedHierarchy(
    intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
    intArrayOf(0, 1, 2, 3, 1, 0, 1, 0, 1, 1, 2)
  )

  @Test
  fun `filter keeps all nodes when predicate always true`() {
    val hierarchy = sampleHierarchy()

    val filtered = hierarchy.filter { true }

    assertEquals(hierarchy.formatString(), filtered.formatString())
  }

  @Test
  fun `filter removes entire hierarchy when predicate always false`() {
    val hierarchy = sampleHierarchy()

    val filtered = hierarchy.filter { false }

    assertEquals(0, filtered.size)
    assertEquals("[]", filtered.formatString())
  }

  @Test
  fun `filter removes descendants of nodes that do not satisfy predicate`() {
    val hierarchy = sampleHierarchy()

    val filtered = hierarchy.filter { nodeId -> nodeId % 3 != 0 }

    val expected = ArrayBasedHierarchy(
      intArrayOf(1, 2, 5, 8, 10, 11),
      intArrayOf(0, 1, 1, 0, 1, 2)
    )

    assertEquals(expected.formatString(), filtered.formatString())
  }

  @Test
  fun `filter handles multiple roots independently`() {
    val hierarchy = ArrayBasedHierarchy(
      intArrayOf(1, 2, 3, 4, 5, 6, 7),
      intArrayOf(0, 1, 2, 0, 1, 0, 1)
    )

    val filtered = hierarchy.filter { nodeId -> nodeId != 5 && nodeId != 2 }

    val expectedIds = intArrayOf(1, 6, 7)
    val expectedDepths = intArrayOf(0, 0, 1)

    assertContentEquals(expectedIds, IntArray(filtered.size) { filtered.nodeId(it) })
    assertContentEquals(expectedDepths, IntArray(filtered.size) { filtered.depth(it) })
  }

  @Test
  fun `filter preserves depth alignment for retained nodes`() {
    val hierarchy = ArrayBasedHierarchy(
      intArrayOf(1, 2, 3, 4, 5, 6, 7),
      intArrayOf(0, 1, 2, 3, 1, 0, 1)
    )

    val filtered = hierarchy.filter { nodeId -> nodeId != 4 }

    val expectedDepths = intArrayOf(0, 1, 2, 1, 0, 1)
    assertContentEquals(expectedDepths, IntArray(filtered.size) { filtered.depth(it) })
  }
}
