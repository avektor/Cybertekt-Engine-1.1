package net.cybertekt.asset;

import net.cybertekt.asset.image.ImageLoader;
import net.cybertekt.asset.shader.ShaderLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Andrew Vektor
 */
public class AssetTest {

    public static final Logger log = LoggerFactory.getLogger(AssetTest.class);

    public static final String[] paths = {"Textures/PNG/RGBA082.png", "Textures/PNG/LUM8.png", "Textures/PNG/LUMA8.png", "Textures/PNG/RGB08.png", "Textures/PNG/RGBA16.png", "Textures/PNG/IDX8.png"};

    public static void main(final String[] args) {
        AssetTest app = new AssetTest();
        app.init();
    }

    public void init() {
        // Register Asset Loaders //
        AssetManager.registerLoader(ImageLoader.class, AssetType.getType("PNG"));
        AssetManager.registerLoader(ShaderLoader.class, AssetType.getType("VERT"), AssetType.getType("FRAG"));
        log.info("Application Started - Asset Count is {} - Total Available Processors is {}", AssetManager.getLoaded(), Runtime.getRuntime().availableProcessors());
        long time = System.nanoTime();

        // Remove the fallback asset and a runtime exception will occur if you attempt to retrieve a non-existent or invalid PNG asset such as "Textures/PNG/Bad.png" or "Textures/PNG/DoesNotExist.png".
        AssetManager.setFallback(AssetManager.get("Textures/PNG/Grayscale.png"), AssetType.getType("PNG"));
        
        //Load Images
        AssetManager.load("Textures/PNG/Grayscale.png"); //Already loaded as a fallback asset. Asset Manager should prevent the same asset from being loaded more than once.
        AssetManager.load("Textures/PNG/IDX8.png");
        AssetManager.load("Textures/PNG/LUM8.png");
        AssetManager.load("Textures/PNG/LUMA8.png");
        AssetManager.load("Textures/PNG/RGB08.png");
        AssetManager.load("Textures/PNG/RGB16.png");
        AssetManager.load("Textures/PNG/RGBA08.png");
        AssetManager.load("Textures/PNG/RGBA082.png");
        AssetManager.load("Textures/PNG/RGBA16.png");
        
        //Load Shaders
        AssetManager.load("Shaders/solid.vert");
        AssetManager.load("Shaders/solid.frag");
        
        //No Exception Thrown Here If A Fallback Asset Has Been Registered For PNG Asset Types.
        AssetManager.load("Textures/PNG/Bad.png"); //Not A Valid PNG Image File.
        AssetManager.load("Textures/PNG/DoesNotExist.png"); //Image File Does Not Exist.
        AssetManager.load("Textures/PNG/DoesNotExist2.png"); //Image File Does Not Exist.
        
        long loops = 0;
        log.info("Now Loading ... {}/{}/{} - {}%", AssetManager.getLoaded(), AssetManager.getFailed(), AssetManager.getRequested(), String.format("%.0f", AssetManager.getProgress() * 100f));
        while (AssetManager.isLoading()) {
            log.info("Now Loading ... {}/{}/{} - {}%", AssetManager.getLoaded(), AssetManager.getFailed(), AssetManager.getRequested(), String.format("%.0f", AssetManager.getProgress() * 100f));
            loops++;
        }
        log.info("Loading Done - {}/{}/{} - {}% ({} Threads - {} Loops)", AssetManager.getLoaded(), AssetManager.getFailed(), AssetManager.getRequested(), String.format("%.0f", AssetManager.getProgress() * 100f), AssetManager.getPoolSize(), loops);
        log.info("Loaded {} of {} assets in {}ms using {} thread(s) - {} Cached - (Active Threads: {})", AssetManager.getLoaded(), AssetManager.getRequested(), Math.round((float) (System.nanoTime() - time) / 1000000L), AssetManager.getPoolSize(), AssetManager.getCacheSize(), Thread.activeCount());

        
        //AssetManager.cachedAssets.keySet().forEach((a) -> {
        //    LOG.info("Cached Asset Key - {}", a.getName());
        //});
    }
}
