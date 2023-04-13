package nomic.data.repositories.games

import nomic.data.dtos.Games
import nomic.data.dtos.games
import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.less
import org.ktorm.dsl.lessEq
import org.ktorm.entity.filter
import org.ktorm.entity.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.stream.Stream

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
class PaginateGamesSpecificationTest(@Autowired private val db: Database) {

    private companion object {

        val filterListGameTestEntities = {
                game: Games ->
            (1600 lessEq game.gameId) and (game.gameId less 1700)
        }

        @JvmStatic
        fun test_paginate_badOffset(): Stream<Int> {
            return Stream.of(15, 20)
        }

        @JvmStatic
        fun test_paginate_validOffset(): Stream<IntArray> {
            return Stream.of(
                intArrayOf(1602, 1603, 1611, 1612, 1613, 1614, 1621, 1622),
                intArrayOf(1602, 1603, 1611, 1612, 1613, 1614),
                intArrayOf(1602, 1603)
            )
        }

        @JvmStatic
        fun test_paginate_validSize(): Stream<Int> {
            return Stream.of(0, 1, 2, 5, 7)
        }

        @JvmStatic
        fun test_paginate_invalidSize_candidatesExceedSize(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(10, 7),
                Arguments.of(13, 7)
            )
        }
    }

    @ParameterizedTest
    @MethodSource("test_paginate_badOffset")
    fun test_paginate_badOffset(offset: Int) {
        val spec = PaginateGamesSpecification(size = 100u, offset.toUInt())

        val games = spec.apply(db.games).filter(filterListGameTestEntities).toList()

        Assertions.assertThat(games.size).isZero()
    }

    @ParameterizedTest
    @MethodSource("test_paginate_validOffset")
    fun test_paginate_validOffset(offsetGames: IntArray) {
        val spec = PaginateGamesSpecification(size = 100U, offsetGames.size.toUInt())

        val games = spec.apply(db.games).filter(filterListGameTestEntities).toList()

        Assertions.assertThat(games).noneMatch { offsetGames.contains(it.gameId) }
    }

    @ParameterizedTest
    @MethodSource("test_paginate_validSize")
    fun test_paginate_validSize(size: Int) {
        val spec = PaginateGamesSpecification(size.toUInt(), 0U)

        val games = spec.apply(db.games).filter(filterListGameTestEntities).toList()

        Assertions.assertThat(games.size).isEqualTo(size)
    }

    @ParameterizedTest
    @MethodSource("test_paginate_invalidSize_candidatesExceedSize")
    fun test_paginate_invalidSize_candidatesExceedSize(expectedSize: Int, actualSize: Int) {
        val spec = PaginateGamesSpecification(expectedSize.toUInt(), 0U)

        val games = spec.apply(db.games).filter(filterListGameTestEntities).toList()

        Assertions.assertThat(games.size).isEqualTo(actualSize)
    }
}
