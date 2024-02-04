package uns.ac.rs.uks.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class RedisEvictConfig {

//    @Autowired
//    private CacheManager cacheManager;
//
//    @EventListener
//    public void onApplicationEvent(ApplicationReadyEvent event) {
//        cacheManager.getCacheNames()
//                .parallelStream()
//                .forEach(n -> cacheManager.getCache(n).clear());
//    }
}
