package nomic.api

import nomic.api.models.GamesApiModel
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class GamesTests(@Autowired val client: TestRestTemplate) : BaseEndToEndTest() {
    private val game = GamesApiModel("New Game", 1)
    private val request = createRequest<Any>(game)

    @Test
    fun `Create Game on existing userId`() {
        val entity = client.exchange<List<GamesApiModel>>("/api/create/game", HttpMethod.POST, request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
    }
}