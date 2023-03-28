package nomic.domain.auth

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * An object using Spring's configuration properties system to encapsulate configuration settings regarding the RSA keys used for signing JWT tokens
 *
 * @property[signingKeySize] The size of the generated key pair in bits - defaults to 2048. It is not recommended to set below 2048
 * @property[publicKeyPath] The file path and name of the public key, which will be used to persist the key by [FileKeyProvider] - Can be either relative or absolute
 * @property[privateKeyPath] The file path and name of the private key, which will be used to persist the key by [FileKeyProvider] - Can be either relative or absolute
 */
@ConfigurationProperties("token")
data class JWTTokenConfigurationProperties(
    var signingKeySize: Int = 2048,
    var publicKeyPath: String = "",
    var privateKeyPath: String = ""
)
