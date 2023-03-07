package nomic.data.repositories.games

import nomic.domain.entities.GameModel

/**
 * Interface creates the game within the DB
 *
 * @see [nomic.domain.entities.GameModel]
 */

interface IGameRepository {
    /**
     * Creates a game
     * @param input Model of the [GameModel][nomic.domain.entities.GameModel] to be made
     */
    fun createGame(input: GameModel)
}
