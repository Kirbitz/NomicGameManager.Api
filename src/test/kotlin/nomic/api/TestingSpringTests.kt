package nomic.api

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class TestingSpringTests(@Autowired val client: TestRestTemplate) {
    // @Test
    fun `Basic Endpoint Fail Test No Auth`() {
        val entity = client.getForEntity<String>("/api/hello/springboot")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        assertThat(entity.body).isNull()
    }
}
