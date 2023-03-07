package nomic.domain.games

import nomic.domain.entities.GameModel

interface IGameDomain {
    /**
     * Creates and formats the game data
     */
    fun createGame(input: GameModel)
}
