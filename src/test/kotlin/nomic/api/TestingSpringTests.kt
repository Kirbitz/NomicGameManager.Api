package nomic.api

import nomic.domain.auth.TokenRegistry
import nomic.domain.entities.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class TestingSpringTests(
    @Autowired val client: TestRestTemplate,
    @Autowired tokenRegistry: TokenRegistry
) : BaseEndToEndTest(tokenRegistry) {

    private val request = createRequest<Any>(user = User(1, "Foo Bar Jr."))

    @Test
    fun `Basic Endpoint Fail Test No Auth`() {
        val entity = client.exchange<String>("/api/hello/springboot", HttpMethod.GET, createRequest<Any>())
        assertThat(entity.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        assertThat(entity.body).isNull()
    }
}
