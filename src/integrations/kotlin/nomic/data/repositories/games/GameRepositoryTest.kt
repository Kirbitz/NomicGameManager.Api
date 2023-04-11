package nomic.data.repositories.games

import nomic.domain.entities.EndUser
import nomic.domain.entities.GameModel
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.ktorm.database.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.util.stream.Stream

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
class GameRepositoryTest(@Autowired private val db: Database) {

    private companion object {
        private val user1: EndUser = EndUser(50, "ListGamesUser1")
        private val user2: EndUser = EndUser(55, "ListGamesUser2")

        @JvmStatic
        fun test_listGames_badOffset_emptyOptional_Source(): Stream<Arguments> {
            return Stream.of(Arguments.of(user1, 5), Arguments.of(user2, 4))
        }

        @JvmStatic
        fun test_listGames_filtersByUser_Source(): Stream<EndUser> {
            return Stream.of(user1, user2)
        }

        @JvmStatic
        fun test_listGames_skipsValidOffsets_Source(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(user1, intArrayOf(51)),
                Arguments.of(user1, intArrayOf(51, 52)),
                Arguments.of(user1, intArrayOf(51, 52, 54)),
                Arguments.of(user2, intArrayOf(56)),
                Arguments.of(user2, intArrayOf(56, 57))
            )
        }

        @JvmStatic
        fun test_listGames_takesValidSize_Source(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(user1, 1),
                Arguments.of(user1, 2),
                Arguments.of(user1, 3),
                Arguments.of(user1, 4),
                Arguments.of(user2, 1),
                Arguments.of(user2, 2),
                Arguments.of(user2, 3)
            )
        }

        @JvmStatic
        fun test_listGames_partialList_InvalidSize_Source(): Stream<Arguments> {
            return Stream.of(Arguments.of(user1, 20, 4), Arguments.of(user2, 10, 3))
        }

        @JvmStatic
        fun test_listGames_dataFromDatabase_Source(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    user1,
                    arrayOf(
                        GameModel(51, "Foo1", LocalDate.parse("2023-01-01"), null, 50),
                        GameModel(52, "Foo2", LocalDate.parse("2023-01-02"), null, 50),
                        GameModel(53, "Foo3", LocalDate.parse("2023-01-04"), null, 50),
                        GameModel(54, "Foo4", LocalDate.parse("2023-01-03"), null, 50)
                    )
                ),
                Arguments.of(
                    user2,
                    arrayOf(
                        GameModel(56, "Bar1", LocalDate.parse("2023-02-01"), null, 55),
                        GameModel(57, "Bar2", LocalDate.parse("2023-02-02"), null, 55),
                        GameModel(58, "Bar4", LocalDate.parse("2023-02-03"), null, 55)
                    )
                )
            )
        }

        @JvmStatic
        fun test_listGames_descendingOrder_Source(): Stream<EndUser> {
            return Stream.of(user1, user2)
        }
    }

    @ParameterizedTest
    @MethodSource("test_listGames_badOffset_emptyOptional_Source")
    fun test_listGames_badOffset_emptyOptional(user: EndUser, offset: UInt) {
        val repo = GameRepository(db)
        val size = 100u

        val games = repo.listGames(user, size, offset)

        Assertions.assertThat(games).isEmpty
    }

    @ParameterizedTest
    @MethodSource("test_listGames_filtersByUser_Source")
    fun test_listGames_filtersByUser(user: EndUser) {
        val repo = GameRepository(db)
        val size = 100u
        val offset = 0u

        val games = repo.listGames(user, size, offset)

        Assertions.assertThat(games.get()).allMatch { it.userId == user.id }
    }

    @ParameterizedTest
    @MethodSource("test_listGames_skipsValidOffsets_Source")
    fun test_listGames_skipsValidOffsets(user: EndUser, offsetGames: IntArray) {
        val repo = GameRepository(db)
        val size = 100u

        val games = repo.listGames(user, size, offsetGames.size.toUInt())

        Assertions.assertThat(games.get()).noneMatch { offsetGames.contains(it.gameId!!) }
    }

    @ParameterizedTest
    @MethodSource("test_listGames_takesValidSize_Source")
    fun test_listGames_takesValidSize(user: EndUser, size: UInt) {
        val repo = GameRepository(db)
        val offset = 0u

        val games = repo.listGames(user, size, offset)

        Assertions.assertThat(games.get().size.toUInt()).isEqualTo(size)
    }

    @ParameterizedTest
    @MethodSource("test_listGames_partialList_InvalidSize_Source")
    fun test_listGames_partialList_InvalidSize(user: EndUser, expectedSize: UInt, actualSize: UInt) {
        val repo = GameRepository(db)
        val offset = 0u

        val games = repo.listGames(user, expectedSize, offset)

        Assertions.assertThat(games.get().size.toUInt()).isEqualTo(actualSize)
    }

    @ParameterizedTest
    @MethodSource("test_listGames_dataFromDatabase_Source")
    fun test_listGames_dataFromDatabase(user: EndUser, expectedGames: Array<GameModel>) {
        val repo = GameRepository(db)
        val offset = 0u
        val size = 100u

        val games = repo.listGames(user, size, offset)

        val expectedGamesMap = expectedGames.associateBy { it.gameId }
        val gamesMap = games.get().associateBy { it.gameId }
        val doMatch = { model1: GameModel, model2: GameModel ->
            val result = model1.gameId == model2.gameId &&
                model1.userId.equals(model2.userId) &&
                model1.title.equals(model2.title) &&
                model1.createDate == model2.createDate &&
                model1.currentPlayer == model2.currentPlayer
            result
        }

        Assertions.assertThat(games.get()).allMatch {
            doMatch(it, expectedGamesMap[it.gameId]!!)
        }
        Assertions.assertThat(expectedGames).allMatch { doMatch(it, gamesMap[it.gameId]!!) }
    }

    @ParameterizedTest
    @MethodSource("test_listGames_descendingOrder_Source")
    fun test_listGames_descendingOrder(user: EndUser) {
        val repo = GameRepository(db)
        val size = 100u
        val offset = 0u

        val games = repo.listGames(user, size, offset).get()

        var lastDate = games.first().createDate
        for (game in games) {
            Assertions.assertThat(lastDate).isBeforeOrEqualTo(game.createDate)
            lastDate = game.createDate
        }
    }
}
