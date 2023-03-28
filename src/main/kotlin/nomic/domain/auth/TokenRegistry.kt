package nomic.domain.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.RegisteredClaims
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import nomic.data.repositories.IUserRepository
import nomic.domain.entities.EndUser
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant

/**
 * This registry service manages the creation, validation, and all other processes necessary for issuing and validating JWT Token.
 */
interface ITokenRegistry {

    /**
     * Issues a JWT Token for a specified user with the specified claims
     *
     * @param[user] The user to whom the JWT Token is being issued so that he can more readily authenticate later
     * @param[claims] A mapping of claims between a string name and a stringified value, by default empty
     * @return The raw JWT Token string authenticating that user
     */
    fun issueToken(user: EndUser, claims: Map<String, String> = mapOf()): String

    /**
     * Validates the authenticity of a specified raw JWT Token
     *
     * @param[rawToken] The raw string representing a JWT Token to be verified as authentic and valid
     * @return A result object containing the success state, the subject, and all valid claims
     */
    fun validateToken(rawToken: String): TokenValidationResult
}

/**
 * A data object containing the success state of a token validation request and relevent details about the token
 *
 * @property[isSuccess] The success state of the token validation request: true only if successfully validated
 * @property[subject] If successful, the subject of the JWT Token, which is the user domain entity of the authenticated user; otherwise null
 * @property[validClaims] A mapping of claims between string name and a stringified value; if unsuccessful, empty
 */
data class TokenValidationResult(val isSuccess: Boolean, val subject: EndUser? = null, val validClaims: Map<String, String> = mapOf())

/**
 * This implementation of ITokenRegistry offers a straightforward implementation using RSA for token signing.
 *
 * @see[ITokenRegistry]
 * @param[keyProvider] This dependency is used to retrieve the RSA keys used to sign all JWT Tokens to validate authenticity and integrity
 * @param[usersRepo] This dependency is used to retrieve the user entity that is the subject of valid JWT Tokens
 */
@Service
class TokenRegistry(
    private val keyProvider: IKeyProvider,
    private val usersRepo: IUserRepository
) : ITokenRegistry {
    private val algorithm: Algorithm
    private val verifier: JWTVerifier

    init {
        val keyPair = keyProvider.getKeyPair()
        algorithm = Algorithm.RSA256(keyPair.publicKey, keyPair.privateKey)
        verifier = JWT.require(algorithm)
            .withIssuer(JWT_ISSUER)
            .withAudience(JWT_ISSUER)
            .withClaimPresence(RegisteredClaims.SUBJECT)
            .acceptLeeway(10) // Issued at, Expires at, and not before as a 10 second window of leeway
            .build()
    }

    override fun issueToken(user: EndUser, claims: Map<String, String>): String {
        val tokenBuilder = JWT.create()
            .withSubject(user.id.toString())
            .withIssuer(JWT_ISSUER)
            .withAudience(JWT_ISSUER)
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plus(Duration.ofHours(2)))
            .withNotBefore(Instant.now())

        for (claim in claims) {
            tokenBuilder.withClaim(claim.key, claim.value)
        }

        return tokenBuilder.sign(algorithm)
    }

    override fun validateToken(rawToken: String): TokenValidationResult {
        // This is a necessary exception-as-logic-control due to the library,
        // unfortunately necessary for the time being.
        return try {
            val jwt = verifier.verify(rawToken)

            // Since the signature has been verified and the token validated, we know that we created the token.
            // Therefore, the subject claim represents a valid user id.
            val user = usersRepo.getById(jwt.subject.toInt()).get()
            val claims = jwt.claims.mapValues { it.value.asString() }
            TokenValidationResult(true, user, claims)
        } catch (e: JWTVerificationException) {
            TokenValidationResult(false)
        }
    }

    companion object {
        internal const val JWT_ISSUER = "NomicGameManager.Api"
    }
}
