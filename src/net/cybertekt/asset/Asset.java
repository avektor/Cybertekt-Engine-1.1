package net.cybertekt.asset;

/**
 * Asset - (C) Cybertekt Software
 *
 * Represents an object constructed from an external resource located at the
 * path specified by an {@link AssetKey asset key}. Implementing subclasses
 * should be effectively immutable as each asset is only loaded once and shared
 * across the entire application.
 *
 * @version 1.1.0
 * @since 1.0.0
 * @author Andrew Vektor
 */
public abstract class Asset {

    /**
     * Asset key that specifies the location of the external resource and its
     * associated file type.
     */
    private final AssetKey KEY;

    /**
     * Constructs an asset and assigns its {@link AssetKey key}.
     *
     * @param key the asset key associated with the asset.
     */
    protected Asset(final AssetKey key) {
        this.KEY = key;
    }

    /**
     * Returns the {@link AssetKey asset key} associated with the asset. The
     * asset key contains the location of the external resource from which the
     * asset is constructed.
     *
     * @return the asset key associated with the asset.
     */
    public final AssetKey getKey() {
        return KEY;
    }
}
