package nomic.data.repositories.games

import nomic.data.EntityNotFoundException
import nomic.data.dtos.Games
import nomic.data.dtos.games
import nomic.domain.entities.EndUser
import nomic.domain.entities.GameModel
import org.ktorm.database.Database
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.entity.count
import org.ktorm.entity.drop
import org.ktorm.entity.filter
import org.ktorm.entity.map
import org.ktorm.entity.sortedBy
import org.ktorm.entity.take
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.Optional

/**
 * Implementation of the [IGameRepository][nomic.data.repositories.games.IGameRepository] uses
 * Ktorm [Database][org.ktorm.database.Database] as the data access layer
 *
 * @see [nomic.data.repositories.games.IGameRepository]
 * @see [org.ktorm.database.Database]
 * @param db the connected instance of [org.ktorm.database.Database] to use as the database
 */
@Repository
class GameRepository(private val db: Database) : IGameRepository {
    override fun createGame(input: GameModel) {
        // Check to see if game exists
        db.insert(Games) {
            set(it.title, input.title)
            set(it.createDate, LocalDate.now())
            set(it.currentPlayer, null)
            // userId will need to be changed to use the token value
            set(it.userId, input.userId)
        }
    }

    override fun listGames(user: EndUser, size: UInt, offset: UInt): Optional<List<GameModel>> {
        if (db.games.count { it.userId eq user.id }.toUInt() < offset) {
            return Optional.empty()
        }

        return Optional.of(
            db.games.filter { it.userId eq user.id }
                .drop(offset.toInt())
                .take(size.toInt())
                .sortedBy { it.createDate }
                .map {
                    GameModel(
                        it.gameId,
                        it.title,
                        it.createDate,
                        it.currentPlayer,
                        it.user.id
                    )
                }.toList()
        )
    }

    override fun deleteGame(gameId: Int) {
        val result = db.delete(Games) { Games.gameId eq gameId }
        if (result < 1) {
            throw EntityNotFoundException(gameId)
        }
    }
}
