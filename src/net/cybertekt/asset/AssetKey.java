package net.cybertekt.asset;

import net.cybertekt.cache.CacheMap;
import net.cybertekt.cache.CacheMap.CacheMode;
import net.cybertekt.cache.CacheMap.MapMode;

/**
 * Asset Key - (C) Cybertekt Software
 *
 * Immutable object that defines the location of an external resource, relative
 * to the {@link AssetManager#rootDir root assets directory}. Asset keys can be
 * constructed and retrieved using the {@link #getKey(java.lang.String)} static
 * utility method provided by this class. Each key is internally cached in order
 * to prevent the construction of multiple asset keys with the same file path.
 * Obsolete asset keys are automatically removed from the key cache when they
 * are no longer in use.
 *
 * @version 1.1.0
 * @since 1.0.0
 * @author Andrew Vektor
 */
public final class AssetKey {

    /**
     * Static {@link CacheMap cache} that contains every unique asset key
     * currently in use by the application.
     */
    private static final CacheMap<String, AssetKey> KEY_CACHE = new CacheMap(CacheMode.Weak, MapMode.Hash);

    /**
     * Each time a new key is created, this value is incremented and assigned as
     * the hash code of the newly created key in order to minimize hash code
     * collisions between keys.
     */
    private static int hashCount = 0;

    /**
     * Retrieves an asset key for the resource located at the specified path.
     * File paths are relative to the {@link AssetManager#rootDir root assets}
     * and are case sensitive. The asset key will be retrieved from the internal
     * asset key cache if it already exists, otherwise a new asset key will be
     * created for the file and added to the key cache.
     *
     * @param path the location of the asset file relative to the
     * {@link AssetManager#rootDir root assets directory}.
     * @return the asset key associated with the resource located at the
     * specified file path.
     */
    public static final AssetKey getKey(String path) {
        if (path.contains("\\")) {
            path = path.replace('\\', '/').toLowerCase();
        }
        AssetKey key = KEY_CACHE.get(path);
        if (key == null) {
            KEY_CACHE.put(path, (key = new AssetKey(path)));
        }
        return key;
    }

    /**
     * Returns the number of asset keys current stored in the key cache.
     *
     * @return the number of cached asset keys.
     */
    public static final int getKeyCount() {
        return KEY_CACHE.size();
    }

    /**
     * The file path of the external asset resource, relative to the
     * {@link AssetManager#rootDir root assets directory}.
     */
    private final String PATH;

    /**
     * The file extension of the external resource.
     */
    private final AssetType TYPE;

    /**
     * The hash code of the asset key.
     */
    private final int HASHCODE;

    /**
     * Constructs a new asset key for the resource located at the specified file
     * path, relative to the {@link AssetManager#rootDir root assets directory}.
     * Keys cannot be constructed directly and instead must be retrieve using
     * the static {@link #getKey(java.lang.String)} utility method.
     *
     * @param path the file path location of the external resource.
     */
    private AssetKey(final String path) {
        PATH = path;
        HASHCODE = AssetKey.hashCount++;
        if (PATH.lastIndexOf('.') > -1) {
            TYPE = AssetType.getType(PATH.substring(PATH.lastIndexOf('.') + 1, PATH.length()));
        } else {
            throw new IllegalArgumentException("Malformed file path [" + PATH + "] - Missing required file type extension");
        }
    }

    /**
     * Returns the {@link AssetType asset type} of the asset key.
     *
     * @return the asset type.
     */
    public final AssetType getType() {
        return TYPE;
    }

    /**
     * Returns the file path of the asset key, relative to the
     * {@link AssetManager#rootDir root asset}.
     *
     * @return the file path of the asset.
     */
    public final String getPath() {
        return PATH;
    }

    /**
     * Returns the absolute path of the asset which includes the
     * {@link AssetManager#rootDir root assets directory}.
     *
     * @return the absolute path of the asset.
     */
    public final String getAbsolutePath() {
        return AssetManager.rootDir + PATH;
    }

    /**
     * Returns the file name of the asset including the file extension.
     *
     * @return the file name and type extension of the asset.
     */
    public final String getName() {
        return getName(true);
    }

    /**
     * Returns the file name of the asset.
     *
     * @param includeFileExtension true to include the file extension.
     * @return the file name of the asset.
     */
    public final String getName(final boolean includeFileExtension) {
        if (includeFileExtension) {
            return PATH.substring(PATH.lastIndexOf('/') + 1);
        }
        return PATH.substring(PATH.lastIndexOf('/') + 1, PATH.lastIndexOf("."));
    }

    /**
     * Returns the file extension of the asset. The file type extension is
     * defined as the characters that follow the last period in the file name.
     *
     * @return the file type extension of the asset.
     */
    public final String getExtension() {
        return TYPE.getExt();
    }

    /**
     * Return the file path of the asset, relative to the
     * {@link AssetManager#rootDir root assets directory}. The String returned
     * by this method appends brackets ('[') to the beginning and end of the
     * file path and therefore should <b>NOT</b> be used to specify the file
     * path to external systems (use {@link #getPath() getPath()} instead). This
     * method is intended for use with logging.
     *
     * @return the asset file path relative to the root assets directory.
     */
    @Override
    public final String toString() {
        return "[" + PATH + "]";
    }

    /**
     * Two keys are only considered to be equal when both point to the same
     * object instance. As there should only ever be a single instance for a
     * provided file path, the file paths are not directly compared. This
     * greatly increases the speed at which keys can be compared and makes them
     * ideal for use with {@link java.util.IdentityHashMap}.
     *
     * @param o the object with which to compare this asset key.
     * @return true if the provided object is the same instance of asset key as
     * this, false otherwise.
     */
    @Override
    public final boolean equals(final Object o) {
        return o == this;
    }

    /**
     * Returns the hash code that was assigned to the asset key during
     * construction.
     *
     * @return the hash code assigned to the asset key.
     */
    @Override
    public final int hashCode() {
        return HASHCODE;
    }
}
