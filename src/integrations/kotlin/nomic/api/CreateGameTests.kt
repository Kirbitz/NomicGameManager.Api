package nomic.api

import nomic.api.models.GamesApiModel
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class CreateGameTests(@Autowired val client: TestRestTemplate) : BaseEndToEndTest() {
    private val game = GamesApiModel("New Game", 2)
    private val request = createRequest<GamesApiModel>(game)

    @Test
    fun `Create Game on existing userId`() {
        val entity = client.exchange<String>("/api/game/create", HttpMethod.POST, request)
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.CREATED)
        Assertions.assertThat(entity.body).isEqualTo("Game Created")
    }

    private val game2 = GamesApiModel("###BAD###", 2)
    private val request2 = createRequest<GamesApiModel>(game2)

    @Test
    fun `Create Game with Bad Title (#)`() {
        val entity = client.exchange<String>("/api/game/create", HttpMethod.POST, request2)
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(entity.body).contains("Has Special Characters")
    }

    private val game3 = GamesApiModel("&&&BAD&&&", 2)
    private val request3 = createRequest<GamesApiModel>(game3)

    @Test
    fun `Create Game with Bad Title (&)`() {
        val entity = client.exchange<String>("/api/game/create", HttpMethod.POST, request3)
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(entity.body).contains("Has Special Characters")
    }

    private val game4 = GamesApiModel("@@@BAD@@@", 2)
    private val request4 = createRequest<GamesApiModel>(game4)

    @Test
    fun `Create Game with Bad Title (@)`() {
        val entity = client.exchange<String>("/api/game/create", HttpMethod.POST, request4)
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(entity.body).contains("Has Special Characters")
    }
}
