package nomic.api.auth

import nomic.domain.entities.LoginName

data class BasicAuthenticationHeader(val loginName: LoginName, val password: String)
