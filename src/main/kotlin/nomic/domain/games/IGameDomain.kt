package nomic.domain.games

import nomic.domain.entities.GameModel

interface IGameDomain {
    /**
     * Creates and formats the game data
     * @param input Model of the [GameModel][nomic.domain.entities.GameModel] to be made
     */
    fun createGame(input: GameModel)
}
