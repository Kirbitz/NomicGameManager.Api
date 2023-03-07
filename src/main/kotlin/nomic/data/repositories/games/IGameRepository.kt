package nomic.data.repositories.games

import nomic.domain.entities.GameModel

/**
 * Interface creates the game within the DB
 *
 * @see [nomic.domain.entities.GameModel]]
 */

interface IGameRepository {
    /**
     * Creates a game
     */
    fun createGame(input: GameModel)
}
