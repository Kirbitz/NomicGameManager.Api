package nomic.domain.auth

import nomic.data.repositories.IUserRepository
import nomic.domain.entities.User
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

class TokenRegistryTest {

    private val keyProvider: IKeyProvider
    private val usersRepo: IUserRepository

    private val tokenRegistry: TokenRegistry

    companion object {
        const val PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAI6jowrmGDTzQg7YQIyEoxQbvB16efH1EsEHj6yE58mIxXNi95ZEO5u7/loWmd1a1eWGI2dIu2dkDyYTVx8YDfUCAwEAAQ=="
        const val PRIVATE_KEY = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAjqOjCuYYNPNCDthAjISjFBu8HXp58fUSwQePrITnyYjFc2L3lkQ7m7v+WhaZ3VrV5YYjZ0i7Z2QPJhNXHxgN9QIDAQABAkAPPnXCSGz127mHlJzxZ4t2LrCgBeLadPKYJpK4QUv0QbzawRQxGJRmV2a+H6Y1oMOXvWhpZXQJIk2ff4EbInnRAiEAxVQdFxWP1QpE03wxeax1ZKHHKZyin+2ucdaue7mDThsCIQC5DMfjo8PX6hI/kl4F0/L+n6ur6Ngzy++OVOiQJwWVLwIgdr1is91Zq4x+VfRJoTnRejiPK88BXNMdQYs5e2HqTTkCIQCtKxGvrCPsXOZAyWblS0edEaykDhystA50gm6z32BxDwIgEaaS6Vy66nFFvX6X79YiZ5++aomx0Tl3d0+L0il2+GQ="

        val publicKeyBytes = Base64.getDecoder().decode(PUBLIC_KEY)
        val privateKeyBytes = Base64.getDecoder().decode(PRIVATE_KEY)

        val testUser1 = User(512, "Titius Livius")
        val testUser2 = User(1024, "Achilleus")
    }

    init {
        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKeySpec = X509EncodedKeySpec(publicKeyBytes)
        val privateKeySpec = PKCS8EncodedKeySpec(privateKeyBytes)

        val publicKey = keyFactory.generatePublic(publicKeySpec) as RSAPublicKey
        val privateKey = keyFactory.generatePrivate(privateKeySpec) as RSAPrivateKey
        val keyPair = RSAKeyPair(publicKey, privateKey)

        keyProvider = mock<IKeyProvider>
        {
            on { it.getKeyPair() } doReturn keyPair
        }

        usersRepo = mock<IUserRepository>
        {
            on { it.getById(testUser1.id) } doReturn Optional.of(testUser1)
            on { it.getById(testUser2.id) } doReturn Optional.of(testUser2)
        }

        tokenRegistry = TokenRegistry(keyProvider, usersRepo)
    }

    @Test
    fun test_issueToken_hasSubjectId() {
        val token1 = tokenRegistry.issueToken(testUser1)
        val token2 = tokenRegistry.issueToken(testUser2)

        val tokenBody1 = String(Base64.getDecoder().decode(token1.split('.')[1]))
        val tokenBody2 = String(Base64.getDecoder().decode(token2.split('.')[1]))

        Assertions.assertThat(tokenBody1).contains("\"sub\":\"${testUser1.id}\"")
        Assertions.assertThat(tokenBody2).contains("\"sub\":\"${testUser2.id}\"")
    }

    @Test
    fun test_issueToken_validates() {
        val token1 = tokenRegistry.issueToken(testUser1)
        val token2 = tokenRegistry.issueToken(testUser2)

        Assertions.assertThat(tokenRegistry.validateToken(token1).isSuccess).isTrue
        Assertions.assertThat(tokenRegistry.validateToken(token2).isSuccess).isTrue
    }

    @Test
    fun validateToken() {
        Assertions.fail<String>("Not yet implemented")
    }
}
