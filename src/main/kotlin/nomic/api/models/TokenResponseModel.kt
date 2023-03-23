package nomic.api.models

/**
 * This data class represents the response returned by `api/auth/token`. It is directly serialized into JSON.
 *
 * @prop[isSuccess] The success state of authentication - true only if authenticated
 * @prop[token] The raw JWT Token - null only if unsuccessful
 */
data class TokenResponseModel(val isSuccess: Boolean, val token: String? = null)
