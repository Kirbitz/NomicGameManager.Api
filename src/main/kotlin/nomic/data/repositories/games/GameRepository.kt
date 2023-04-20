package nomic.data.repositories.games

import nomic.data.EntityNotFoundException
import nomic.data.dtos.Games
import nomic.data.dtos.games
import nomic.domain.entities.GameModel
import org.ktorm.database.Database
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.entity.map
import org.springframework.stereotype.Repository
import java.time.LocalDate

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

    override fun listGames(vararg specifications: IGameSpecification): List<GameModel> {
        var sequence = db.games

        for (specification in specifications) {
            sequence = specification.apply(sequence)
        }

        return sequence.map {
            GameModel(
                it.gameId,
                it.title,
                it.createDate,
                it.currentPlayer,
                it.user.id
            )
        }.toList()
    }

    override fun deleteGame(gameId: Int) {
        val result = db.delete(Games) { Games.gameId eq gameId }
        if (result < 1) {
            throw EntityNotFoundException(gameId)
        }
    }
}
