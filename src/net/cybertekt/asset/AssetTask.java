package net.cybertekt.asset;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import net.cybertekt.asset.AssetManager.AssetInitializationException;

/**
 * Asset Task - (C) Cybertekt Software
 *
 * Callable class that constructs an {@link Asset asset}.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public abstract class AssetTask implements Callable {

    /**
     * The {@link AssetKey asset key} associated with the {@link Asset asset} to
     * be loaded.
     */
    protected final AssetKey KEY;

    /**
     * An input stream that provides the data to be used for constructing the
     * {@link Asset asset}.
     */
    protected final InputStream INPUT;

    /**
     * The {@link Asset asset} loaded by this task.
     */
    private Asset asset;

    /**
     * Constructs a new task for loading the {@link Asset asset} associated with
     * the provided {@link AssetKey asset key} and input stream.
     *
     * @param key the {@link AssetKey asset key} associated with the
     * {@link Asset asset} to be loaded by this task.
     * @param input the input stream containing the data for the external file
     * from which to load the {@link Asset asset}.
     */
    public AssetTask(final AssetKey key, final InputStream input) {
        this.KEY = key;
        this.INPUT = input;
    }

    /**
     * Begins loading the {@link Asset asset} associated with the
     * {@link AssetKey asset key} and input stream provided to this task during
     * constructing if it has not already been loaded. If the
     * {@link Asset asset} has already been loaded, this method will return the
     * previously loaded asset immediately.
     *
     * @return the {@link Asset asset} loaded by this task.
     * @throws net.cybertekt.asset.AssetManager.AssetInitializationException if
     * the loader is unable to load construct the asset.
     */
    @Override
    public final Asset call() throws AssetInitializationException {
        if (asset == null) {
            try {
                asset = load();
            } finally {
                try {
                    INPUT.close();
                } catch (final IOException e) {
                    throw new AssetInitializationException(KEY, e.getMessage());
                }
            }
        }
        return asset;
    }

    /**
     * Constructs and returns the {@link Asset asset} associated with the
     * {@link AssetKey asset key} and input stream provided to this task during
     * construction. Note that the input stream does not need to be manually
     * closed by the subclass. Input streams are automatically closed once this
     * method returns.
     *
     * @return the {@link Asset asset} associated with the
     * {@link AssetKey asset key} and input stream provided to this task during
     * construction.
     * @throws net.cybertekt.asset.AssetManager.AssetInitializationException if
     * the loader is unable to construct the asset.
     */
    public abstract Asset load() throws AssetInitializationException;

    /**
     * Returns the {@link AssetKey asset key} associated with the
     * {@link Asset asset} to be loaded by this task.
     *
     * @return the {@link AssetKey asset key} associated with the
     * {@link Asset asset} to be loaded by this task.
     */
    public final AssetKey getKey() {
        return KEY;
    }

    /**
     * Returns the {@link Asset asset} loaded by this task or null if the asset
     * has not yet been loaded.
     *
     * @return the {@link Asset asset} loaded by this task or null if the asset
     * has not yet been loaded.
     */
    public final Asset getAsset() {
        return asset;
    }
}
