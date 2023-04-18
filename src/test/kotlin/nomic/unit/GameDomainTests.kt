package nomic.unit

import nomic.data.dtos.GameDTO
import nomic.data.dtos.Games
import nomic.data.repositories.games.GameRepository
import nomic.data.repositories.games.IGameSpecification
import nomic.domain.entities.EndUser
import nomic.domain.entities.GameModel
import nomic.domain.games.GameDomain
import nomic.domain.games.IGameSpecificationFactory
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.ktorm.entity.EntitySequence
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
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
    fun test_listGames_correctlyOrchestrated() {
        val calledSpecsOrder = mutableListOf<DummySpecification>()
        val user = EndUser(1, "Foo")
        val size = 100U
        val offset = 0U

        val gameSpecFactory = mock<IGameSpecificationFactory> {
            on(it.filterByUser(user)) doReturn DummySpecification(calledSpecsOrder, "User")
            on(it.paginate(size, offset)) doReturn DummySpecification(calledSpecsOrder, "Paginate")
            on(it.sortByMostRecentFirst()) doReturn DummySpecification(calledSpecsOrder, "Sort")
        }

        val gameRepo = mock<GameRepository> {
            on(it.listGames(any<IGameSpecification>())) doAnswer {
                    answer ->
                for (arg in answer.arguments) {
                    if (arg is DummySpecification) {
                        arg.call()
                    }
                }

                listOf()
            }
        }

        val domain = GameDomain(gameRepo, gameSpecFactory)

        domain.listGames(user, size, offset)

        Assertions.assertThat(calledSpecsOrder).allMatch { it.hasBeenCalled }
        Assertions.assertThat(calledSpecsOrder.size).isEqualTo(3)
        Assertions.assertThat(calledSpecsOrder[0].name).isEqualTo("User")
        Assertions.assertThat(calledSpecsOrder[1].name).isEqualTo("Sort")
        Assertions.assertThat(calledSpecsOrder[2].name).isEqualTo("Paginate")
    }

    // This is a dummy implementation of specification to ensure that the domain
    // service is correctly orchestrating the calls in a way that makes sense.
    private class DummySpecification(
        private val list: MutableList<DummySpecification>,
        val name: String
    ) : IGameSpecification {

        var hasBeenCalled: Boolean = false
            private set

        override fun apply(sequence: EntitySequence<GameDTO, Games>): EntitySequence<GameDTO, Games> = sequence

        fun call() {
            list.add(this)
            hasBeenCalled = true
        }
    }

    @Test
    fun test_listGames_badOffset_emptyOptional() {
        val spec = IGameSpecification { it }
        val user = EndUser(1, "Foo")
        val size = 100U
        val offset = 10U

        val gameSpecFactory = mock<IGameSpecificationFactory> {
            on(it.filterByUser(user)) doReturn spec
            on(it.paginate(size, offset)) doReturn spec
            on(it.sortByMostRecentFirst()) doReturn spec
        }

        val gameRepo = mock<GameRepository> {
            on(it.listGames(spec, spec, spec)) doReturn listOf()
        }

        val domain = GameDomain(gameRepo, gameSpecFactory)

        val games = domain.listGames(user, size, offset)

        Assertions.assertThat(games).isEmpty
    }

    @Test
    fun test_listGames_noElements_emptyList() {
        val spec = IGameSpecification { it }
        val user = EndUser(1, "Foo")
        val size = 100U
        val offset = 0U

        val gameSpecFactory = mock<IGameSpecificationFactory> {
            on(it.filterByUser(user)) doReturn spec
            on(it.paginate(size, offset)) doReturn spec
            on(it.sortByMostRecentFirst()) doReturn spec
        }

        val gameRepo = mock<GameRepository> {
            on(it.listGames(spec, spec, spec)) doReturn listOf()
        }

        val domain = GameDomain(gameRepo, gameSpecFactory)

        val games = domain.listGames(user, size, offset)

        Assertions.assertThat(games).isPresent
        Assertions.assertThat(games.get().size).isZero
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
