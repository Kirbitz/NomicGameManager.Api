package nomic.config.resolvers

import nomic.api.auth.BasicAuthenticationHeader
import nomic.domain.entities.LoginName
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

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
