package tech.jaya.ridely

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class RidelyApplication

fun main(args: Array<String>) {
    runApplication<RidelyApplication>(*args)
}
