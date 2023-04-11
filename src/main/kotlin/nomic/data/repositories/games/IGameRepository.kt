package nomic.data.repositories.games

import nomic.domain.entities.EndUser
import nomic.domain.entities.GameModel
import java.util.Optional

/**
 * Interface manipulates game data in the DB
 *
 * @see [nomic.domain.entities.GameModel]
 */

interface IGameRepository {
    /**
     * Creates a new game
     *
     * @param input Model of the [GameModel][nomic.domain.entities.GameModel] to be made
     */
    fun createGame(input: GameModel)

    /**
     * Retrives the specified user's most recent games in descending order
     *
     * @param[user] The user who created the games
     * @param[size] The maximum number of games returned
     * @param[offset] How many games to skip to allow for pagination.
     * @return An empty optional if there were no valid games based on the offset,
     * otherwise a list up to @see[size] games in descending order
     */
    fun listGames(user: EndUser, size: UInt, offset: UInt): Optional<List<GameModel>>

    /**
     * Deletes the specified game from the DB
     *
     * @param gameId The id of the game to delete from the DB
     */
    fun deleteGame(gameId: Int)
}
