package cz.cizek.edu.l1cache.config

import org.springframework.cache.CacheManager
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.web.context.annotation.RequestScope

@Configuration
class CacheConfig {

    @Bean("mainCacheManager")
    fun mainCacheManager(): CacheManager {
        return ConcurrentMapCacheManager()
    }

    @Bean
    @RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    fun l1CacheManager(): CacheManager = ConcurrentMapCacheManager()

}