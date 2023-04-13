package nomic.data.repositories.games

import nomic.data.dtos.GameDTO
import nomic.data.dtos.Games
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.drop
import org.ktorm.entity.take

class PaginateGamesSpecification(private val size: UInt, private val offset: UInt) : IGameSpecification {
    override fun apply(sequence: EntitySequence<GameDTO, Games>): EntitySequence<GameDTO, Games> {
        return sequence.drop(offset.toInt()).take(size.toInt())
    }
}
