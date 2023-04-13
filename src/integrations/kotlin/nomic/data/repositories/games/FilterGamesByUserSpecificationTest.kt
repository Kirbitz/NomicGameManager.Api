package nomic.data.repositories.games

import nomic.domain.entities.EndUser
import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.ktorm.database.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.stream.Stream

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
class FilterGamesByUserSpecificationTest(@Autowired private val db: Database) {
    private companion object {
        private val user1: EndUser = EndUser(1610, "ListGamesUser2")
        private val user2: EndUser = EndUser(1620, "ListGamesUser3")

        @JvmStatic
        fun test_filterByUser_valid(): Stream<EndUser> {
            return Stream.of(user1, user2)
        }

        @JvmStatic
        fun test_filterByUser_invalid(): Stream<EndUser> {
            return Stream.of(
                EndUser(-10, "BadUser1"),
                EndUser(1619, "BadUser2")
            )
        }
    }

    @ParameterizedTest
    @MethodSource("test_filterByUser_valid")
    fun test_filterByUser_valid(user: EndUser) {
        val repo = GameRepository(db)

        val games = repo.listGames(FilterGamesByUserSpecification(user))

        Assertions.assertThat(games).allMatch { it.userId == user.id }
    }

    @ParameterizedTest
    @MethodSource("test_filterByUser_invalid")
    fun test_filterByUser_invalid(user: EndUser) {
        val repo = GameRepository(db)

        val games = repo.listGames(FilterGamesByUserSpecification(user))

        Assertions.assertThat(games).isEmpty()
    }
}
