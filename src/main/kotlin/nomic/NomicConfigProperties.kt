package nomic

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "nomic")
data class NomicConfigProperties(
    var dbEndpoint: String = "",
    var dbUsername: String = "",
    var dbPassword: String = "",
    var dbPort: Int = -1,
    var dbName: String = ""
)
