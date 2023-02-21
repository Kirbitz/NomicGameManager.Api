package nomic.domain.auth

import nomic.data.repositories.CredentialRepository
import nomic.data.repositories.UserRepository
import nomic.domain.entities.Credential
import nomic.domain.entities.LoginName
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.stereotype.Component

/**
 * This implementation of [UserAuthenticator] hashes passwords using the Argon2id algorithm with the defaults for Spring Security v5.8
 * @see UserAuthenticator
 *
 * @param creds This dependency is used to retrieve and create all necessary credential data objects
 * @param users This dependency is user to retrieve and create user entities
 * @param tokenRegistry This dependency is used to issue JWT Tokens upon authentication
 */
@Component
class Argon2UserAuthenticator(
    private val creds: CredentialRepository,
    private val users: UserRepository,
    private val tokenRegistry: TokenRegistry
) : UserAuthenticator {

    override fun authenticateUserWithCredentials(loginName: LoginName, password: String): AuthenticationResult {
        val credential = creds.getByName(loginName)
        if (credential.isEmpty) {
            return AuthenticationResult(false)
        }

        if (checkUserCredentials(credential.get(), password)) {
            val token = tokenRegistry.issueToken(credential.get().user)
            return AuthenticationResult(true, token)
        } else {
            return AuthenticationResult(false)
        }
    }

    override fun createUser(name: String, loginName: LoginName, password: String): AuthenticationResult {
        /*val encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()
        val passwordHash = PasswordHash(encoder.encode(password))

        // TODO Add business rules regarding logins and passwords
        val user = users.create(name)
        creds.create(user, loginName, passwordHash)
        return AuthenticationResult(true, tokenRegistry.issueToken(user))*/
        TODO("Not yet implemented")
    }

    private fun checkUserCredentials(credential: Credential, password: String): Boolean {
        // Hash and check against DB
        val encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()
        return encoder.matches(password, credential.passwordHash.rawHash)
    }
}
