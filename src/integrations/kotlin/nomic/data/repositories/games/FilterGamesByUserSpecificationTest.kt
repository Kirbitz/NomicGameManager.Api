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

    // These tests use JUnit's parameterized tests and method source. Essentially,
    // JUnit grabs arguments from the method sources and runs each test with
    // each set of arguments. In Java, the method source is just a static method,
    // due to the differences between Java and Kotlin, they get implemented as a
    // function in the companion object with the JvmStatic annotation, and those
    // functions are located at the bottom of these tests.

    @ParameterizedTest
    @MethodSource("parameters_source_valid")
    fun test_filterByUser_valid(user: EndUser) {
        val repo = GameRepository(db)

        val games = repo.listGames(FilterGamesByUserSpecification(user))

        Assertions.assertThat(games).allMatch { it.userId == user.id }
    }

    @ParameterizedTest
    @MethodSource("parameters_source_invalid")
    fun test_filterByUser_invalid(user: EndUser) {
        val repo = GameRepository(db)

        val games = repo.listGames(FilterGamesByUserSpecification(user))

        Assertions.assertThat(games).isEmpty()
    }

    private companion object {
        private val user1: EndUser = EndUser(1610, "ListGamesUser2")
        private val user2: EndUser = EndUser(1620, "ListGamesUser3")

        @JvmStatic
        fun parameters_source_valid(): Stream<EndUser> {
            return Stream.of(user1, user2)
        }

        @JvmStatic
        fun parameters_source_invalid(): Stream<EndUser> {
            return Stream.of(
                EndUser(-10, "BadUser1"),
                EndUser(1619, "BadUser2")
            )
        }
    }
}
