package nomic.data

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "db")
data class DatabaseConfigProperties(
    var connectionString: String = "",
    var username: String = "",
    var password: String = ""
)
