package nomic.domain.auth

import nomic.domain.entities.User

interface TokenRegistry {
    fun issueToken(user: User, claims: Map<String, String> = mapOf()): String
    fun validateToken(rawToken: String): TokenValidationResult
}

data class TokenValidationResult(val isSuccess: Boolean, val subject: User? = null, val validClaims: Map<String, String> = mapOf())
