package nomic.data.dtos

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface CredentialDTO : Entity<CredentialDTO> {
    companion object : Entity.Factory<CredentialDTO>()
    var user: UserDTO
    var loginName: String
    var passwordHash: String
}

object Credentials : Table<CredentialDTO>("Credential") {
    val userId = int("userId").primaryKey().references(Users) { it.user }
    var loginName = varchar("username").bindTo { it.loginName }
    var passwordHash = varchar("password").bindTo { it.passwordHash }
}

val Database.credentials get() = this.sequenceOf(Credentials)
