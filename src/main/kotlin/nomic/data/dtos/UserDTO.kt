package nomic.data.dtos

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface UserDTO : Entity<UserDTO> {
    val id: Int
    var name: String
}

object Users : Table<UserDTO>("users") {
    val id = int("id").primaryKey().bindTo { it.id }
    var name = varchar("name").bindTo { it.name }
}