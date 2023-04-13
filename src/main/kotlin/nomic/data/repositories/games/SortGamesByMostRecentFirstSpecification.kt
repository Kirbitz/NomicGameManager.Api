package nomic.data.repositories.games

import nomic.data.dtos.GameDTO
import nomic.data.dtos.Games
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.sortedByDescending

class SortGamesByMostRecentFirstSpecification : IGameSpecification {
    override fun apply(sequence: EntitySequence<GameDTO, Games>): EntitySequence<GameDTO, Games> {
        return sequence.sortedByDescending { it.createDate }
    }
}
