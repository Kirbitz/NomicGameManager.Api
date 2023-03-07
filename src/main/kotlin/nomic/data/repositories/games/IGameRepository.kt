package nomic.data.repositories.games

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

    /**
     * Deletes the specified game from the DB
     *
     * @param gameId The id of the game to delete from the DB
     */
    fun deleteGame(gameId: Int)
}
