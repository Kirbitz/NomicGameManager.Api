package nomic.data.repositories.games

import nomic.domain.entities.EndUser
import nomic.domain.games.IGameSpecificationFactory
import org.springframework.stereotype.Service

@Service
class GameSpecificationFactory : IGameSpecificationFactory {
    override fun filterByUser(user: EndUser): IGameSpecification {
        return FilterGamesByUserSpecification(user)
    }

    override fun paginate(size: UInt, offset: UInt): IGameSpecification {
        return PaginateGamesSpecification(size, offset)
    }

    override fun sortByMostRecentFirst(): IGameSpecification {
        return SortGamesByMostRecentFirstSpecification()
    }
}
