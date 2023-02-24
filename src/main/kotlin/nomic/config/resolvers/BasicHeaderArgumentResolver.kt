package nomic.config.resolvers

import nomic.api.auth.BasicAuthenticationHeader
import nomic.domain.entities.LoginName
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

/**
 * This argument resolver resolves a [BasicAuthenticationHeader] parameters by loading
 * the current [org.springframework.security.core.context.SecurityContext][SecurityContext] and parsing the authentication token into a
 * [BasicAuthenticationHeader]
 *
 * @see[org.springframework.web.method.support.HandlerMethodArgumentResolver]
 * @see[BasicAuthenticationHeader]
 * @see[org.springframework.security.core.context.SecurityContext]
 * @see[nomic.config.security.filters.BasicAuthenticationSecurityFilter]
 */
class BasicHeaderArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(methodParameter: MethodParameter): Boolean {
        return methodParameter.parameterType.equals(BasicAuthenticationHeader::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val auth = SecurityContextHolder.getContext().authentication
        return BasicAuthenticationHeader(
            auth.principal as LoginName,
            auth.credentials as String
        )
    }
}
