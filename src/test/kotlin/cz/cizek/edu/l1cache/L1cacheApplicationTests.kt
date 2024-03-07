package cz.cizek.edu.l1cache

import io.mockk.clearMocks
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.cache.CacheManager
import org.springframework.cache.concurrent.ConcurrentMapCache
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class L1cacheApplicationTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    @Qualifier("mainCacheManager")
    private lateinit var mainCacheManager: CacheManager

    companion object {
        val mainCacheSpy = spyk(ConcurrentMapCache("mainCache"))
    }

    val key = "key"
    val cachedData = "DATA"
    val mainCacheName = "mainCache"

    @AfterEach
    fun teardown() {
        mainCacheSpy.evict(key)
        clearMocks(mainCacheSpy)
    }

    @Test
    fun mainCacheWorks() {

        val mainCache = mainCacheManager.getCache(mainCacheName)!!

        assertThat(mainCache.get(key)).isNull()

        mockMvc.get("/onlyMain")

        verify(exactly = 3) { mainCacheSpy.get(any()) }
        assertThat(mainCache.get(key)?.get()).isEqualTo(cachedData)
    }

    @Test
    fun l1Works() {

        val mainCache = mainCacheManager.getCache(mainCacheName)!!

        //first counted GET call
        assertThat(mainCache.get(key)).isNull()

        mockMvc.get("/withL1")

        //Main cache is checked only once while calling real code
        verify(exactly = 2) { mainCacheSpy.get(any()) }
        verify(exactly = 1) { mainCacheSpy.put(any(), any()) }

        assertThat(mainCache.get(key)?.get()).isEqualTo(cachedData)
    }

    @TestConfiguration
    class TestCache {

        @Bean("mainCacheManager")
        @Primary
        fun mainCacheManager(): CacheManager {
            val concurrentMapCacheManager = spyk<ConcurrentMapCacheManager>()

            every { concurrentMapCacheManager.getCache("mainCache") } returns mainCacheSpy

            return concurrentMapCacheManager
        }
    }


}
