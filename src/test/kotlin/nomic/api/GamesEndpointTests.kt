package nomic.api

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class GamesEndpointTests(@Autowired val client: TestRestTemplate) {
    @Test
    fun `Game Successfully Deleted`() {
        val httpEntity = HttpEntity<String>("Game Deleted")
        val entity = client.exchange("/api/games/remove/{gameId}", HttpMethod.DELETE, httpEntity, Void::class.java, "42")
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.ACCEPTED)
    }

    @Test
    fun `Game Not Deleted Invalid GameId`() {
        val httpEntity = HttpEntity<String>("Game Deleted")
        val entity = client.exchange("/api/games/remove/{gameId}", HttpMethod.DELETE, httpEntity, Void::class.java, "penpineappleapplepen")
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `Game Not Deleted Game Not Found`() {
        val httpEntity = HttpEntity<String>("Game Deleted")
        val entity = client.exchange("/api/games/remove/{gameId}", HttpMethod.DELETE, httpEntity, Void::class.java, "404")
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }
}