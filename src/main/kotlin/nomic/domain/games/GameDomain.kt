package nomic.domain.games

import nomic.data.repositories.games.GameRepository
import org.springframework.stereotype.Service

/**
 * Implementation of the [IGameDomain][nomic.domain.games.IGameDomain] uses
 * [GameRepository][nomic.data.repositories.games.GameRepository] as a data layer
 *
 * @see [nomic.domain.games.IGameDomain]
 * @see [nomic.data.repositories.games.GameRepository]
 * @param gameRepository the instance of [GameRepository][nomic.data.repositories.games.GameRepository]
 * to use as a data collector
 */
@Service
class GameDomain(private val gameRepository: GameRepository) : IGameDomain {
    override fun deleteGame(gameId: String) {
        val gameIdInt: Int = gameId.toIntOrNull() ?: throw IllegalArgumentException("Please enter a valid GameId!")

        gameRepository.deleteGame(gameIdInt)
    }
}