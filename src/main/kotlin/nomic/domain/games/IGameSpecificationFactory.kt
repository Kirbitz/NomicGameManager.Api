package nomic.domain.games

import nomic.data.repositories.games.IGameSpecification
import nomic.domain.entities.EndUser

interface IGameSpecificationFactory {
    fun filterByUser(user: EndUser): IGameSpecification
    fun paginate(size: UInt = 100U, offset: UInt = 0U): IGameSpecification
    fun sortByMostRecentFirst(): IGameSpecification
}
