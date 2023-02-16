package nomic.domain.auth

import nomic.data.repositories.CredentialRepository
import nomic.data.repositories.UserRepository
import nomic.domain.entities.Credential
import nomic.domain.entities.LoginName
import nomic.domain.entities.PasswordHash
import nomic.domain.entities.User
import org.assertj.core.api.Assertions
import org.assertj.core.api.Condition
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import java.util.Optional

class Argon2UserAuthenticatorTest
{
    private val auth: Argon2UserAuthenticator

    private val authSucceededCondition : Condition<AuthenticationResult> = Condition({ it.isSuccess }, "Authentication is successful")

    init {
        val encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()

        val passwordPass = PasswordHash(encoder.encode("pass"))
        val passwordFoobar = PasswordHash(encoder.encode("foobar"))

        val credBob = Credential(User(1, "Bob"), LoginName("alfredo"), passwordPass)
        val credJane = Credential(User(2, "Jane"), LoginName("see_sharp"), passwordFoobar)

        val credsRepo = mock<CredentialRepository>
        {
            on { getByName(credBob.loginName) } doReturn Optional.of(credBob)
            on { getByName(credJane.loginName) } doReturn Optional.of(credJane)
        }

        val userRepo = mock<UserRepository>
        {
            on { getById(credBob.id) } doReturn Optional.of(credBob.user)
            on { getById(credJane.id) } doReturn Optional.of(credJane.user)
        }

        val tokenRepo = mock<TokenRegistry>()

        auth = Argon2UserAuthenticator(credsRepo, userRepo, tokenRepo)
    }

    @Test
    fun test_authenticate_badPassword()
    {
        val attempt1 = auth.authenticateUserWithCredentials(LoginName("alfredo"), "password")
        val attempt2 = auth.authenticateUserWithCredentials(LoginName("see_sharp"), "super_secret_password")

        val falseAuth = AuthenticationResult(false)
        Assertions.assertThat(attempt1).isEqualTo(falseAuth)
        Assertions.assertThat(attempt2).isEqualTo(falseAuth)
    }

    @Test
    fun test_authenticate_badUsername()
    {
        val attempt1 = auth.authenticateUserWithCredentials(LoginName("alfredo1"), "pass")
        val attempt2 = auth.authenticateUserWithCredentials(LoginName("see_sharp1"), "foobar")

        val falseAuth = AuthenticationResult(false)
        Assertions.assertThat(attempt1).isEqualTo(falseAuth)
        Assertions.assertThat(attempt2).isEqualTo(falseAuth)
    }

    @Test
    fun test_authenticate_success()
    {
        val attempt1 = auth.authenticateUserWithCredentials(LoginName("alfredo"), "pass")
        val attempt2 = auth.authenticateUserWithCredentials(LoginName("see_sharp"), "foobar")

        Assertions.assertThat(attempt1).`is`(authSucceededCondition)
        Assertions.assertThat(attempt2).`is`(authSucceededCondition)
    }
}