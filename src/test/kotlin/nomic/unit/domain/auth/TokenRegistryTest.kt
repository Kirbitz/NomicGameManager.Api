package nomic.unit.domain.auth

import com.auth0.jwt.RegisteredClaims
import nomic.data.repositories.IUserRepository
import nomic.domain.auth.IKeyProvider
import nomic.domain.auth.RSAKeyPair
import nomic.domain.auth.TokenRegistry
import nomic.domain.entities.EndUser
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

    private companion object {
        const val PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAI6jowrmGDTzQg7YQIyEoxQbvB16efH1EsEHj6yE58mIxXNi95ZEO5u7/loWmd1a1eWGI2dIu2dkDyYTVx8YDfUCAwEAAQ=="
        const val PRIVATE_KEY = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAjqOjCuYYNPNCDthAjISjFBu8HXp58fUSwQePrITnyYjFc2L3lkQ7m7v+WhaZ3VrV5YYjZ0i7Z2QPJhNXHxgN9QIDAQABAkAPPnXCSGz127mHlJzxZ4t2LrCgBeLadPKYJpK4QUv0QbzawRQxGJRmV2a+H6Y1oMOXvWhpZXQJIk2ff4EbInnRAiEAxVQdFxWP1QpE03wxeax1ZKHHKZyin+2ucdaue7mDThsCIQC5DMfjo8PX6hI/kl4F0/L+n6ur6Ngzy++OVOiQJwWVLwIgdr1is91Zq4x+VfRJoTnRejiPK88BXNMdQYs5e2HqTTkCIQCtKxGvrCPsXOZAyWblS0edEaykDhystA50gm6z32BxDwIgEaaS6Vy66nFFvX6X79YiZ5++aomx0Tl3d0+L0il2+GQ="

        // These are actually null-safe, but the compiler/IDE aren't sure due to the java/kotlin interop.
        val publicKeyBytes = Base64.getDecoder().decode(PUBLIC_KEY)!!
        val privateKeyBytes = Base64.getDecoder().decode(PRIVATE_KEY)!!

        val testUser1 = EndUser(512, "Titius Livius")
        val testUser2 = EndUser(1024, "Achilleus")
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
    }

    @Test
    fun test_issueToken_hasSubjectId() {
        val tokenRegistry = TokenRegistry(keyProvider, usersRepo)

        val token1 = tokenRegistry.issueToken(testUser1)
        val token2 = tokenRegistry.issueToken(testUser2)

        val tokenBody1 = String(Base64.getDecoder().decode(token1.split('.')[1]))
        val tokenBody2 = String(Base64.getDecoder().decode(token2.split('.')[1]))

        val subject = RegisteredClaims.SUBJECT

        Assertions.assertThat(tokenBody1).contains("\"$subject\":\"${testUser1.id}\"")
        Assertions.assertThat(tokenBody2).contains("\"$subject\":\"${testUser2.id}\"")
    }

    @Test
    fun test_issueToken_hasIssuerAndAudience() {
        val tokenRegistry = TokenRegistry(keyProvider, usersRepo)

        val token = tokenRegistry.issueToken(testUser1)

        val tokenBody = String(Base64.getDecoder().decode(token.split('.')[1]))

        Assertions.assertThat(tokenBody)
            .contains("\"${RegisteredClaims.ISSUER}\":\"${TokenRegistry.JWT_ISSUER}\"")

        Assertions.assertThat(tokenBody)
            .contains("\"${RegisteredClaims.AUDIENCE}\":\"${TokenRegistry.JWT_ISSUER}\"")
    }

    @Test
    fun test_issueToken_has_issuedAt_expires_notBefore() {
        val tokenRegistry = TokenRegistry(keyProvider, usersRepo)

        val token = tokenRegistry.issueToken(testUser1)
        val tokenBody = String(Base64.getDecoder().decode(token.split('.')[1]))

        Assertions.assertThat(tokenBody).contains(RegisteredClaims.ISSUED_AT)
        Assertions.assertThat(tokenBody).contains(RegisteredClaims.EXPIRES_AT)
        Assertions.assertThat(tokenBody).contains(RegisteredClaims.NOT_BEFORE)
    }

    @Test
    fun test_issueToken_validates() {
        val tokenRegistry = TokenRegistry(keyProvider, usersRepo)

        val token1 = tokenRegistry.issueToken(testUser1)
        val token2 = tokenRegistry.issueToken(testUser2)

        val validation1 = tokenRegistry.validateToken(token1)
        val validation2 = tokenRegistry.validateToken(token2)

        Assertions.assertThat(validation1.isSuccess).isTrue
        Assertions.assertThat(validation2.isSuccess).isTrue

        Assertions.assertThat(validation1.subject?.id).isEqualTo(testUser1.id)
        Assertions.assertThat(validation2.subject?.id).isEqualTo(testUser2.id)
    }

    @Test
    fun test_validateToken_expiredToken() {
        val tokenRegistry = TokenRegistry(keyProvider, usersRepo)
        val badToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI1MTIiLCJhdWQiOiJOb21pY0dhbWVNYW5hZ2VyLkFwaSIsIm5iZiI6MTY3ODIyNDYyMCwiaXNzIjoiTm9taWNHYW1lTWFuYWdlci5BcGkiLCJleHAiOjE2NzgyMzE4MjAsImlhdCI6MTY3ODIyNDYyMH0.AzP26ODJH-QEUcaZnRTlIaNMjzIzltOaKPqWf4xW9mSpkJBYCbyoCJGdvU_0kw3E9Ydn9IX_X6ezD29KPmuC8Q"

        val result = tokenRegistry.validateToken(badToken)
        Assertions.assertThat(result.isSuccess).isFalse
        Assertions.assertThat(result.subject).isNull()
        Assertions.assertThat(result.validClaims).isEmpty()
    }

    @Test
    fun test_validateToken_badToken() {
        val tokenRegistry = TokenRegistry(keyProvider, usersRepo)
        val result = tokenRegistry.validateToken("Blah.Foo.Bar")
        Assertions.assertThat(result.isSuccess).isFalse
        Assertions.assertThat(result.subject).isNull()
        Assertions.assertThat(result.validClaims).isEmpty()
    }
}
