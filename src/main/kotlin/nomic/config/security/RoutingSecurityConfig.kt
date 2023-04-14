package nomic.config.security

import nomic.config.security.filters.BasicAuthenticationSecurityFilter
import nomic.config.security.filters.JWTAuthenticationSecurityFilter
import nomic.domain.auth.ITokenRegistry
import nomic.domain.auth.IUserAuthenticator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * This Spring [Configuration] configures the filter chain to add in [JWTAuthenticationSecurityFilter] and
 * [BasicAuthenticationSecurityFilter] and configures Spring Security to require authentication to all endpoints,
 * with the effect that legitimate JWT authentication is performed on all endpoints other than the token endpoints,
 * while the `api/auth/token` endpoint requires either a basic auth header or a bearer jwt header.
 *
 * @see[org.springframework.context.annotation.Configuration]
 * @see[org.springframework.security.web.SecurityFilterChain]
 */
@Configuration
class RoutingSecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity, tokenRegistry: ITokenRegistry, userAuthenticator: IUserAuthenticator): SecurityFilterChain {
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
