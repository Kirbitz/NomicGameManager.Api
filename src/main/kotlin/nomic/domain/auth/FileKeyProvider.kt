package nomic.domain.auth

import org.springframework.stereotype.Component
import java.io.File
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

@Component
class FileKeyProvider(private val tokenConfig : JWTTokenConfigurationProperties) : KeyProvider {

    private var keyPair: RSAKeyPair? = null

    private fun loadKeyFiles() : Boolean {
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

    private fun generateKeyPair() : RSAKeyPair {
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

    override fun GetKeyPair(): RSAKeyPair {
        if (loadKeyFiles()) {
            return keyPair!!
        }

        return generateKeyPair()
    }
}