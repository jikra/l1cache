package cz.cizek.edu.l1cache.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder

@Aspect
@Component
@Order(1)
class L1CacheAspect(
    @Qualifier("l1CacheManager") val l1CacheManager: CacheManager
) {

    private val cacheName = "l1Cache"

    @Around("@annotation(l1Cache)")
    @Throws(Throwable::class)
    fun l1Cache(joinPoint: ProceedingJoinPoint, l1Cache: L1Cache): Any {

        //request scope
        if (RequestContextHolder.getRequestAttributes() != null) {
            val cachedValue = l1CacheManager.getCache(cacheName)?.get(l1Cache.key)?.get()
            if (cachedValue != null) return cachedValue
        }

        val r = joinPoint.proceed()

        //request scope
        if (RequestContextHolder.getRequestAttributes() != null) {
            println(Thread.currentThread().name)
            l1CacheManager.getCache(cacheName)?.putIfAbsent(l1Cache.key, r)
        }

        return r
    }
}