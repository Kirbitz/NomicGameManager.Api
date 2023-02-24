package nomic.api.auth

import nomic.domain.entities.LoginName

data class BasicAuthenticationHeader(val username: LoginName, val password: String)
