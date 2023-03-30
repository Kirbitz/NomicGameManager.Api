package nomic.unit.domain.auth

import nomic.domain.auth.FileKeyProvider
import nomic.domain.auth.JWTTokenConfigurationProperties
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import java.nio.file.Files
import java.nio.file.Path

@TestMethodOrder(OrderAnnotation::class)
class FileKeyProviderTest {

    private val tokenConfig = JWTTokenConfigurationProperties(
        publicKeyPath = "integration-test.key",
        privateKeyPath = "integration-test.pem",
        signingKeySize = 512
    )

    @Test
    @Order(1)
    fun test_getKeyPair_generatesSizeCorrectly() {
        tokenConfig.signingKeySize = 2048
        val provider = FileKeyProvider(tokenConfig)

        val publicPath = Path.of(tokenConfig.publicKeyPath)
        if (Files.exists(publicPath)) {
            Files.delete(publicPath)
        }

        val privatePath = Path.of(tokenConfig.privateKeyPath)
        if (Files.exists(privatePath)) {
            Files.delete(privatePath)
        }

        val pair = provider.getKeyPair()

        Assertions.assertThat(pair).isNotNull

        Assertions.assertThat(pair.publicKey.modulus.bitLength())
            .isEqualTo(tokenConfig.signingKeySize)

        Assertions.assertThat(pair.privateKey.modulus.bitLength())
            .isEqualTo(tokenConfig.signingKeySize)

        Assertions.assertThat(Files.exists(publicPath)).isTrue
        Assertions.assertThat(Files.exists(privatePath)).isTrue
    }

    @Test
    @Order(2)
    fun test_getKeyPair_generatePair() {
        val provider = FileKeyProvider(tokenConfig)

        val publicPath = Path.of(tokenConfig.publicKeyPath)
        if (Files.exists(publicPath)) {
            Files.delete(publicPath)
        }

        val privatePath = Path.of(tokenConfig.privateKeyPath)
        if (Files.exists(privatePath)) {
            Files.delete(privatePath)
        }

        val pair = provider.getKeyPair()

        Assertions.assertThat(pair).isNotNull

        Assertions.assertThat(pair.publicKey.modulus.bitLength())
            .isEqualTo(tokenConfig.signingKeySize)

        Assertions.assertThat(pair.privateKey.modulus.bitLength())
            .isEqualTo(tokenConfig.signingKeySize)

        Assertions.assertThat(Files.exists(publicPath)).isTrue
        Assertions.assertThat(Files.exists(privatePath)).isTrue
    }

    @Test
    @Order(3)
    fun test_getKeyPair_alreadyExists() {
        val publicPath = Path.of(tokenConfig.publicKeyPath)
        val privatePath = Path.of(tokenConfig.privateKeyPath)

        Assertions.assertThat(Files.exists(publicPath)).isTrue
        Assertions.assertThat(Files.exists(privatePath)).isTrue

        val publicContents = Files.readAllBytes(publicPath)
        val privateContents = Files.readAllBytes(privatePath)

        val provider = FileKeyProvider(tokenConfig)
        val pair = provider.getKeyPair()

        Assertions.assertThat(pair.publicKey.modulus.bitLength())
            .isEqualTo(tokenConfig.signingKeySize)

        Assertions.assertThat(pair.privateKey.modulus.bitLength())
            .isEqualTo(tokenConfig.signingKeySize)

        Assertions.assertThat(Files.readAllBytes(publicPath))
            .containsExactly(publicContents.toTypedArray())

        Assertions.assertThat(Files.readAllBytes(privatePath))
            .containsExactly(privateContents.toTypedArray())
    }
}
