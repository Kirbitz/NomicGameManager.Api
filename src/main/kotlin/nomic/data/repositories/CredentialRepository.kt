package nomic.data.repositories

import nomic.data.dtos.CredentialDTO
import nomic.data.dtos.UserDTO
import nomic.data.dtos.credentials
import nomic.data.dtos.users
import nomic.domain.entities.Credential
import nomic.domain.entities.LoginName
import nomic.domain.entities.PasswordHash
import nomic.domain.entities.User
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.springframework.stereotype.Component


interface CredentialRepository : Repository<Credential> {
    fun getByUser(user: User): Credential = getById(user.id)
    fun getByName(loginName: LoginName): Credential
}

// TODO: Implement Credentials Repository once the database is setup
@Component
class CredentialRepositoryImpl(private val db: Database) : CredentialRepository {

    override fun create(entity : Credential) {
        var userDto = db.users.find { it.id eq entity.user.id }

        if (userDto == null) {
            userDto = UserDTO {
                this.id = entity.user.id
                this.name = entity.user.name
            }

            db.users.add(userDto)
        }

        val credential = CredentialDTO {
            this.user = userDto
            this.username = entity.loginName.rawName
            this.passwordHash = entity.passwordHash.rawHash
        }

        db.credentials.add(credential)

    }

    override fun update(entity: Credential) {
        val credDto = db.credentials.find { it.userId eq entity.user.id } ?: return

        credDto.passwordHash = entity.passwordHash.rawHash
        credDto.username = entity.loginName.rawName

        credDto.flushChanges()
    }

    override fun getById(id: Int): Credential {
        val userDto = db.users.find { it.id eq id } ?: TODO("Not Found")
        val credDto = db.credentials.find { it.userId eq id } ?: TODO("Not Found")
        return Credential(
            User(userDto.name, userDto.id),
            LoginName(credDto.username),
            PasswordHash(credDto.passwordHash),
        )
    }

    override fun delete(entity: Credential) {
        db.credentials.find { it.userId eq entity.id}?.delete()
    }

    override fun getByName(loginName: LoginName): Credential {
        val credDto = db.credentials.find { it.username eq loginName.rawName } ?: TODO("Not Found")
        val user = User(credDto.user.name, credDto.user.id)
        return Credential(
            user,
            loginName,
            PasswordHash(credDto.passwordHash),
        )
    }
}
