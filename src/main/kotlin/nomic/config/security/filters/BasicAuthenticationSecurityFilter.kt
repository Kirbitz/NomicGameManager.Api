package nomic.config.security.filters

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import nomic.domain.auth.IUserAuthenticator
import nomic.domain.entities.LoginName
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.filter.GenericFilterBean
import java.util.*

/**
 * This security filter checks incoming HTTP requests for the BASIC authorization header, parses it and authenticates the credentials.
 * Upon authentication, it constructs and adds a [UsernamePasswordAuthenticationToken] to the current [SecurityContext]. Regardless of
 * validation success, it continues the filter chain.
 *
 * @see[nomic.domain.auth.IUserAuthenticator]
 * @see[org.springframework.security.core.context.SecurityContext]
 * @see[org.springframework.security.authentication.UsernamePasswordAuthenticationToken]
 * @see[org.springframework.web.filter.GenericFilterBean]
 * @see[jakarta.servlet.FilterChain]
 */
class BasicAuthenticationSecurityFilter(
    private val userAuthenticator: IUserAuthenticator
) : GenericFilterBean() {

    val customUrlFilter = AntPathRequestMatcher("/api/auth/token")

    /**
     * A wrapper object around the [LoginName] and password contained inside the Basic Authorization header
     *
     * @prop[loginName] The [LoginName] of the user credentials in the HTTP Basic Authorization
     * @prop[password] The [password] of the user credentials in the HTTP Basic Authorization
     */
    private data class Credentials(val loginName: LoginName, val password: String)

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpRequest = request as HttpServletRequest

        // This filter is only allowed to run on the token endpoint. Otherwise, API consumers can disregard
        // using JWT tokens and use Basic auth exclusively.
        if (!customUrlFilter.matches(httpRequest)) {
            chain?.doFilter(request, response)
            return
        }

        val authorizationHeader = httpRequest.getHeader("Authorization")

        val authToken = tryParse(authorizationHeader)
        if (authToken.isPresent) {
            val creds = authToken.get()
            val authResult = userAuthenticator.authenticateUserWithCredentials(creds.loginName, creds.password)

            if (authResult.isSuccess) {
                val context = SecurityContextHolder.getContext()

                // TODO Could setting the `credentials` to null cause issues? Bad practice within Spring Security?
                context.authentication = UsernamePasswordAuthenticationToken(authResult.user, null, listOf())
            }
        }

        chain?.doFilter(request, response)
    }

    /**
     * Parses HTTP Basic authorization header, and if valid, constructs a wrapper object around the credentials
     *
     * @param[authorizationHeader] The HTTP Authorization header which is expected to be Basic
     * @return A [Credentials] wrapper object around the [LoginName] and password in the header
     */
    private fun tryParse(authorizationHeader: String?): Optional<Credentials> {
        if (authorizationHeader == null) {
            return Optional.empty()
        }

        val headerParts = authorizationHeader.split(" ")
        if (headerParts.size != 2) {
            return Optional.empty()
        }

        val authType = headerParts[0]
        if (!authType.equals("Basic")) {
            return Optional.empty()
        }

        val credentialBytes = Base64.getDecoder().decode(headerParts[1])
        val decodedHeader = String(credentialBytes)

        if (!decodedHeader.contains(':')) {
            return Optional.empty()
        }

        val credentials = decodedHeader.split(':', limit = 2)

        if (!LoginName.canParse(credentials[0])) {
            return Optional.empty()
        }

        val loginName = LoginName(credentials[0])
        val password = credentials[1]

        return Optional.of(Credentials(loginName, password))
    }
}
