package nomic.data.repositories.implementations

import nomic.data.EntityNotFoundException
import nomic.data.dtos.UserDTO
import nomic.data.dtos.users
import nomic.data.repositories.IUserRepository
import nomic.domain.entities.User
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.springframework.stereotype.Repository
import java.util.Optional

/**
 * This implementation of [nomic.data.repositories.IUserRepository][IUserRepository] uses a
 * KTorm [org.ktorm.database.Database][Database] as the data access layer.
 *
 * @see[nomic.data.repositories.IUserRepository]
 * @see[org.ktorm.database.Database]
 * @param[db] A connected instance of [org.ktorm.database.Database][Database] to use as the database
 */
@Repository
class UserRepository(private val db: Database) : IUserRepository {
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

    override fun delete(entity: User): Boolean {
        val userDto = db.users.find { it.id eq entity.id } ?: return false
        return userDto.delete() > 0
    }
}
