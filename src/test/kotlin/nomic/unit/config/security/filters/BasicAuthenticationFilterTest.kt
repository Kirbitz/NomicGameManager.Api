package nomic.unit.config.security.filters

import nomic.config.security.filters.BasicAuthenticationSecurityFilter
import nomic.domain.auth.AuthenticationResult
import nomic.domain.auth.IUserAuthenticator
import nomic.domain.entities.EndUser
import nomic.domain.entities.LoginName
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
import java.util.Base64

class BasicAuthenticationFilterTest {
    private val login1 = Pair(LoginName("Alice"), "alicepassword")
    private val user1 = EndUser(100, "Sauron")

    private val login2 = Pair(LoginName("Bob"), "bobpassword")
    private val user2 = EndUser(12, "Saruman")

    private val invalidLogin = Pair(LoginName("Eve"), "evepassword")

    private val authenticator = mock<IUserAuthenticator> {
        on {
            authenticateUserWithCredentials(login1.first, login1.second)
        } doReturn AuthenticationResult(true, user1)
        on {
            authenticateUserWithCredentials(login2.first, login2.second)
        } doReturn AuthenticationResult(true, user2)
        on {
            authenticateUserWithCredentials(invalidLogin.first, invalidLogin.second)
        } doReturn AuthenticationResult(false)
    }

    private val mockMvc = MockMvcBuilders
        .standaloneSetup(TestController())
        .addFilter<StandaloneMockMvcBuilder>(BasicAuthenticationSecurityFilter(authenticator))
        .build()

    @BeforeEach
    fun init() {
        // Hacky solution to ensure that the security context doesn't persist between tests
        SecurityContextHolder.clearContext()
    }

    private fun encode(pair: Pair<LoginName, String>) = encode(pair.first.rawName, pair.second)

    private fun encode(loginName: String, password: String): String {
        return String(Base64.getEncoder().encode("$loginName:$password".toByteArray()))
    }

    @Test
    fun filter_validEndpoint_noLogin_fails() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/token"))
            .andExpect(content().string("null"))
            .andExpect(status().isOk)
    }

    @Test
    fun filter_validEndpoint_validLogin_succeeds() {
        val request1 = MockMvcRequestBuilders.get("/api/auth/token")
            .header(HttpHeaders.AUTHORIZATION, "Basic ${encode(login1)}")

        mockMvc.perform(request1)
            .andExpect(content().string(user1.id.toString()))
            .andExpect(status().isOk)

        val request2 = MockMvcRequestBuilders.get("/api/auth/token")
            .header(HttpHeaders.AUTHORIZATION, "Basic ${encode(login2)}")

        mockMvc.perform(request2)
            .andExpect(status().isOk)
            .andExpect(content().string(user2.id.toString()))
    }

    @Test
    fun filter_validEndpoint_invalidLogin_fails() {
        val request = MockMvcRequestBuilders.get("/api/auth/token")
            .header(HttpHeaders.AUTHORIZATION, "Basic ${encode(invalidLogin)}")

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(content().string("null"))
    }

    @Test
    fun filter_validEndpoint_badLogin_noHeader_fails() {
        val request = MockMvcRequestBuilders.get("/api/auth/token")
            .header(HttpHeaders.AUTHORIZATION, "${encode(invalidLogin)}")

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(content().string("null"))
    }

    @Test
    fun filter_validEndpoint_badLoginName_fails() {
        val request = MockMvcRequestBuilders.get("/api/auth/token")
            .header(HttpHeaders.AUTHORIZATION, "${encode("To be or not to be", "ThatIsTheQuestion")}")

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(content().string("null"))
    }

    @Test
    fun filter_invalidEndpoint_noLogin_fails() {
        mockMvc.perform(MockMvcRequestBuilders.get("/test"))
            .andExpect(content().string("null"))
            .andExpect(status().isOk)
    }

    @Test
    fun filter_invalidEndpoint_validLogin_fails() {
        val request = MockMvcRequestBuilders.get("/test")
            .header(HttpHeaders.AUTHORIZATION, "Basic ${encode(login1)}")

        mockMvc.perform(request)
            .andExpect(content().string("null"))
            .andExpect(status().isOk)
    }

    @Test
    fun filter_invalidEndpoint_invalidLogin_fails() {
        val request = MockMvcRequestBuilders.get("/test")
            .header(HttpHeaders.AUTHORIZATION, "Basic ${encode(invalidLogin)}")

        mockMvc.perform(request)
            .andExpect(content().string("null"))
            .andExpect(status().isOk)
    }

    @Test
    fun filter_invalidEndpoint_badLogin_noHeader_fails() {
        val request = MockMvcRequestBuilders.get("/test")
            .header(HttpHeaders.AUTHORIZATION, "${encode(invalidLogin)}")

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(content().string("null"))
    }

    @Test
    fun filter_invalidEndpoint_badLoginName_fails() {
        val request = MockMvcRequestBuilders.get("/test")
            .header(HttpHeaders.AUTHORIZATION, "${encode("To be or not to be", "ThatIsTheQuestion")}")

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(content().string("null"))
    }

    // This controller serves to provide feedback on the internal workings of the filter,
    // so that the filter are verified in as much isolation as possible.
    @RestController
    private class TestController {

        private fun core(): String {
            val context = SecurityContextHolder.getContext()
            val user = context.authentication?.principal as EndUser?
            return user?.id.toString()
        }

        @GetMapping("/api/auth/token")
        fun testValid(): String {
            return core()
        }

        @GetMapping("/test")
        fun testInvalid(): String {
            return core()
        }
    }
}
