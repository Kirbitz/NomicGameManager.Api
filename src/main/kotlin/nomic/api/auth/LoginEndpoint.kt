package nomic.api.auth

import nomic.domain.auth.UserAuthenticator

import nomic.domain.entities.LoginName
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/auth")
class LoginEndpoint(val userAuthenticator: UserAuthenticator) {

    @GetMapping("login")
    @ResponseBody
    fun loginRequest(@RequestBody request: LoginRequestModel): LoginResponseModel {
        val userAuthentication = userAuthenticator.authenticateUserWithCredentials(
                                    LoginName(request.name), request.password)

        return LoginResponseModel(userAuthentication.isSuccess, userAuthentication.token)
    }
}

data class LoginRequestModel(val name: String, val password: String)
data class LoginResponseModel(val isSuccess: Boolean, val token: String? = null)