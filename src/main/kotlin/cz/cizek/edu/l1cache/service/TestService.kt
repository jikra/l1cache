package cz.cizek.edu.l1cache.service

import cz.cizek.edu.l1cache.aspect.L1Cache
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class TestService {

    @Cacheable(key = "'key'", cacheNames = ["mainCache"])
    @L1Cache(key = "l1key")
    fun cacheItL1() = "DATA"

    @Cacheable(key = "'key'", cacheNames = ["mainCache"])
    fun cacheItOnlyMain() = "DATA"
}