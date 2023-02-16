package nomic.data.dtos

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface UserDTO : Entity<UserDTO> {
    companion object : Entity.Factory<UserDTO>()
    val id: Int
    val name: String
}

object Users : Table<UserDTO>("users") {
    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
}

val Database.users get() = this.sequenceOf(Users)
