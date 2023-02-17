package nomic.domain.auth

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("token")
data class JWTTokenConfigurationProperties(
    var signingKeySize: Int = 2048,
    var publicKeyPath: String = "",
    var privateKeyPath: String = ""
)
