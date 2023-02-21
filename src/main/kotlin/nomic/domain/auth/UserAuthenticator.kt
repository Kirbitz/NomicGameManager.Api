package nomic.domain.auth

import nomic.domain.entities.LoginName

/**
 * A domain service that provides functions for authenticating user accounts
 */
interface UserAuthenticator {

    /**
     * Authenticates a user's credentials provided the login name and plain text password.
     *
     * Upon successful authentication, this function returns a result object containing a JWT Token which the user to more
     * readily provide authentication in future API calls.
     *
     * @param[loginName] A domain object wrapper around a string that ensures it is a valid login name
     * @param[password] A string containing the user's password. Do not request the password over unencrypted channels such as HTTP
     * @return An object containing the success state of the authentication and the JWT Token
     */
    fun authenticateUserWithCredentials(loginName: LoginName, password: String): AuthenticationResult

    /**
     * Creates a new user with the provided name and credentials.
     *
     * Per business rules, this function will fail if the login name is not unique or if the password does not pass specified
     * password strength rules. Otherwise, this function will create the user and return a result object with a JWT Token.
     *
     * @param[name] The user's personal name - no associated business rules regarding valid names
     * @param[loginName] The user's proposed login name that must be unique
     * @param[password] The user's proposed password that must pass strength tests
     * @return An object containing the success state of the account creation and the JWT Token
     */
    fun createUser(name: String, loginName: LoginName, password: String): AuthenticationResult
}

/**
 * An data object containing the success state of the authentication
 *
 * @property[isSuccess] The success state of the authentication: true only if successful
 * @property[token] If successful, the raw JWT Token that can be returned to the user for easier authentication in later calls; otherwise null
 */
data class AuthenticationResult(val isSuccess: Boolean, val token: String? = null)
