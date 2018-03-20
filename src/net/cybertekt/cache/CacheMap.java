package net.cybertekt.cache;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache Map - (C) Cybertekt Software.
 *
 * Defines a managed map that stores key-value pairs and automatically removes
 * any cached value once it becomes obsolete. Obsolete entries are defined based
 * on the {@link CacheMode} specified during initialization. Key-value pairs are
 * stored in an internally managed {@link java.util.Map}. The type of
 * {@link java.util.Map} to be used is determined based upon the {@link MapMode}
 * provided by the caller when constructing a cache.
 *
 * @version 1.1.0
 * @since 1.0.0
 * @author Andrew Vektor
 *
 * @param <Key> the type of key object for storing and retrieving values.
 * @param <Value> the type of value object to associate with each key.
 */
public class CacheMap<Key, Value> {

    /**
     * Determines when obsolete values will be removed from the cache.
     */
    public static enum CacheMode {

        /**
         * Cached values are tracked with
         * {@link java.lang.ref.SoftReference soft references}.
         */
        Soft,
        /**
         * Cached values are tracked with
         * {@link java.lang.ref.WeakReference weak references}.
         */
        Weak,
        /**
         * Cached values are tracking with
         * {@link java.lang.ref.PhantomReference phantom references}.
         */
        Phantom;
    }

    /**
     * Indicates the type of internal {@link java.util.Map} used for storing and
     * retrieving cached values. Cannot be changed after construction.
     */
    public static enum MapMode {

        /**
         * Key-value pairs are internally stored using an
         * {@link java.util.IdentityHashMap identity hash map}.
         */
        Identity,
        /**
         * Key-value pairs are internally stored using a
         * {@link java.util.HashMap hash map}.
         */
        Hash,
        /**
         * Key-value pairs are internally stored using a
         * {@link java.util.concurrent.ConcurrentHashMap concurrent hash map}.
         */
        Concurrent;
    }

    /**
     * Internal {@link java.util.Map} that stores the cached key-value pairs.
     * The {@link java.util.Map} type is determined by the
     * {@link MapMode map mode} provided during construction of this cache.
     */
    private final Map<Key, Reference<Value>> cache;

    /**
     * Queue in which obsolete values are enqueued for removal from the cache.
     * The {@link java.lang.ref.ReferenceQueue queue} type is determined by the
     * {@link CacheMode} provided during the construction of this cache.
     */
    private final ReferenceQueue<? extends Value> queue = new ReferenceQueue<>();

    /**
     * Defines how each map value is cached and determines when cached values
     * are to be considered obsolete. Obsolete objects will be automatically
     * removed from the cache.
     */
    private final CacheMode cacheMode;

    /**
     * Determines the type of {@link java.util.Map map} used for internally
     * storing the cached key-value pairs.
     */
    private final MapMode mapMode;

    /**
     * Constructs an empty CacheMap using the default
     * {@link CacheMode#Soft Soft} {@link CacheMode} and
     * {@link MapMode#Hash Hash} {@link MapMode}.
     */
    public CacheMap() {
        this(CacheMode.Soft, MapMode.Hash);
    }

    /**
     * Constructs an empty cache with the specified {@link CacheMode} and the
     * default {@link MapMode#Hash Hash} {@link MapMode}.
     *
     * @param cacheMode the {@link CacheMode} that determines how cached values
     * are tracked and when they will be discarded.
     */
    public CacheMap(final CacheMode cacheMode) {
        this(cacheMode, MapMode.Hash);
    }

    /**
     * Constructs an empty cache with the specified {@link MapMode} and the
     * default {@link CacheMode#Soft} {@link CacheMode}.
     *
     * @param mapMode the {@link MapMode} that determines how cached values are
     * stored and retrieved.
     */
    public CacheMap(final MapMode mapMode) {
        this(CacheMode.Soft, mapMode);
    }

    /**
     * Constructs an empty cache with the specified {@link CacheMode} and
     * {@link MapMode}.
     *
     * @param cacheMode the {@link CacheMode} that determines how cached values
     * are tracked and when they will be discarded.
     * @param mapMode the {@link MapMode} that determines how cached values are
     * stored, searched, and retrieved.
     */
    public CacheMap(final CacheMode cacheMode, final MapMode mapMode) {
        this.cacheMode = cacheMode;
        this.mapMode = mapMode;
        switch (this.mapMode) {
            case Identity: {
                cache = new IdentityHashMap<>();
                break;
            }
            case Hash: {
                cache = new HashMap<>();
                break;
            }
            case Concurrent: {
                cache = new ConcurrentHashMap<>();
                break;
            }
            default: {
                cache = new HashMap<>();
                break;
            }
        }
    }

    /**
     * Inserts the specified key-value pair into this cache. If the key already
     * exists within the cache it will be replaced by the specified value.
     * Always returns a reference to the key parameter for the purpose of call
     * chaining.
     *
     * @param key the key to insert into this cache.
     * @param value the value to associate with the specified key.
     * @return the key parameter for the purpose of call chaining.
     */
    public Key put(final Key key, final Value value) {
        update();
        Reference<Value> ref;
        if ((ref = cache.put(key, createReference(key, value))) != null) {
            ((CacheReference<Key>) ref).clear();
        }
        return key;
    }

    /**
     * Retrieves the cached value associated with the specified key. Null will
     * be returned if the specified key does not exist within this cache.
     *
     * @param key the key in which to retrieve the associated cached value.
     * @return the cached value associated with the specified key, or null if
     * the key does not exist within this cache.
     */
    public Value get(final Key key) {
        update();
        Reference<Value> ref;
        return ((ref = cache.get(key)) != null) ? ref.get() : null;
    }

    /**
     * Removes a value from the cache and clears its reference.
     *
     * @param key the key associated with the value to remove.
     */
    public void remove(final Key key) {
        Reference<Value> ref = cache.remove(key);
        if (ref != null) {
            ((CacheReference<Key>) ref).clear();
        }

    }

    /**
     * Polls the internal {@link java.lang.ref.ReferenceQueue} and removes all
     * obsolete values from the cache. This method is called automatically
     * before each operation performed on the cache map.
     */
    public void update() {
        for (Reference<? extends Value> ref = queue.poll(); ref != null; ref = queue.poll()) {
            final Key key;
            if ((key = ((CacheReference<Key>) ref).getKey()) != null) {
                cache.remove(key);
            }
        }
    }

    /**
     * Indicates if the provided key exists as an entry in this cache.
     *
     * @param key the key to check for within the cache.
     * @return true if this cache contains the provided key or false if it does
     * not.
     */
    public final boolean containsKey(final Key key) {
        update();
        return cache.containsKey(key);
    }

    /**
     * Returns the current size of this cache.
     *
     * @return the total number of key-value pairs currently within the cache.
     */
    public final int size() {
        update();
        return cache.size();
    }

    /**
     * Returns true if this cache contains no key-reference pairs.
     *
     * @return true if this cache is empty, false otherwise.
     */
    public final boolean isEmpty() {
        update();
        return cache.isEmpty();
    }

    /**
     * Removes all objects from the cache and clears all references. The cache
     * will be empty when this method returns. References are cleared in order
     * to prevent old objects from triggering the removal of new objects that
     * share the same key as one of the objects already removed from the cache.
     */
    public final void clear() {
        for (Reference<Value> ref : cache.values()) {
            ((CacheReference<Key>) ref).clear();
        }
        cache.clear();
    }

    /**
     * Returns a Set view of the keys contained in this cache. The set is backed
     * by the map, so changes to the cache are reflected in the set, and
     * vice-versa. If the cache is modified while an iteration over the set is
     * in progress (except through the iterator's own remove operation), the
     * results of the iteration are undefined. The set supports element removal,
     * which removes the corresponding mapping from the map, via the
     * Iterator.remove, Set.remove, removeAll, retainAll, and clear operations.
     * It does not support the add or addAll operations.
     *
     * @return a set view of the keys contained in this cache.
     */
    public final Set<Key> keySet() {
        return cache.keySet();
    }

    /**
     * Indicates the {@link CacheMode} that determines how cached values are
     * tracked and when to remove obsolete entries. Cannot be changed after
     * construction.
     *
     * @return the {@link CacheMode} as specified during construction of this
     * cache.
     */
    public final CacheMode getCacheMode() {
        return cacheMode;
    }

    /**
     * Indicates the type of internal {@link java.util.Map} used for storing and
     * retrieving cached values. Cannot be changed after construction.
     *
     * @return the {@link MapMode} as specified during the construction of this
     * cache.
     */
    public final MapMode getMapMode() {
        return mapMode;
    }

    /**
     * Constructs a {@link CacheReference} for the specified key and value. The
     * type of {@link CacheReference} returned is determined by the
     * {@link CacheMode} specified during construction. Any references
     * constructed using this method are automatically enqueued in the
     * {@link CacheMap#queue cache queue}.
     *
     * @param key the key associated with the cached value.
     * @param referent the cached value/referent.
     * @return a new {@link CacheReference} for the specified key and value.
     */
    private Reference<Value> createReference(final Key key, final Value referent) {
        switch (cacheMode) {
            case Soft: {
                return new SoftCacheReference(key, referent, queue);
            }
            case Weak: {
                return new WeakCacheReference(key, referent, queue);
            }
            case Phantom: {
                return new PhantomCacheReference(key, referent, queue);
            }
            default: {
                return new SoftCacheReference(key, referent, queue);
            }
        }
    }

    /**
     * Defines a reference to a cached value. Used to abstract away the three
     * types of cache references. Stores the key associated with the referent
     * which is used to remove obsolete values from the cache.
     *
     * @param <Key> the cache Key.
     */
    private interface CacheReference<Key> {

        /**
         * Returns the key associated with the cached value/referent.
         *
         * @return the key for the cached value/referent.
         */
        public Key getKey();

        /**
         * Clears the reference value and its associated key. This should be
         * called when a object in the cache has been replaced by a new instance
         * that shares the same key. This prevents an obsolete object from
         * triggering the removal of the object that it was replaced by.
         */
        public void clear();
    }

    /**
     * References a softly cached value, meaning cached objects will be removed
     * from the cache when they have no remaining strong references. Softly
     * cached values may not be purged from the cache until memory becomes
     * constrained.
     */
    private class SoftCacheReference extends SoftReference<Value> implements CacheReference<Key> {

        /**
         * The key associated with the cached value/referent.
         */
        private Key key;

        /**
         * Constructs a soft reference for the specified key-value pair to be
         * enqueued in the specified {@link java.lang.ref.ReferenceQueue}.
         *
         * @param key the key associated with the cached value.
         * @param referent the cached value/referent.
         * @param queue the {@link java.lang.ref.ReferenceQueue} in which to
         * enqueue this reference.
         */
        public SoftCacheReference(final Key key, final Value referent, final ReferenceQueue queue) {
            super(referent, queue);
            this.key = key;
        }

        /**
         * Returns the key associated with the cached value/referent.
         *
         * @return the key associated with the cached value.
         */
        @Override
        public final Key getKey() {
            return key;
        }

        /**
         * Clears the reference value and its associated key. This should be
         * called when a object in the cache has been replaced by a new instance
         * that shares the same key. This prevents an obsolete object from
         * triggering the removal of the object that it was replaced by.
         */
        @Override
        public final void clear() {
            super.clear();
            key = null;
        }
    }

    /**
     * References a weakly cached value which will be removed from the cache
     * when it has no remaining strong references.
     */
    private class WeakCacheReference extends WeakReference<Value> implements CacheReference<Key> {

        /**
         * The key associated with the cached value/referent.
         */
        private Key key;

        /**
         * Constructs a weak reference for the specified key-value pair to be
         * enqueued in the specified {@link java.lang.ref.ReferenceQueue}.
         *
         * @param key the key associated with the cached value.
         * @param referent the cached value/referent.
         * @param queue the {@link java.lang.ref.ReferenceQueue} in which to
         * enqueue this reference.
         */
        public WeakCacheReference(final Key key, final Value referent, final ReferenceQueue queue) {
            super(referent, queue);
            this.key = key;
        }

        /**
         * Returns the key associated with the cached value/referent.
         *
         * @return the key associated with the cached value.
         */
        @Override
        public final Key getKey() {
            return key;
        }

        /**
         * Clears the reference value and its associated key. This should be
         * called when a object in the cache has been replaced by a new instance
         * that shares the same key. This prevents an obsolete object from
         * triggering the removal of the object that it was replaced by.
         */
        @Override
        public final void clear() {
            super.clear();
            key = null;
        }
    }

    private class PhantomCacheReference extends PhantomReference<Value> implements CacheReference<Key> {

        /**
         * The key associated with the cached value/referent.
         */
        private Key key;

        /**
         * Constructs a phantom reference for the specified key-value pair to be
         * enqueued in the specified {@link java.lang.ref.ReferenceQueue}.
         *
         * @param key the key associated with the cached value.
         * @param referent the cached value/referent.
         * @param queue the {@link java.lang.ref.ReferenceQueue} in which to
         * enqueue this reference.
         */
        public PhantomCacheReference(final Key key, final Value referent, final ReferenceQueue queue) {
            super(referent, queue);
            this.key = key;
        }

        /**
         * Returns the key associated with the cached value/referent.
         *
         * @return the key associated with the cached value.
         */
        @Override
        public final Key getKey() {
            return key;
        }

        /**
         * Clears the reference value and its associated key. This should be
         * called when a object in the cache has been replaced by a new instance
         * that shares the same key. This prevents an obsolete object from
         * triggering the removal of the object that it was replaced by.
         */
        @Override
        public final void clear() {
            super.clear();
            key = null;
        }
    }
}
