package nomic.domain.games

import nomic.data.repositories.games.GameRepository
import nomic.domain.entities.GameModel
import org.springframework.stereotype.Service

/**
 * Implementation of the [IGameDomain][nomic.domain.games.IGameDomain] uses
 * [GameRepository][nomic.data.repositories.games.GameRepository] as a data layer
 *
 * @see [nomic.domain.games.IGameDomain]
 * @see [nomic.data.repositories.games.GameRepository]
 * @param GameRepository the instance of [GameRepository][nomic.data.repositories.games.GameRepository]
 * to use as a data collector
 */
@Service
class GameDomain(
        private val gameRepository: GameRepository
) : IGameDomain {
    override fun createGame(title: String, currentPlayer: String) {
        val titleString: String = title.toString() ?: throw IllegalArgumentException("Please enter a valid name!")
        val currentPlayerInt: Int = currentPlayer.isNull() ?: throw illegalArgumentException("Please enter a valid currentPlayer!")

        return gameRepository.createGame(titleString, currentPlayerInt)
    }
}