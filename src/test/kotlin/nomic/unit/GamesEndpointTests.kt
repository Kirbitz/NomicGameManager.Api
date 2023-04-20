package nomic.unit

import nomic.api.GamesEndpoint
import nomic.api.models.ResponseFormat
import nomic.domain.entities.EndUser
import nomic.domain.entities.GameModel
import nomic.domain.games.GameDomain
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.springframework.http.HttpStatus
import java.time.LocalDate
import java.util.Optional

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
    fun test_listGames_validParameters() {
        val user = EndUser(1, "Foo Bar")
        val domain = mock<GameDomain>() {
            on(it.listGames(user, 100U, 0U)) doReturn Optional.of(listOf())
        }

        val endpoint = GamesEndpoint(domain)

        val result = endpoint.listGames(100U, 0U, user).body as ResponseFormat<*>

        Assertions.assertThat(result.success).isTrue
        Assertions.assertThat(result.status).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(result.data).isInstanceOf(List::class.java)
    }

    @Test
    fun test_listGames_invalidSize() {
        val user = EndUser(1, "Foo Bar")
        val size = 1000u
        val domain = mock<GameDomain>() {
            on(it.listGames(user, size, 0U)) doReturn Optional.of(listOf())
        }

        val endpoint = GamesEndpoint(domain)

        val result = endpoint.listGames(size, 0U, user).body as ResponseFormat<*>

        Assertions.assertThat(result.success).isFalse
        Assertions.assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(result.data).isInstanceOf(String::class.java)
    }

    @Test
    fun test_listGames_invalidOffset() {
        val user = EndUser(1, "Foo Bar")
        val domain = mock<GameDomain>() {
            on(it.listGames(user, 100U, 0U)) doReturn Optional.empty()
        }

        val endpoint = GamesEndpoint(domain)

        val result = endpoint.listGames(100U, 0U, user).body as ResponseFormat<*>

        Assertions.assertThat(result.success).isFalse
        Assertions.assertThat(result.status).isEqualTo(HttpStatus.NOT_FOUND)
        Assertions.assertThat(result.data).isInstanceOf(String::class.java)
    }

    @Test
    fun `Delete Game with Id Provided and Proper Response Object Returned`() {
        val result = gameEndpoint.deleteGame("1234").body as ResponseFormat<String>

        Assertions.assertThat(result.success).isTrue
        Assertions.assertThat(result.status).isEqualTo(HttpStatus.ACCEPTED)
        Assertions.assertThat(result.data).contains("Game Deleted")
    }
}
