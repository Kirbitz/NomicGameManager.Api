package nomic.config.security.filters

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import nomic.domain.entities.LoginName
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import java.util.*

/**
 * This security filter checks incoming HTTP requests for the BASIC authorization header  and parses it. If it successfully
 * parses, it constructs and adds a [UsernamePasswordAuthenticationToken] with the credentials to the
 * current [SecurityContext] Regardless of validation success, it continues the filter chain.
 *
 * @see[org.springframework.security.core.context.SecurityContext]
 * @see[org.springframework.security.authentication.UsernamePasswordAuthenticationToken]
 * @see[org.springframework.web.filter.GenericFilterBean]
 * @see[jakarta.servlet.FilterChain]
 */
class BasicAuthenticationSecurityFilter : GenericFilterBean() {

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpRequest = request as HttpServletRequest
        val authorizationHeader = httpRequest.getHeader("Authorization")

        val authToken = tryParse(authorizationHeader)
        if (authToken.isPresent) {
            val context = SecurityContextHolder.getContext()
            context.authentication = authToken.get()
        }

        chain?.doFilter(request, response)
    }

    /**
     * Parses HTTP Basic authorization header, and if valid, constructs an authentication token
     * with it.
     *
     * @param[authorizationHeader] The HTTP Authorization header which is expected to be Basic
     * @return An Authentication Token containing the [LoginName] as the principal and the password as the credentials
     */
    private fun tryParse(authorizationHeader: String?): Optional<UsernamePasswordAuthenticationToken> {
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

        val loginName = LoginName(credentials[0])
        val password = credentials[1]

        return Optional.of(UsernamePasswordAuthenticationToken(loginName, password, listOf()))
    }
}
