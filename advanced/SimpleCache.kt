package advanced

import java.time.Clock
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * A small concurrent cache with pluggable TTL behaviour. Entries are lazily
 * cleaned up on read/write operations to avoid dedicated maintenance threads
 * while still preventing unbounded growth.
 */
class SimpleCache<K : Any, V : Any>(
  private val clock: Clock = Clock.systemUTC(),
  private val defaultTtl: Duration? = Duration.ofMinutes(1),
  private val map: ConcurrentMap<K, CacheEntry<V>> = ConcurrentHashMap(),
) {
  data class CacheEntry<V : Any>(val value: V, val expiresAtMillis: Long?) {
    fun isExpired(nowMillis: Long): Boolean = expiresAtMillis?.let { nowMillis >= it } ?: false
  }

  /** Stores [value] for [key] with an optional [ttl]. */
  fun put(key: K, value: V, ttl: Duration? = defaultTtl) {
    val expiresAt = ttl?.let { clock.millis() + it.toMillis() }
    map[key] = CacheEntry(value, expiresAt)
  }

  /** Returns a cached value or `null` if it is missing or expired. */
  fun get(key: K): V? {
    val now = clock.millis()
    val entry = map[key] ?: return null
    if (entry.isExpired(now)) {
      map.remove(key, entry)
      return null
    }
    return entry.value
  }

  /**
   * Atomically loads a value if none exists or the stored entry is expired.
   * The supplier executes at most once per successful cache update.
   */
  fun getOrCompute(key: K, ttl: Duration? = defaultTtl, supplier: () -> V): V {
    while (true) {
      val now = clock.millis()
      val existing = map[key]
      if (existing != null && !existing.isExpired(now)) {
        return existing.value
      }

      val freshValue = supplier()
      val newEntry = CacheEntry(
        value = freshValue,
        expiresAtMillis = ttl?.let { now + it.toMillis() }
      )

      if (existing == null) {
        if (map.putIfAbsent(key, newEntry) == null) {
          return freshValue
        }
      } else if (map.replace(key, existing, newEntry)) {
        return freshValue
      }
    }
  }

  /** Removes [key] and returns `true` if an entry was deleted. */
  fun invalidate(key: K): Boolean = map.remove(key) != null

  /** Removes all expired entries. Useful for scheduled maintenance. */
  fun cleanupExpired() {
    val now = clock.millis()
    map.entries.removeIf { (_, entry) -> entry.isExpired(now) }
  }

  /** Returns the number of live (non-expired) entries. */
  fun size(): Int {
    cleanupExpired()
    return map.size
  }
}
