package net.cybertekt.asset;

import java.util.ArrayList;
import java.util.List;
import net.cybertekt.cache.CacheMap;
import net.cybertekt.cache.CacheMap.CacheMode;
import net.cybertekt.cache.CacheMap.MapMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dal0119
 */
public class HashTest {

    public static final Logger log = LoggerFactory.getLogger(HashTest.class);

    private final CacheMap<AssetKey, Float> hashCache = new CacheMap<>(CacheMode.Soft, MapMode.Concurrent);

    private final List<AssetKey> keyList = new ArrayList<>();

    public static void main(final String[] args) {
        HashTest app = new HashTest();
        app.start();
    }

    public void start() {
        long time = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            AssetKey k = AssetKey.getKey("Interface/Test/" + i + ".png");
            hashCache.put(k, 1f);
            keyList.add(k);
        }
        //keyList.stream().forEach((k) -> {
        //    hashCache.get(k);
        //});
        float elapsed = (System.nanoTime() - time) / 1000000;
        log.info("Hash Cache Speed - [{}] Keys Cached - Took [{}ms]", AssetKey.getKeyCount(), (int) elapsed);

    }

    public long getAvgTime() {
        long time = System.nanoTime();
        keyList.stream().forEach((k) -> {
            hashCache.get(k);
        });
        return (System.nanoTime() - time) / keyList.size();
    }

    public long getBestTime() {
        long time = System.nanoTime();
        long best = 5000;
        for (final AssetKey k : keyList) {
            hashCache.get(k);
            if ((System.nanoTime() - time) < best) {
                best = System.nanoTime() - time;
            }
            time = System.nanoTime();
        }
        return best;
    }

}
