package nomic.domain.auth

import nomic.domain.entities.User

/**
 * This registry service manages the creation, validation, and all other processes necessary for issuing and validating JWT Token.
 */
interface TokenRegistry {

    /**
     * Issues a JWT Token for a specified user with the specified claims
     * 
     * @param user The user to whom the JWT Token is being issued so that he can more readily authenticate later
     * @param claims A mapping of claims between a string name and a stringified value, by default empty
     * 
     * @return The raw JWT Token string authenticating that user
     */
    fun issueToken(user: User, claims: Map<String, String> = mapOf()): String

    /**
     * Validates the authenticity of a specified raw JWT Token
     * 
     * @param rawToken The raw string representing a JWT Token to be verified as authentic and valid
     * 
     * @return A result object containing the success state, the subject, and all valid claims
     */
    fun validateToken(rawToken: String): TokenValidationResult
}


/**
 * A data object containing the success state of a token validation request and relevent details about the token
 * 
 * @property isSuccess The success state of the token validation request: true only if successfully validated
 * @property subject If successful, the subject of the JWT Token, which is the user domain entity of the authenticated user; otherwise null
 * @property validClaims A mapping of claims between string name and a stringified value; if unsuccessful, empty
 */
data class TokenValidationResult(val isSuccess: Boolean, val subject: User? = null, val validClaims: Map<String, String> = mapOf())
