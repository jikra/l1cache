package cz.cizek.edu.l1cache

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class L1cacheApplication

fun main(args: Array<String>) {
    runApplication<L1cacheApplication>(*args)
}
