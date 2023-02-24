package nomic.api.auth

import nomic.domain.auth.UserAuthenticator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * This controller listens on `api/auth/login` for users to authenticate via HTTP Basic authentication and receive a JWT Token. Users
 * can then use the JWT Token to authenticate on other endpoints using HTTP Bearer authentication. Accordingly, this endpoint does not require
 * users to be previously authenticated.
 *
 * @see[nomic.domain.auth.UserAuthenticator]
 * @param[userAuthenticator] This dependency is a domain service used by the endpoint to handle authenticating users and generating tokens.
 */
@RestController
@RequestMapping("api/auth")
class LoginEndpoint(private val userAuthenticator: UserAuthenticator) {

    /**
     * This endpoint is the core of the controller. It accepts the Authorization header expecting Basic Authentication and
     * returns the response model to be serialized by Spring and Jackson. It operates over the POST method.
     *
     * @param[authorization] The HTTP Basic Authorization Header
     * @return A Spring entity representing the response that gets serialized into JSON
     */
    @PostMapping("login")
    fun loginRequest(authorization: BasicAuthenticationHeader): ResponseEntity<LoginResponseModel> {
        val userAuthentication = userAuthenticator.authenticateUserWithCredentials(
            authorization.loginName,
            authorization.password
        )

        val responseModel = LoginResponseModel(userAuthentication.isSuccess, userAuthentication.token)
        return ResponseEntity(responseModel, HttpStatus.OK)
    }
}

/**
 * This data class represents the response returned by `api/auth/login`. It is directly serialized into JSON.
 *
 * @prop[isSuccess] The success state of authentication - true only if authenticated
 * @prop[token] The raw JWT Token - null only if unsuccessful
 */
data class LoginResponseModel(val isSuccess: Boolean, val token: String? = null)
