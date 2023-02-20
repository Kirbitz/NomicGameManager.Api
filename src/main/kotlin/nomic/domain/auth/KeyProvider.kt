package nomic.domain.auth

import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

interface KeyProvider {
    fun getKeyPair(): RSAKeyPair
}

data class RSAKeyPair(val publicKey: RSAPublicKey, val privateKey: RSAPrivateKey)
