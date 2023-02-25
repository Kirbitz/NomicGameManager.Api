package nomic.data.repositories.games

import nomic.data.EntityNotFoundException
import nomic.data.dtos.Games
import org.ktorm.database.Database
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.springframework.stereotype.Repository

/**
 * Implementation of the [IGameRepository][nomic.data.repositories.games.IGameRepository] uses
 * Ktorm [Database][org.ktorm.database.Database] as the data access layer
 *
 * @see [nomic.data.repositories.games.IGameRepository]
 * @see [org.ktorm.database.Database]
 * @param db the connected instance of [org.ktorm.database.Database] to use as the database
 */
@Repository
class GameRepository(private val db: Database): IGameRepository {
    override fun deleteGame(gameId: Int) {
        val result = db.delete(Games) { Games.gameId eq gameId }
        if(result < 1) {
            throw EntityNotFoundException(gameId)
        }
    }
}