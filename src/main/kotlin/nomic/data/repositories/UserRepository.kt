package nomic.data.repositories

import nomic.data.EntityNotFoundException
import nomic.data.dtos.UserDTO
import nomic.data.dtos.users
import nomic.domain.entities.User
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.springframework.stereotype.Component
import java.util.Optional

/**
 * This interface specifies the repository pattern for user entities.
 *
 * @see[nomic.domain.entities.User]
 */
interface UserRepository : Repository<User> {
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

@Component
class UserRepositoryImpl(private val db: Database) : UserRepository {
    override fun create(name: String): User {
        val userDTO = UserDTO {
            this.name = name
        }

        db.users.add(userDTO)
        return User(userDTO.id, userDTO.name)
    }

    override fun findUserByName(name: String): Optional<User> {
        val userDto = db.users.find { it.name eq name } ?: return Optional.empty()
        return Optional.of(User(userDto.id, userDto.name))
    }

    override fun update(entity: User) {
        val userDto = db.users.find { it.id eq entity.id } ?: throw EntityNotFoundException(entity.id)
        userDto.name = entity.name
        userDto.flushChanges()
    }

    override fun getById(id: Int): Optional<User> {
        val userDto = db.users.find { it.id eq id } ?: return Optional.empty()
        return Optional.of(User(userDto.id, userDto.name))
    }

    override fun delete(entity: User) {
        db.users.find { it.id eq entity.id }?.delete()
    }
}
