package nomic.game.manager.domain.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import java.time.Duration
import java.time.Instant

class DefaultTokenRegistry(private val keyProvider : KeyProvider) : TokenRegistry {
    private val algorithm: Algorithm
    private val verifier: JWTVerifier

    init {
        val keyPair = keyProvider.GetKeyPair()
        algorithm = Algorithm.RSA256(keyPair.publicKey, keyPair.privateKey)
        verifier = JWT.require(algorithm)
            .withIssuer("NomicGameManager.Api")
            .build()
    }


    override fun issueToken(claims : Map<String, String>) : String {
        val tokenBuilder = JWT.create()
            .withIssuer("NomicGameManager.Api")
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plus(Duration.ofHours(2)))

        for (claim in claims) {
            tokenBuilder.withClaim(claim.key, claim.value)
        }

        return tokenBuilder.sign(algorithm)
    }

    override fun validateToken(rawToken: String): TokenValidationResult {
        try {
            val jwt = verifier.verify(rawToken)
            val claims = jwt.claims.mapValues { it.value.asString() }
            return TokenValidationResult(true, claims)
        } catch (e : JWTVerificationException) {
            return TokenValidationResult(false, mapOf())
        }
    }
}