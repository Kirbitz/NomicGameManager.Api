package nomic.domain.auth

import nomic.domain.entities.LoginName
import nomic.domain.entities.User

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
