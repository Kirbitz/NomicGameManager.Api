package nomic.data.repositories.games


/**
 * Interface that handles actions made on the game table
 */
interface IGameRepository {
    /**
     * Deletes the specified game from the DB
     *
     * @param gameId The id of the game to delete from the DB
     */
    fun deleteGame(gameId: Int)
}
