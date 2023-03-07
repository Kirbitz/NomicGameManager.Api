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
    override fun createGame(input: GameModel) {
        val regex = "^[A-Za-z0-9 .!?]*$".toRegex()

        if (!regex.matches(input.title)) {
            throw IllegalArgumentException("Has Special Characters")
        }

        gameRepository.createGame(input)
    }
}
