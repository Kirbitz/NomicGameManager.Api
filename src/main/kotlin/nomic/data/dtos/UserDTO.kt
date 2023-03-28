package nomic.data.dtos

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * This DTO represents a user entry in the database and employs KTorm's framework
 *
 * @see[org.ktorm.entity.Entity]
 * @see[nomic.domain.entities.EndUser]
 * @property[id] Represents the primary key id of the user
 * @property[name] Represents the name of the user
 */
interface UserDTO : Entity<UserDTO> {
    companion object : Entity.Factory<UserDTO>()
    var id: Int
    var name: String
}

/**
 * This object represents the users table in the database, employing KTorm's framework.
 *
 * @see[org.ktorm.schema.Table]
 * @property[id] The primary key for [UserDTO]
 * @property[name] The name of the user
 */
object Users : Table<UserDTO>("User") {
    val id = int("userId").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
}

/**
 * A sequence of [UserDTO]
 */
val Database.users get() = this.sequenceOf(Users)
