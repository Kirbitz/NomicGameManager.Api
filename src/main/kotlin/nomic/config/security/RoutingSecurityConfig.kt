package nomic.config.security

import nomic.config.security.filters.BasicAuthenticationSecurityFilter
import nomic.config.security.filters.JWTAuthenticationSecurityFilter
import nomic.domain.auth.TokenRegistry
import nomic.domain.auth.UserAuthenticator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * This Spring [Configuration] configures the filter chain to add in [JWTAuthenticationSecurityFilter] and
 * [BasicAuthenticationSecurityFilter] and configures Spring Security to require authentication to all endpoints,
 * with the effect that legitimate JWT authentication is performed on all endpoints other than the auth endpoints,
 * while the `api/auth` endpoints require a basic auth header.
 *
 * @see[org.springframework.context.annotation.Configuration]
 * @see[org.springframework.security.web.SecurityFilterChain]
 */
@Configuration
class RoutingSecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity, tokenRegistry: TokenRegistry, userAuthenticator: UserAuthenticator): SecurityFilterChain {
        val jwtFilter = JWTAuthenticationSecurityFilter(tokenRegistry)
        val basicFilter = BasicAuthenticationSecurityFilter(userAuthenticator)

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
        http.addFilterBefore(basicFilter, JWTAuthenticationSecurityFilter::class.java)

        http.httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }
        return http.build()
    }
}
