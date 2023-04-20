package nomic.domain.games

import nomic.data.repositories.games.GameRepository
import nomic.domain.entities.EndUser
import nomic.domain.entities.GameModel
import org.springframework.stereotype.Service
import java.util.Optional

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
class GameDomain(
    private val gameRepository: GameRepository,
    private val specFactory: IGameSpecificationFactory
) : IGameDomain {
    override fun createGame(input: GameModel) {
        val regex = "^[A-Za-z0-9 .!?]*$".toRegex()

        if (!regex.matches(input.title)) {
            throw IllegalArgumentException("Has Special Characters")
        }

        gameRepository.createGame(input)
    }

    override fun listGames(user: EndUser, size: UInt, offset: UInt): Optional<List<GameModel>> {
        // Max size and offset conditions are endpoint logic rather than business logic.

        val games = gameRepository.listGames(
            specFactory.filterByUser(user),
            specFactory.sortByMostRecentFirst(),
            specFactory.paginate(size, offset)
        )

        if (offset > 0u && games.isEmpty()) {
            return Optional.empty()
        }

        return Optional.of(games)
    }

    override fun deleteGame(gameId: String) {
        val gameIdInt: Int = gameId.toIntOrNull() ?: throw IllegalArgumentException("Please enter a valid GameId!")

        gameRepository.deleteGame(gameIdInt)
    }
}
