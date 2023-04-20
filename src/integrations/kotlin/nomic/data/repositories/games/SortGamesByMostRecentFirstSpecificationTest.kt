package nomic.data.repositories.games

import nomic.data.dtos.games
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.less
import org.ktorm.dsl.lessEq
import org.ktorm.entity.filter
import org.ktorm.entity.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
class SortGamesByMostRecentFirstSpecificationTest(@Autowired private val db: Database) {

    @Test
    fun test_sortGamesBy_mostRecentFirst() {
        val spec = SortGamesByMostRecentFirstSpecification()

        val games = spec.apply(db.games).filter { (1600 lessEq it.gameId) and (it.gameId less 1700) }.toList()

        var lastDate = games.first().createDate
        for (game in games) {
            Assertions.assertThat(lastDate).isAfterOrEqualTo(game.createDate)
            lastDate = game.createDate
        }
    }
}
