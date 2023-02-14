package game.manager.nomic.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(NomicConfigProperties::class)
class ApiApplication

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}
