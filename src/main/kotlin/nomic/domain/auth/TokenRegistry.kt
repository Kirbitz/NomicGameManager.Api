package nomic.game.manager.domain.auth

interface TokenRegistry {
    fun issueToken(claims: Map<String, String> = mapOf()) : String
    fun validateToken(rawToken: String) : TokenValidationResult
}

data class TokenValidationResult(val isSuccess: Boolean, val validClaims: Map<String, String>)