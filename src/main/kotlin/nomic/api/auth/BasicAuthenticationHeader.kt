package nomic.api.auth

import java.util.Base64

class BasicAuthenticationHeader(value: String) {
    val username: String
    val password: String

    init {
        val headerParts = value.split(" ")
        if (headerParts.size != 2) {
            throw BadAuthorizationHeaderException()
        }

        val authType = headerParts[0]
        if (!authType.equals("Basic")) {
            throw IncorrectAuthorizationHeaderException(authType)
        }

        val credentialBytes = Base64.getDecoder().decode(headerParts[1])
        val decodedHeader = String(credentialBytes)

        if (!decodedHeader.contains(':')) {
            throw BadAuthorizationHeaderException("Invalid Basic Authentication")
        }

        val credentials = decodedHeader.split(':', limit = 2)

        username = credentials[0]
        password = credentials[1]
    }
}
