package nomic.data.repositories.games

import nomic.data.dtos.GameDTO
import nomic.data.dtos.Games
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.sortedByDescending

/**
 * This specification sorts the games in the query by their creation dates in descending order.
 * This results in the newest games being foremost in the query
 */
class SortGamesByMostRecentFirstSpecification : IGameSpecification {
    override fun apply(sequence: EntitySequence<GameDTO, Games>): EntitySequence<GameDTO, Games> {
        return sequence.sortedByDescending { it.createDate }
    }
}
