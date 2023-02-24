package nomic.config.security.filters

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import nomic.domain.auth.TokenRegistry
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import java.util.*

/**
 * This security filter checks incoming HTTP requests for the authorization header, parses it, and validates the JWT Token if present. Upon validation,
 * it constructs and adds a [UsernamePasswordAuthenticationToken] to the current [SecurityContext]. Regardless of validation success,
 * it continues the filter chain.
 *
 * @param[tokenRegistry] This dependency is used to validate JWT Tokens passed through the Authorization header.
 * @see[org.springframework.security.core.context.SecurityContext]
 * @see[org.springframework.security.authentication.UsernamePasswordAuthenticationToken]
 * @see[org.springframework.web.filter.GenericFilterBean]
 * @see[jakarta.servlet.FilterChain]
 */
class JWTAuthenticationSecurityFilter(
    private val tokenRegistry: TokenRegistry
) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpRequest = request as HttpServletRequest

        val authorizationHeader = httpRequest.getHeader("Authorization")
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            chain?.doFilter(request, response)
            return
        }

        val rawToken = authorizationHeader.substring("Bearer ".length)

        val validation = tokenRegistry.validateToken(rawToken)
        if (validation.isSuccess) {
            val auth = UsernamePasswordAuthenticationToken(validation.subject, rawToken, listOf())
            SecurityContextHolder.getContext().authentication = auth
        }

        chain?.doFilter(request, response)
    }
}
