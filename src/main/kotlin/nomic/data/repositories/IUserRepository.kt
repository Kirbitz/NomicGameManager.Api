package nomic.data.repositories

import nomic.domain.entities.User
import java.util.Optional

/**
 * This interface specifies the repository pattern for user entities.
 *
 * @see[nomic.domain.entities.User]
 */
interface IUserRepository : IRepository<User> {
    /**
     * Creates a new user entity and persists to the data layer.
     *
     * @param[name] The name of the user
     * @return The new [nomic.domain.entities.User][User] domain entity
     */
    fun create(name: String): User

    /**
     * Retrieves the user with the provided name
     *
     * @param[name] The name of the desired user
     * @return An [java.util.Optional][Optional] wrapping the user if successful, an empty optional otherwise
     */
    fun findUserByName(name: String): Optional<User>

    // get games that user is hosting?
    // get games that user is part of?
}
