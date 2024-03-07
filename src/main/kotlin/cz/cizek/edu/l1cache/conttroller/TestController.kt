package cz.cizek.edu.l1cache.conttroller

import cz.cizek.edu.l1cache.service.TestService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(private val testService: TestService) {

    @GetMapping("/onlyMain")
    fun onlyMain() {
        testService.cacheItOnlyMain()
        testService.cacheItOnlyMain()
    }

    @GetMapping("/withL1")
    fun withL1() {
        testService.cacheItL1()
        testService.cacheItL1()
    }

}