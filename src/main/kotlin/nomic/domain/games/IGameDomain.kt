package nomic.domain.games

/**
 * Manages data from the [GameRepository][nomic.data.repositories.games.GameRepository] for the Game Api
 */
interface IGameDomain {
    /**
     * Sends call to [GameRepository][nomic.data.repositories.games.GameRepository] to delete the game
     *
     * @param gameId The id of the game to delete
     */
    fun deleteGame(gameId: String)
}