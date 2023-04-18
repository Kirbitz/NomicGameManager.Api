package nomic.domain.games

import nomic.data.repositories.games.IGameSpecification
import nomic.domain.entities.EndUser

/**
 * This employs the factory pattern to decouple the Ktorm aware specification implementation
 * from the domain layer. Each method constructs and returns a specification implementing
 * the name of the method.
 *
 * This factory focuses on constructing specification operating on game entities.
 */
interface IGameSpecificationFactory {

    /**
     * This method constructs a specification that will filter out games that were not
     * created by the provided user. The effect of it will be that all games in the query
     * will have been created by the provided user.
     *
     * @param[user] The user whose games are desired
     * @return A specification implementing the behavior
     */
    fun filterByUser(user: EndUser): IGameSpecification

    /**
     * This method constructs a specification that will construct a subset of games from a
     * larger set of games. This will allow users to essentially page through all the games
     * while maintaining performance and security.
     *
     * @param[size] How many elements should be in the page
     * @param[offset] How many elements should be skipped before adding entities
     * to the returned page
     * @return A specification implementing the behavior
     */
    fun paginate(size: UInt = 100U, offset: UInt = 0U): IGameSpecification

    /**
     * This method constructs a specification that will sort the list of games in order of
     * game creation date descending. This will lead to the most recently created games being
     * added to the list before older games.
     *
     * @return a specification implementing the behavior
     */
    fun sortByMostRecentFirst(): IGameSpecification
}
