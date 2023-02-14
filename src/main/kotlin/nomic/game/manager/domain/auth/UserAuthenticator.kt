package nomic.game.manager.domain.auth

import nomic.game.manager.domain.entities.Username

interface UserAuthenticator {
    fun authenticateUserWithCredentials(username: Username, password: String) : AuthenticationResult
    fun createUser(username: Username, password: String)
}

data class AuthenticationResult(val isSuccess: Boolean, val token: String? = null)