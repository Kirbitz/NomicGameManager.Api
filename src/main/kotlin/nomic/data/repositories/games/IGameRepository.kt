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
     *
     * @param title the title of the game to be made
     * @param currentPlayer the id of the currentPlayer for the gam
     */

    fun createGame(title: String, currentPlayer: Int)

}