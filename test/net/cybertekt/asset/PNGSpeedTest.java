package net.cybertekt.asset;

import java.util.Random;
import net.cybertekt.asset.image.ImageLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dal0119
 */
public class PNGSpeedTest {

    public static final Logger log = LoggerFactory.getLogger(PNGSpeedTest.class);
    
    public static void main(final String[] args) {
        PNGSpeedTest app = new PNGSpeedTest();
        app.init();
    }

    public void init() {
        AssetManager.registerLoader(ImageLoader.class);

        long time = System.nanoTime();
        ImageLoader l = new ImageLoader();
        
        l.loadInline(AssetKey.getKey("Textures/PNG/RGB08.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGB08.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGB16.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGB16.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGBA08.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGBA08.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGBA082.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGBA082.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGBA16.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGBA16.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGB08.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGB08.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGB16.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGB16.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGBA08.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGBA08.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGBA082.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGBA082.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGBA16.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGBA16.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGB08.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGB08.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGB16.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGB16.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGBA08.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGBA08.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGBA082.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGBA082.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGBA16.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGBA16.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGB08.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGB08.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGB16.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGB16.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGBA08.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGBA08.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGBA082.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGBA082.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGBA16.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGBA16.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGB08.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGB08.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGB16.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGB16.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGBA08.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGBA08.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGBA082.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGBA082.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGBA16.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGBA16.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGB08.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGB08.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGB16.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGB16.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGBA08.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGBA08.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGBA082.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGBA082.png")));
        l.loadInline(AssetKey.getKey("Textures/PNG/RGBA16.png"), AssetManager.stream(AssetKey.getKey("Textures/PNG/RGBA16.png")));
        
        long after = System.nanoTime();
        log.info("PNG Loaded in {}ns - {}ms", after - time, (after - time) / 1000000);
    }
}
