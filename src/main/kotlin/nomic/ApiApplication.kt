package nomic

import nomic.data.DatabaseConfigProperties
import nomic.domain.auth.JWTTokenConfigurationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

/**
 * This class serves as the entry point for the Spring Boot application.
 *
 * This only needs to be referenced starting the server in the main function.
 */
@SpringBootApplication
@EnableConfigurationProperties(
    JWTTokenConfigurationProperties::class,
    DatabaseConfigProperties::class
)
class ApiApplication

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}
