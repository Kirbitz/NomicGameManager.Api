package nomic.game.manager.domain.auth

import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

interface KeyProvider {
    fun GetKeyPair() : RSAKeyPair
}

data class RSAKeyPair(val publicKey: RSAPublicKey, val privateKey: RSAPrivateKey)