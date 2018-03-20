package net.cybertekt.asset;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import net.cybertekt.asset.Asset;
import net.cybertekt.asset.AssetKey;
import net.cybertekt.asset.AssetManager.AssetInitializationException;
import net.cybertekt.asset.AssetType;

/**
 * Asset Loader - (C) Cybertekt Software.
 *
 * Interface for creating {@link Asset assets} of a specific
 * {@link @AssetType type} loaded from an external resource.
 *
 * @version 1.1.0
 * @since 1.0.0
 * @author Andrew Vektor
 */
public abstract class AssetLoader {

    /**
     * Map that stores asset loading tasks based on the type of asset to be
     * loaded.
     */
    protected final Map<AssetType, AssetTask> TASK_MAP = new HashMap<>();

    /**
     * List of {@link AssetType asset types} supported by the loader.
     */
    protected final List<AssetType> SUPPORTED = new ArrayList<>();

    /**
     * Subclasses must return a callable {@link AssetTask task} which will be
     * used for the concurrent construction of the {@link Asset asset}
     * associated with the specified {@link AssetKey asset key} using the data
     * provided by the {@link InputStream stream}.
     *
     * @param key the {@link AssetKey asset key} associated with the
     * {@link Asset asset} to be loaded.
     * @param stream the input stream for the file located at the path specified
     * by the {@link AssetKey asset key} relative to the root assets directory
     * as defined within the {@link AssetManager#rootDir asset manager}.
     * @return an {@link AssetTask task} for loading the asset associated with
     * the provided {@link AssetKey asset key}.
     */
    public abstract AssetTask newTask(final AssetKey key, final InputStream stream);

    /**
     * Constructs and returns the {@link Asset asset} associated with the
     * provided {@link AssetKey asset key} using the data provided by an input
     * stream.
     *
     * @param key the {@link AssetKey asset key} associated with the
     * {@link Asset asset} to be loaded.
     * @param stream the input stream for the file located at the path specified
     * by the {@link AssetKey asset key} relative to the root assets directory
     * as defined within the {@link AssetManager#rootDir asset manager}.
     * @return the {@link Asset asset} associated with the provided
     * {@link AssetKey asset key} constructed from the data provided by the
     * input stream.
     * @throws net.cybertekt.asset.AssetManager.AssetInitializationException if
     * the {@link Asset asset} cannot be loaded by this loader using the data
     * provided by the input stream.
     */
    public final Asset loadInline(final AssetKey key, final InputStream stream) throws AssetInitializationException {
        return newTask(key, stream).load();
    }

    /**
     * Returns the list of {@link AssetType asset types} supported by the
     * loader.
     *
     * @return the list of asset types supported by the loader.
     */
    public final List<AssetType> getSupportedAssetTypes() {
        return SUPPORTED;
    }
}
