package nomic.api.auth

import org.assertj.core.api.Assertions
import org.assertj.core.api.Condition
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import java.util.Base64
import java.util.function.Predicate

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class LoginEndpointTest(@Autowired val client: TestRestTemplate) {

    private val hasToken = Condition<LoginResponseModel?>(Predicate { it.token != null }, "Login Response has Token")

    @Test
    fun test_loginFails_noCredentials() {
        val request = HttpEntity<Any>(HttpHeaders())
        val entity = client.postForEntity("/api/auth/login", request, String::class.java)
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        Assertions.assertThat(entity.body).isNull()
    }

    @Test
    fun test_loginFails_wrongAuth() {
        val headers = HttpHeaders()
        var creds = Base64.getEncoder().encodeToString("FakeUser:Real*Password".toByteArray())
        headers.set("Authorization", "Bearer $creds")

        val request = HttpEntity<Any>(headers)
        val entity = client.postForEntity("/api/auth/login", request, String::class.java)
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        Assertions.assertThat(entity.body).isNull()
    }

    @Test
    fun test_loginFails_malformedAuth() {
        val headers = HttpHeaders()
        var creds = Base64.getEncoder().encodeToString("FakeUser@Real*Password".toByteArray())
        headers.set("Authorization", "Basic $creds")

        val request = HttpEntity<Any>(headers)
        val entity = client.postForEntity("/api/auth/login", request, String::class.java)
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        Assertions.assertThat(entity.body).isNull()
    }

    @Test
    fun test_loginFails_badCredentials() {
        val headers = HttpHeaders()
        val creds = Base64.getEncoder().encodeToString("FakeUser:Real*Password".toByteArray())
        headers.set("Authorization", "Basic $creds")

        val request = HttpEntity<Any>(headers)
        val entity = client.postForEntity("/api/auth/login", request, LoginResponseModel::class.java)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
        Assertions.assertThat(entity.body).isNotNull().isNot(hasToken)
    }

    @Test
    fun test_loginSucceeds() {
        val creds = Base64.getEncoder().encodeToString("TestUser:password".toByteArray())
        val headers = HttpHeaders()
        headers.set("Authorization", "Basic $creds")

        val request = HttpEntity<Any>(headers)
        val entity = client.postForEntity("/api/auth/login", request, LoginResponseModel::class.java)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body).isNotNull().has(hasToken)
    }
}
