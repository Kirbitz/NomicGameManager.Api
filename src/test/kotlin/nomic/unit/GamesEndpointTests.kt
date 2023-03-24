package nomic.unit

import nomic.api.GamesEndpoint
import nomic.api.models.ResponseFormat
import nomic.domain.entities.GameModel
import nomic.domain.games.GameDomain
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.springframework.http.HttpStatus
import java.time.LocalDate

class GamesEndpointTests {
    private val gameEndpoint: GamesEndpoint
    private val gameDomainMock: GameDomain = mock()

    init {
        gameEndpoint = GamesEndpoint(gameDomainMock)
    }

    @Test
    fun `Create Game with Game Object Provided and Proper Response Object Returned`() {
        val gameInput = GameModel(13, "Cool Title Bro", LocalDate.now(), null, 42)

        val result = gameEndpoint.createGame(gameInput).body as ResponseFormat<String>

        Assertions.assertThat(result.success).isTrue
        Assertions.assertThat(result.status).isEqualTo(HttpStatus.CREATED)
        Assertions.assertThat(result.data).contains("Game Created")
    }

    @Test
    fun `Delete Game with Id Provided and Proper Response Object Returned`() {
        val result = gameEndpoint.deleteGame("1234").body as ResponseFormat<String>

        Assertions.assertThat(result.success).isTrue
        Assertions.assertThat(result.status).isEqualTo(HttpStatus.ACCEPTED)
        Assertions.assertThat(result.data).contains("Game Deleted")
    }
}
