package nomic.domain.auth

import org.springframework.stereotype.Service
import java.io.File
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

/**
 * This service provides an abstraction for retrieving a RSA Key Pair for signing JWT Tokens
 */
interface IKeyProvider {
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
 * @see[java.security.interfaces.RSAPrivateKey]
 * @see[java.security.interfaces.RSAPublicKey]
 *
 * @property[publicKey] The public key component of RSA encryption
 * @property[privateKey] The private key component of RSA encryption
 */
data class RSAKeyPair(val publicKey: RSAPublicKey, val privateKey: RSAPrivateKey)

/**
 * This implementation of [IKeyProvider] generates, persists, and loads RSA keys on the filesystem in the locations specified by [tokenConfig]
 *
 * @see[JWTTokenConfigurationProperties]
 * @param[tokenConfig] This dependency is the configuration parsed by Spring with the various properties needed for the JWT Token RSA keys
 */
@Service
class FileKeyProvider(private val tokenConfig: JWTTokenConfigurationProperties) : IKeyProvider {

    private var keyPair: RSAKeyPair? = null

    /**
     * Loads the public and private key file into memory if it's not already been cached.
     * If the keys have not been generated, it will fail.
     *
     * @return Whether it was successfully loaded
     */
    private fun loadKeyFiles(): Boolean {
        if (keyPair != null) {
            return true
        }

        val publicKeyFile = File(tokenConfig.publicKeyPath)
        val privateKeyFile = File(tokenConfig.privateKeyPath)

        if (!publicKeyFile.exists() || !privateKeyFile.exists()) {
            return false
        }

        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKeySpec = X509EncodedKeySpec(publicKeyFile.readBytes())
        val privateKeySpec = PKCS8EncodedKeySpec(privateKeyFile.readBytes())

        val publicKey = keyFactory.generatePublic(publicKeySpec) as RSAPublicKey
        val privateKey = keyFactory.generatePrivate(privateKeySpec) as RSAPrivateKey

        keyPair = RSAKeyPair(publicKey, privateKey)
        return true
    }

    /**
     * Generates a RSA key pair, saves them to memory, and persists them to file. They are generated
     * in accordance with the settings specified by [tokenConfig]
     *
     * @see[JWTTokenConfigurationProperties]
     * @return The newly generated key pair.
     */
    private fun generateKeyPair(): RSAKeyPair {
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(tokenConfig.signingKeySize)

        val pair = generator.generateKeyPair()
        val publicKey = pair.public as RSAPublicKey
        val privateKey = pair.private as RSAPrivateKey

        val publicKeyFile = File(tokenConfig.publicKeyPath)
        val privateKeyFile = File(tokenConfig.privateKeyPath)

        publicKeyFile.writeBytes(publicKey.encoded)
        privateKeyFile.writeBytes(privateKey.encoded)

        keyPair = RSAKeyPair(publicKey, privateKey)
        return keyPair!!
    }

    /**
     * Retrieves or generates a RSA key pair
     *
     * @return A pairing of RSA public and private keys
     */
    override fun getKeyPair(): RSAKeyPair {
        if (loadKeyFiles()) {
            return keyPair!!
        }

        return generateKeyPair()
    }
}
