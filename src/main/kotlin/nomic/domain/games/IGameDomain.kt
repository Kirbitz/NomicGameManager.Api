package nomic.domain.games

import nomic.domain.entities.GamesModel

interface IGameDomain {
    /**
     * Creates and formats the game data
     *
     * @param gameId The id of the game to be made
     * @param name The name of the game to be made
     * @param createDate The date that the game was created
     */
    fun createGame(title: String, currentPlayer: String)
}