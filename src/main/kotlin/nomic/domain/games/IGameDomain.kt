package nomic.domain.gamesGamesModelimport

interface IGameDomain {
    /**
     * Creates and formats the game data

     */
    fun createGame(userId: String) MutableList<GameModel>
}