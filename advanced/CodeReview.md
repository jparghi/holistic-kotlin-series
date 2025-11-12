# SimpleCache Code Review

The submitted `SimpleCache` implementation relies on `ConcurrentHashMap`, which is a good
starting point for concurrent access, but several correctness and production-readiness
issues must be addressed before it can be used safely at scale. Below are the most
important findings, why they matter, and concrete improvement ideas.

## 1. Expired entries stay in memory indefinitely
- **Issue**: `get` checks the TTL window but never removes stale entries. Entries with an
  expired timestamp will remain in the map forever, which causes unbounded memory growth
  and pushes more load onto garbage collection.
- **Risk**: In a long-running service with many unique keys, the cache will retain useless
  data and can lead to memory pressure, GC pauses, or even `OutOfMemoryError`.
- **Recommendation**: Remove expired entries lazily when they are detected during `get`
  calls and periodically trigger a cleanup pass (either on a maintenance thread or from
  write operations). This keeps the cache size bounded without adding heavy locking.

## 2. Fixed TTL is inflexible
- **Issue**: The TTL is a hard-coded constant (`60_000` ms). Different data types often
  require different lifetimes, and environments may need to tune cache behavior.
- **Risk**: Deployments cannot adjust freshness guarantees without code changes and a new
  release. Some entries may expire too soon while others linger too long.
- **Recommendation**: Make the TTL configurable via constructor parameter(s) and allow
  callers to override it per entry when necessary. Accepting a `Duration` leads to
  self-documenting code.

## 3. Timestamp stored separately from value causes double lookups
- **Issue**: Each `get` call reads the current time twice (`System.currentTimeMillis()` in
  the predicate and potentially again when re-fetching). Combined with the need to perform
  map lookups more than once in certain flows, this can become a measurable overhead under
  thousands of reads per second.
- **Risk**: Increased latency and CPU usage. Additionally, if the system clock shifts
  backwards, entries may appear valid again.
- **Recommendation**: Capture the current time only once per call and compare using that
  snapshot. Consider injecting a `Clock` to make the behavior testable and robust against
  time shifts.

## 4. No support for concurrent refresh or atomic updates
- **Issue**: `put` overwrites entries without coordination. If two threads compute a new
  value concurrently, the later write wins regardless of freshness. There is no helper to
  compute-or-load a missing value atomically, which is a common cache use-case.
- **Risk**: Duplicate work, thundering herds, and inconsistent values when different
  threads race to populate the cache.
- **Recommendation**: Provide atomic helpers such as `computeIfAbsent`, `getOrPut`, or a
  method that takes a supplier. Use the concurrent map's `compute`/`computeIfPresent`
  primitives to ensure only one winner updates the cache and that TTL bookkeeping stays
  consistent.

## 5. `size()` reports logical entries without considering expiration
- **Issue**: `size()` returns the raw `ConcurrentHashMap` size. Expired items inflate this
  count, making operational metrics misleading.
- **Risk**: Alerting and dashboards cannot rely on the exposed size to understand cache
  health. Operators might think the cache is hot even though most entries are stale.
- **Recommendation**: Run a lazy cleanup before reporting the size or calculate it based
  on live entries only. Alternatively, make the distinction explicit by returning both the
  physical map size and the count of fresh entries.

## 6. No observability hooks
- **Issue**: The implementation offers no insight into hit rate, evictions, or cleanup
  activity. Diagnosing performance problems would be challenging.
- **Risk**: Production incidents take longer to resolve because engineers cannot tell why
  cached values are missing or how quickly entries churn.
- **Recommendation**: Track optional metrics (hits, misses, evictions) via callbacks or a
  pluggable observer. Even lightweight counters guarded by `LongAdder` can be valuable.

Implementing the above improvements will make the cache safer under high concurrency,
reduce stale data, and give operators the knobs and visibility they need in production.
