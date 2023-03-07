package nomic.api.auth

import nomic.domain.auth.ITokenRegistry
import nomic.domain.entities.User
import org.assertj.core.api.Assertions
import org.assertj.core.api.Condition
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import java.util.*
import java.util.function.Predicate

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class TokenEndpointTest(@Autowired val client: TestRestTemplate) {

    private val hasToken = Condition<LoginResponseModel?>(Predicate { it.token != null }, "Login Response has Token")

    @Test
    fun test_tokenFails_noCredentials() {
        val request = HttpEntity<Any>(HttpHeaders())
        val entity = client.postForEntity<String>("/api/auth/token", request)
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        Assertions.assertThat(entity.body).isNull()
    }

    @Test
    fun test_loginFails_login_wrongAuth() {
        val headers = HttpHeaders()
        var creds = Base64.getEncoder().encodeToString("FakeUser:Real*Password".toByteArray())
        headers.set("Authorization", "Bearer $creds")

        val request = HttpEntity<Any>(headers)
        val entity = client.postForEntity<String>("/api/auth/token", request)
        Assertions.assertThat(entity.body).isNull()
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun test_loginFails_login_malformedAuth() {
        val headers = HttpHeaders()
        var creds = Base64.getEncoder().encodeToString("FakeUser@Real*Password".toByteArray())
        headers.set("Authorization", "Basic $creds")

        val request = HttpEntity<Any>(headers)
        val entity = client.postForEntity<String>("/api/auth/token", request)
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        Assertions.assertThat(entity.body).isNull()
    }

    @Test
    fun test_tokenFails_login_badCredentials() {
        val headers = HttpHeaders()
        val creds = Base64.getEncoder().encodeToString("FakeUser:Real*Password".toByteArray())
        headers.set("Authorization", "Basic $creds")

        val request = HttpEntity<Any>(headers)
        val entity = client.postForEntity<LoginResponseModel>("/api/auth/token", request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        Assertions.assertThat(entity.body).isNull()
    }

    @Test
    fun test_tokenSucceeds_login() {
        val creds = Base64.getEncoder().encodeToString("TestUser:password".toByteArray())
        val headers = HttpHeaders()
        headers.set("Authorization", "Basic $creds")

        val request = HttpEntity<Any>(headers)
        val entity = client.postForEntity<LoginResponseModel>("/api/auth/token", request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body).isNotNull().has(hasToken)
    }

    @Test
    fun test_tokenSucceeds_refreshToken(@Autowired tokenRegistry: ITokenRegistry) {
        val headers = HttpHeaders()
        headers.setBearerAuth(tokenRegistry.issueToken(User(1, "Foo Bar Jr.")))

        val request = HttpEntity<Any>(headers)
        val entity = client.postForEntity("/api/auth/token", request, LoginResponseModel::class.java)
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body).isNotNull().has(hasToken)
    }
}
