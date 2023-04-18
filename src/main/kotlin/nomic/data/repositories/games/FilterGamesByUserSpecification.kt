package nomic.data.repositories.games

import nomic.data.dtos.GameDTO
import nomic.data.dtos.Games
import nomic.domain.entities.EndUser
import org.ktorm.dsl.eq
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.filter

/**
 * This specification filters games from the repository call to contain only games created
 * by the provider user.
 *
 * @see[user] The user to filter the games by;
 */
class FilterGamesByUserSpecification(private val user: EndUser) : IGameSpecification {
    override fun apply(sequence: EntitySequence<GameDTO, Games>): EntitySequence<GameDTO, Games> {
        return sequence.filter { user.id eq Games.userId }
    }
}
