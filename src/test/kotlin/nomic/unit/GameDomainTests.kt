package nomic.unit

import nomic.data.repositories.games.GameRepository
import nomic.domain.entities.GameModel
import nomic.domain.games.GameDomain
import nomic.domain.games.IGameSpecificationFactory
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.time.LocalDate

class GameDomainTests {
    private val gameDomain: GameDomain
    private val gameRepoMock: GameRepository = mock()

    init {
        gameDomain = GameDomain(gameRepoMock, mock<IGameSpecificationFactory>())
    }

    @Test
    fun `Create Game Valid Game Data`() {
        val inputGame = GameModel(123, "My Awesome Game!", LocalDate.now(), null, 2)
        gameDomain.createGame(inputGame)

        verify(gameRepoMock, times(1)).createGame(inputGame)
    }

    @Test
    fun `Create Game Bad Game Data - Title`() {
        val inputGame = GameModel(123, "!@#$", LocalDate.now(), null, 2)

        assertThrows(IllegalArgumentException::class.java) {
            gameDomain.createGame(inputGame)
        }
    }

    @Test
    fun `Remove Game Valid Game Id`() {
        gameDomain.deleteGame("1234")

        verify(gameRepoMock, times(1)).deleteGame(1234)
    }

    @Test
    fun `Remove Game Bad Game Id`() {
        assertThrows(IllegalArgumentException::class.java) {
            gameDomain.deleteGame("apple")
        }
    }
}
