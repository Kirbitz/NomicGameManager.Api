package nomic.domain.auth

import nomic.data.repositories.ICredentialRepository
import nomic.data.repositories.IUserRepository
import nomic.domain.entities.Credential
import nomic.domain.entities.LoginName
import nomic.domain.entities.User
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.stereotype.Service

/**
 * A domain service that provides functions for authenticating user accounts
 */
interface IUserAuthenticator {

    /**
     * Authenticates a user's credentials provided the login name and plain text password.
     *
     * Upon successful authentication, this function returns a result object containing the user entity representing the
     * authenticated user.
     *
     * @param[loginName] A domain object wrapper around a string that ensures it is a valid login name
     * @param[password] A string containing the user's password. Do not request the password over unencrypted channels such as HTTP
     * @return An object containing the success state of the authentication and the user who has been authenticated
     */
    fun authenticateUserWithCredentials(loginName: LoginName, password: String): AuthenticationResult

    /**
     * Creates a new user with the provided name and credentials.
     *
     * Per business rules, this function will fail if the login name is not unique or if the password does not pass specified
     * password strength rules. Otherwise, this function will create the user and return a result object with the new user.
     *
     * @param[name] The user's personal name - no associated business rules regarding valid names
     * @param[loginName] The user's proposed login name that must be unique
     * @param[password] The user's proposed password that must pass strength tests
     * @return An object containing the success state of the account creation and the user account who has been created
     */
    fun createUser(name: String, loginName: LoginName, password: String): AuthenticationResult
}

/**
 * An data object containing the success state of the authentication
 *
 * @property[isSuccess] The success state of the authentication: true only if successful
 * @property[user] If successful, the user entity represented the authenticated user; otherwise null
 */
data class AuthenticationResult(val isSuccess: Boolean, val user: User? = null)

/**
 * This implementation of [IUserAuthenticator] hashes passwords using the Argon2id algorithm with the defaults for Spring Security v5.8
 * @see[IUserAuthenticator]
 * @see[org.springframework.security.crypto.argon2.Argon2PasswordEncoder]
 *
 * @param[creds] This dependency is used to retrieve and create all necessary credential data objects
 * @param[users] This dependency is user to retrieve and create user entities
 */
@Service
class Argon2UserAuthenticator(
    private val creds: ICredentialRepository,
    private val users: IUserRepository
) : IUserAuthenticator {

    override fun authenticateUserWithCredentials(loginName: LoginName, password: String): AuthenticationResult {
        val credential = creds.getByName(loginName)
        if (credential.isEmpty) {
            return AuthenticationResult(false)
        }

        if (checkUserCredentials(credential.get(), password)) {
            return AuthenticationResult(true, credential.get().user)
        } else {
            return AuthenticationResult(false)
        }
    }

    override fun createUser(name: String, loginName: LoginName, password: String): AuthenticationResult {
        /*val encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()
        val passwordHash = PasswordHash(encoder.encode(password))

        val user = users.create(name)
        creds.create(user, loginName, passwordHash)
        return AuthenticationResult(true, tokenRegistry.issueToken(user))*/
        TODO("Not yet fully implemented - to be completed in NOM-56")
    }

    private fun checkUserCredentials(credential: Credential, password: String): Boolean {
        val hasher = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()
        return hasher.matches(password, credential.passwordHash.rawHash)
    }
}
