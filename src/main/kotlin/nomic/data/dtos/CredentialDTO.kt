package nomic.data.dtos

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * This DTO represents a credential entry in the database and employs KTorm's framework.
 *
 * @see[org.ktorm.entity.Entity]
 * @see[nomic.domain.entities.Credential]
 * @property[user] Represents a foreign key connection to a user in the database
 * @property[loginName] Represents a [nomic.domain.entities.LoginName][LoginName]
 * @property[passwordHash] Represents a [nomic.domain.entities.PasswordHash][PasswordHash]
 */
interface CredentialDTO : Entity<CredentialDTO> {
    companion object : Entity.Factory<CredentialDTO>()
    var user: UserDTO
    var loginName: String
    var passwordHash: String
}

/**
 * This object represents the credentials table in the database, employing KTorm's framework.
 *
 * @see[org.ktorm.schema.Table]
 * @property[userId] The foreign key to the [Users] table and also the primary key for [CredentialDTO]
 * @property[loginName] The login name component of the credentials
 * @property[passwordHash] The hash of the password component of the credentials
 */
object Credentials : Table<CredentialDTO>("Credential") {
    val userId = int("userId").primaryKey().references(Users) { it.user }
    var loginName = varchar("username").bindTo { it.loginName }
    var passwordHash = varchar("password").bindTo { it.passwordHash }
}

/**
 * A sequence of [CredentialDTO]
 */
val Database.credentials get() = this.sequenceOf(Credentials)
