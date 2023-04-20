package nomic.data.repositories.games

import nomic.data.dtos.GameDTO
import nomic.data.dtos.Games
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.drop
import org.ktorm.entity.take

/**
 * This specification constructs a page of games from a larger query. It accepts the maximum
 * quantity of games to be in the list, as well as many games to skip over in creating the page.
 *
 * @see[size] The max number of games
 * @see[offset] How many games should be skipped before listing games
 */
class PaginateGamesSpecification(private val size: UInt, private val offset: UInt) : IGameSpecification {
    override fun apply(sequence: EntitySequence<GameDTO, Games>): EntitySequence<GameDTO, Games> {
        return sequence.drop(offset.toInt()).take(size.toInt())
    }
}
