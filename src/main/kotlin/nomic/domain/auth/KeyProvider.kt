package nomic.domain.auth

import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

/**
 * This service provides an abstraction for retrieving a RSA Key Pair for signing JWT Tokens
 */
interface KeyProvider {
    /**
     * Provides a RSA key pair
     *
     * @return A wrapper pairing the JDK's RSA public and private keys
     */
    fun getKeyPair(): RSAKeyPair
}

/**
 * A wrapper pairing the JDK's RSA public and private keys
 *
 * @see java.security.interfaces.RSAPrivateKey
 * @see java.security.interfaces.RSAPublicKey
 *
 * @property publicKey The public key component of RSA encryption
 * @property privateKey The private key component of RSA encryption
 */
data class RSAKeyPair(val publicKey: RSAPublicKey, val privateKey: RSAPrivateKey)
