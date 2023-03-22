package nomic.integration.api

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class DeleteGamesTests(@Autowired val client: TestRestTemplate) : BaseEndToEndTest() {
    private val request = createRequest<Any>()

    @Test
    fun `Game Successfully Deleted`() {
        val entity = client.exchange<String>("/api/game/remove/42", HttpMethod.DELETE, request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.ACCEPTED)
        Assertions.assertThat(entity.body).isEqualTo("Game Deleted")
    }

    @Test
    fun `Game Not Deleted Invalid GameId`() {
        val entity = client.exchange<String>("/api/game/remove/penpineappleapplepen", HttpMethod.DELETE, request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(entity.body).contains("Please enter a valid GameId")
    }

    @Test
    fun `Game Not Deleted Game Not Found`() {
        val entity = client.exchange<String>("/api/game/remove/404", HttpMethod.DELETE, request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        Assertions.assertThat(entity.body).contains("not found")
    }
}
