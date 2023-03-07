package nomic.domain.games

import nomic.domain.entities.GameModel

interface IGameDomain {
    /**
     * Creates and formats the game data
     * @param input Model of the game to be made
     */
    fun createGame(input: GameModel)
}
