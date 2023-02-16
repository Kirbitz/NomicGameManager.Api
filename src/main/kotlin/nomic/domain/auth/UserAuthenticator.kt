package nomic.domain.auth

import nomic.domain.entities.LoginName

interface UserAuthenticator {
    fun authenticateUserWithCredentials(loginName: LoginName, password: String): AuthenticationResult
    fun createUser(loginName: LoginName, password: String)
}

data class AuthenticationResult(val isSuccess: Boolean, val token: String? = null)
