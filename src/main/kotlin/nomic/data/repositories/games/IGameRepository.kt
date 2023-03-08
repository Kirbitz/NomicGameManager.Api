package nomic.data.repositories.games

import nomic.domain.entities.GameModel

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
     * Deletes the specified game from the DB
     *
     * @param gameId The id of the game to delete from the DB
     */
    fun deleteGame(gameId: Int)
}
