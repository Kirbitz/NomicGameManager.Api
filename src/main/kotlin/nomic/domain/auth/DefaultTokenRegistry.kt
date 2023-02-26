package nomic.domain.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.RegisteredClaims
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import nomic.data.repositories.IUserRepository
import nomic.domain.entities.User
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant

/**
 * This implementation of ITokenRegistry offers a straightforward implementation using RSA for token signing.
 *
 * @see[TokenRegistry]
 * @param[keyProvider] This dependency is used to retrieve the RSA keys used to sign all JWT Tokens to validate authenticity and integrity
 * @param[usersRepo] This dependency is used to retrieve the user entity that is the subject of valid JWT Tokens
 */
@Component
class DefaultTokenRegistry(
    private val keyProvider: IKeyProvider,
    private val usersRepo: IUserRepository
) : ITokenRegistry {
    private val algorithm: Algorithm
    private val verifier: JWTVerifier

    init {
        val keyPair = keyProvider.getKeyPair()
        algorithm = Algorithm.RSA256(keyPair.publicKey, keyPair.privateKey)
        verifier = JWT.require(algorithm)
            .withIssuer(JWT_SERVER)
            .withAudience(JWT_SERVER)
            .withClaimPresence(RegisteredClaims.SUBJECT)
            .acceptLeeway(10) // Issued at, Expires at, and not before as a 10 second window of leeway
            .build()
    }

    override fun issueToken(user: User, claims: Map<String, String>): String {
        val tokenBuilder = JWT.create()
            .withSubject(user.id.toString())
            .withIssuer(JWT_SERVER)
            .withAudience(JWT_SERVER)
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plus(Duration.ofHours(2)))
            .withNotBefore(Instant.now())

        for (claim in claims) {
            tokenBuilder.withClaim(claim.key, claim.value)
        }

        return tokenBuilder.sign(algorithm)
    }

    override fun validateToken(rawToken: String): TokenValidationResult {
        // TODO Refactor out the exception pattern, possibly swap out jwt libraries
        try {
            val jwt = verifier.verify(rawToken)

            // Since the signature has been verified and the token validated, we know that we created the token.
            // Therefore, the subject claim represents a valid user id.
            val user = usersRepo.getById(jwt.subject.toInt()).get()
            val claims = jwt.claims.mapValues { it.value.asString() }
            return TokenValidationResult(true, user, claims)
        } catch (e: JWTVerificationException) {
            return TokenValidationResult(false)
        }
    }

    companion object {
        private const val JWT_SERVER = "NomicGameManager.Api"
    }
}
