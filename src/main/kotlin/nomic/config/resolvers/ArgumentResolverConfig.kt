package nomic.config.resolvers

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * This configuration class adds the various argument resolvers to Spring.
 *
 * @see[import org.springframework.web.method.support.HandlerMethodArgumentResolver]
 */
@Configuration
class ArgumentResolverConfig : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        super.addArgumentResolvers(resolvers)
        // TODO Consider refactoring this (based on need) to dynamically add resolver
    }
}
