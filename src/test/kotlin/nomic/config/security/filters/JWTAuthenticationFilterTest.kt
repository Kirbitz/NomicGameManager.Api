package nomic.config.security.filters

import nomic.domain.auth.ITokenRegistry
import nomic.domain.auth.TokenValidationResult
import nomic.domain.entities.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

class JWTAuthenticationFilterTest {

    @BeforeEach
    fun init() {
        // Hacky solution to ensure that the security context doesn't persist between tests
        SecurityContextHolder.clearContext()
    }

    @Test
    fun filter_validToken_succeeds() {
        // Since we mock the token registry, we don't need a legit token.
        val token1 = "LegitToken1"
        val user1 = User(100, "Frodo Baggins")

        val token2 = "LegitToken2"
        val user2 = User(12, "Aragorn")

        val tokenRegistry = mock<ITokenRegistry> {
            on { validateToken(token1) } doReturn TokenValidationResult(true, user1)
            on { validateToken(token2) } doReturn TokenValidationResult(true, user2)
        }

        val mockMvc = MockMvcBuilders
            .standaloneSetup(TestController())
            .addFilter<StandaloneMockMvcBuilder>(JWTAuthenticationSecurityFilter(tokenRegistry))
            .build()

        val request1 = MockMvcRequestBuilders.get("/test")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $token1")

        mockMvc.perform(request1)
            .andExpect(content().string(user1.id.toString()))
            .andExpect(status().isOk)

        val request2 = MockMvcRequestBuilders.get("/test")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $token2")

        mockMvc.perform(request2)
            .andExpect(status().isOk)
            .andExpect(content().string(user2.id.toString()))
    }

    @Test
    fun filter_invalidToken_fails() {
        // Since we mock the token registry, we don't need a legit token.
        val token = "BadToken"

        val tokenRegistry = mock<ITokenRegistry> {
            on { validateToken(token) } doReturn TokenValidationResult(false)
        }

        val mockMvc = MockMvcBuilders
            .standaloneSetup(TestController())
            .addFilter<StandaloneMockMvcBuilder>(JWTAuthenticationSecurityFilter(tokenRegistry))
            .build()

        val request = MockMvcRequestBuilders.get("/test")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $token")

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(content().string("null"))
    }

    @RestController
    private class TestController {

        @GetMapping("/test")
        fun test(): String {
            val context = SecurityContextHolder.getContext()
            val user = context.authentication?.principal as User?
            return user?.id.toString()
        }
    }
}
