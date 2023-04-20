package nomic.data.repositories.games

import nomic.data.dtos.GameDTO
import nomic.data.dtos.Games
import nomic.data.dtos.games
import nomic.domain.entities.GameModel
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.filter
import org.ktorm.entity.map
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
class GameRepositoryTest(@Autowired private val db: Database) {

    @Test
    fun test_listGames_dataFromDatabase() {
        val repo = GameRepository(db)
        val expectedGames = db.games.map { GameModel(it.gameId, it.title, it.createDate, it.currentPlayer, it.user.id) }
            .associateBy { it.gameId }.toMutableMap()

        val games = repo.listGames()

        for (game in games) {
            Assertions.assertThat(expectedGames).containsKey(game.gameId)
            val expectedGame = expectedGames[game.gameId]!!

            Assertions.assertThat(game.createDate).isEqualTo(expectedGame.createDate)
            Assertions.assertThat(game.userId).isEqualTo(expectedGame.userId)
            Assertions.assertThat(game.title).isEqualTo(expectedGame.title)
            Assertions.assertThat(game.currentPlayer).isEqualTo(expectedGame.currentPlayer)

            expectedGames.remove(game.gameId)
        }

        Assertions.assertThat(expectedGames.size).isZero()
    }

    @Test
    fun test_listGames_callsSpecificationsInOrder() {
        val repo = GameRepository(db)
        val specUser = IGameSpecification { games -> games.filter { it.userId eq 1601 } }

        var hasFirstSpecRun = false

        val firstSpec = IGameSpecification {
                games ->
            hasFirstSpecRun = true
            games
        }

        val secondSpec = IGameSpecification {
                games ->
            Assertions.assertThat(hasFirstSpecRun).isTrue
            games
        }

        repo.listGames(specUser, firstSpec, secondSpec)
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 2, 4, 8, 16])
    fun test_listGames_callsAllSpecifications(specificationCount: Int) {
        val repo = GameRepository(db)
        val mockSpecifications = mutableListOf<IGameSpecification>()
        var totalSpecificationCalls = 0

        for (i in 1..specificationCount) {
            mockSpecifications.add(
                mock<IGameSpecification> {
                    on(it.apply(any<EntitySequence<GameDTO, Games>>())) doAnswer {
                            answer ->
                        totalSpecificationCalls++
                        answer.getArgument(0)
                    }
                }
            )
        }

        // Filter for the performance of the test so that the repo isn't processing a bunch of entities
        val filterByUser = IGameSpecification { games -> games.filter { it.userId eq 1601 } }

        repo.listGames(filterByUser, *mockSpecifications.toTypedArray())

        Assertions.assertThat(totalSpecificationCalls).isEqualTo(specificationCount)
    }
}
