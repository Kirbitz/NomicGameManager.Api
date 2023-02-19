package nomic.api.auth

import org.assertj.core.api.Assertions
import org.assertj.core.api.Condition
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
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

    // @Test
    fun test_loginFails_noCredentials() {
        val entity = client.getForEntity<LoginResponseModel>("/api/auth/login")
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        Assertions.assertThat(entity.body).isNull()
    }

    // @Test
    fun test_loginFails_badCredentials() {
        val headers = HttpHeaders()
        val creds = Base64.getEncoder().encodeToString("baduser:badpassword".toByteArray())
        headers.set("Authorization", "Basic $creds")

        val request = HttpEntity<Any>(null, headers)
        val entity = client.postForEntity("/api/auth/login", request, LoginResponseModel::class.java)
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body).isNotNull().isNot(hasToken)
    }

    // @Test
    fun test_loginSucceeds() {
        val headers = HttpHeaders()
        // TODO Add credentials
        val creds = Base64.getEncoder().encodeToString("".toByteArray())
        headers.set("Authorization", "Basic $creds")

        val request = HttpEntity<Any>(null, headers)
        val entity = client.postForEntity("/api/auth/login", request, LoginResponseModel::class.java)
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body).isNotNull().has(hasToken)
    }
}
