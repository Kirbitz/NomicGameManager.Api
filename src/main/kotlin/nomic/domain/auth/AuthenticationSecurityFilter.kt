package nomic.domain.auth

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean

class AuthenticationSecurityFilter(
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
