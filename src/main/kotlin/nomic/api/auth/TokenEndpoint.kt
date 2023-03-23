package nomic.api.auth

import nomic.api.models.TokenResponseModel
import nomic.domain.auth.ITokenRegistry
import nomic.domain.entities.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * This controller listens on `api/auth/token` for users to authenticate either via HTTP Basic authentication when logging in initially or
 * via HTTP Bearer authentication with an old token. It then provides a JWT Token to access other endpoints, effectively allowing the initial token
 * to act as a refresh token for previously authenticated users.
 *
 * @see[nomic.domain.auth.ITokenRegistry]
 * @see[nomic.config.security.filters.BasicAuthenticationSecurityFilter]
 * @see[nomic.config.security.filters.JWTAuthenticationSecurityFilter]
 * @param[tokenRegistry] This dependency is a domain service used by the endpoint to generate the JWT Token for the authenticated user.
 */
@RestController
@RequestMapping("api/auth")
class TokenEndpoint(private val tokenRegistry: ITokenRegistry) {

    /**
     * This endpoint is the core of the controller. It accepts the authenticated user, which is the principal of the context,
     * provided automatically by Spring. It operates over the POST method.
     *
     * @param[authorization] The authenticated user principal
     * @return A Spring entity representing the response that gets serialized into JSON
     */
    @PostMapping("token", produces = ["application/json;charset=UTF-8"])
    fun tokenRequest(@AuthenticationPrincipal user: User): ResponseEntity<TokenResponseModel> {
        val responseModel = TokenResponseModel(true, tokenRegistry.issueToken(user))

        return ResponseEntity(responseModel, HttpStatus.OK)
    }
}
