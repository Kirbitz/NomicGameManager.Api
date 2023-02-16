package nomic.data.repositories

import nomic.data.dtos.UserDTO
import nomic.data.dtos.users
import nomic.domain.entities.LoginName
import nomic.domain.entities.User
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.springframework.stereotype.Component

interface UserRepository : Repository<User> {
    fun findUserByName(name: String): User

    // get games that user is hosting?
    // get games that user is part of?
}

@Component
class UserRepositoryImpl(private val db: Database) : UserRepository {
    override fun findUserByName(name: String): User {
        val userDto = db.users.find { it.name eq name } ?: TODO("Not found")
        return User(userDto.name, userDto.id)
    }

    override fun create(entity: User) {
        val userDto = UserDTO {
            this.name = entity.name
        }

        db.users.add(userDto)
        entity.id = userDto.id
    }

    override fun update(entity: User) {
        val userDto = db.users.find {it.id eq entity.id} ?: TODO("Not Found")
        userDto.name = entity.name
        userDto.flushChanges()
    }

    override fun getById(id: Int): User {
        val userDto = db.users.find { it.id eq id } ?: TODO("Not Found")
        return User(userDto.name, userDto.id)
    }

    override fun delete(entity: User) {
        db.users.find { it.id eq entity.id }?.delete()
    }

}
