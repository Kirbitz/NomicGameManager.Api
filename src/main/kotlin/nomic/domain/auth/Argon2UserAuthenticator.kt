package nomic.game.manager.domain.auth

import nomic.game.manager.data.repositories.CredentialRepository
import nomic.game.manager.data.repositories.UserRepository
import nomic.game.manager.domain.entities.PasswordHash
import nomic.game.manager.domain.entities.Username
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.stereotype.Component

@Component
class Argon2UserAuthenticator(private val creds: CredentialRepository,
                              private val users: UserRepository,
                              private val tokenRegistry: TokenRegistry
) : UserAuthenticator
{

    override fun authenticateUserWithCredentials(username: Username, password: String) : AuthenticationResult {
        if (checkUserCredentials(username, password)) {
            val token = tokenRegistry.issueToken()
            return AuthenticationResult(true, token);
        } else {
            return AuthenticationResult(false)
        }
    }

    override fun createUser(username: Username, password: String) {
        val encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()
        val passwordHash = PasswordHash(encoder.encode(password))

        val user = users.create(username)
        creds.create(user, passwordHash)
    }

    private fun checkUserCredentials(username: Username, password: String) : Boolean {
        // Hash and check against DB
        val encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()
        val credential = creds.getByName(username)
        return encoder.matches(password, credential.passwordHash.rawHash.toString())
    }
}