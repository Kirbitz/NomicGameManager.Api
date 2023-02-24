package nomic.api.auth

import nomic.domain.entities.LoginName

/**
 * This object represents the decoded HTTP Basic Authorization header, containing a parsed
 * login name and password.
 *
 * @param[loginName] The login name which the user is trying to authenticate with
 * @param[password] The password which the user is trying to authenticate with
 */
data class BasicAuthenticationHeader(val loginName: LoginName, val password: String)
