package nomic.domain.auth

import nomic.data.repositories.CredentialRepository
import nomic.data.repositories.UserRepository
import nomic.domain.entities.LoginName
import nomic.domain.entities.PasswordHash
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.stereotype.Component

@Component
class Argon2UserAuthenticator(
    private val creds: CredentialRepository,
    private val users: UserRepository,
    private val tokenRegistry: TokenRegistry,
) : UserAuthenticator {

    override fun authenticateUserWithCredentials(loginName: LoginName, password: String): AuthenticationResult {
        if (checkUserCredentials(loginName, password)) {
            val user = users.findUserByName(loginName)
            val token = tokenRegistry.issueToken(user)
            return AuthenticationResult(true, token)
        } else {
            return AuthenticationResult(false)
        }
    }

    override fun createUser(loginName: LoginName, password: String) {
        val encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()
        val passwordHash = PasswordHash(encoder.encode(password))

        val user = users.create(loginName.rawName)
        creds.create(user, loginName, passwordHash)
    }

    private fun checkUserCredentials(loginName: LoginName, password: String): Boolean {
        // Hash and check against DB
        val encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()
        val credential = creds.getByName(loginName)
        return encoder.matches(password, credential.passwordHash.rawHash.toString())
    }
}
