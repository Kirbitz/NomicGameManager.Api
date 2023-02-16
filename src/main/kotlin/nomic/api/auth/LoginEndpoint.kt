package nomic.api.auth

import nomic.domain.auth.UserAuthenticator
import nomic.domain.entities.LoginName
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.util.Base64

@RestController
@RequestMapping("api/auth")
class LoginEndpoint(val userAuthenticator: UserAuthenticator) {

    @GetMapping("login")
    fun loginRequest(@RequestHeader(HttpHeaders.AUTHORIZATION)
                     authorization: String): ResponseEntity<LoginResponseModel> {
        val credentialBytes = Base64.getDecoder().decode(authorization.substring("Basic ".length))
        val credentials = String(credentialBytes).split(':')

        val userAuthentication = userAuthenticator.authenticateUserWithCredentials(
            LoginName(credentials[0]),
            credentials[1],
        )

        val responseModel = LoginResponseModel(userAuthentication.isSuccess, userAuthentication.token)
        return ResponseEntity(responseModel, HttpStatus.OK)

    }
}

data class LoginResponseModel(val isSuccess: Boolean, val token: String? = null)
