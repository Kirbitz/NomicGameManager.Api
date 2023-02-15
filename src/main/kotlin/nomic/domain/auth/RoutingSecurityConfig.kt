package nomic.domain.auth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class RoutingSecurityConfig {

    @Bean
    fun filterChain(http : HttpSecurity, tokenRegistry: TokenRegistry) : SecurityFilterChain {
        val filter = AuthenticationSecurityFilter(tokenRegistry)
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter::class.java)

        http.httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests {
                it.requestMatchers("/auth/login").permitAll()
                    .anyRequest().authenticated()
            }
        return http.build()
    }
}