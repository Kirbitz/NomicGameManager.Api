package game.manager.nomic.api

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class RulesAmendmentsTests(@Autowired val client: TestRestTemplate) {
    @Test
    fun `Bad Game ID`() {
        val entity = client.getForEntity<String>("/api/rules_amendments/apple")
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(entity.body).contains("gameId was not a valid integer")
    }

    @Test
    fun `Internal Server Error`() {
        val entity = client.getForEntity<String>("/api/rules_amendments/123")
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        Assertions.assertThat(entity.body).contains("Internal server error")
    }
}