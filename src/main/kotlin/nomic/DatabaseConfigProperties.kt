package nomic

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "db")
data class DatabaseConfigProperties(
    var endpoint: String = "",
    var username: String = "",
    var password: String = "",
    var port: Int = -1,
    var schema: String = "",
)
