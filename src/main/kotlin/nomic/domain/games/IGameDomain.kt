package nomic.domain.games

import nomic.domain.entities.EndUser
import nomic.domain.entities.GameModel
import java.util.Optional

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
     * Sends call to [GameRepository][nomic.data.repositories.games.GameRepository] to delete the game
     *
     * @param gameId The id of the game to delete
     */
    fun deleteGame(gameId: String)
}
