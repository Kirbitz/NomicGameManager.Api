package nomic.domain.games

import nomic.domain.entities.GameModel

/**
 * Manages data from the [GameRepository][nomic.data.repositories.games.GameRepository] for the Game Api
 */
interface IGameDomain {
    /**
     * Creates and formats the game data
     * @param input Model of the [GameModel][nomic.domain.entities.GameModel] to be made
     */
    fun createGame(input: GameModel)

    /**
     * Sends call to [GameRepository][nomic.data.repositories.games.GameRepository] to delete the game
     *
     * @param gameId The id of the game to delete
     */
    fun deleteGame(gameId: String)
}
