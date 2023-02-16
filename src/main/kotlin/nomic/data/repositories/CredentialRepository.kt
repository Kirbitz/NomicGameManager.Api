package nomic.data.repositories

import nomic.data.dtos.CredentialDTO
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

interface Repository<TEntity> {
    fun update(entity: TEntity)
}

interface CredentialRepository : Repository<Credential> {
    fun create(user: User, loginName: LoginName, passwordHash: PasswordHash): Credential
    fun getByUser(user: User): Credential
    fun getByName(loginName: LoginName): Credential
}

// TODO: Implement Credentials Repository once the database is setup
@Component
class CredentialRepositoryImpl(private val db: Database) : CredentialRepository {

    override fun create(user: User, loginName: LoginName, passwordHash: PasswordHash): Credential {
        val userDto = db.users.find { it.id eq user.id } ?: TODO("Proper not found exception")

        val credential = CredentialDTO {
            this.user = userDto
            this.username = loginName.rawName
            this.passwordHash = passwordHash.rawHash
        }

        db.credentials.add(credential)
        return Credential(user, loginName, passwordHash)
    }

    override fun update(credential: Credential) {
        val credDto = db.credentials.find { it.userId eq credential.user.id } ?: return

        credDto.passwordHash = credential.passwordHash.rawHash
        credDto.username = credential.loginName.rawName

        credDto.flushChanges()
    }

    override fun getByUser(user: User): Credential {
        val credDto = db.credentials.find { it.userId eq user.id } ?: TODO("Not Found")
        return Credential(
            user,
            LoginName(credDto.username),
            PasswordHash(credDto.passwordHash),
        )
    }

    override fun getByName(loginName: LoginName): Credential {
        val credDto = db.credentials.find { it.username eq loginName.rawName } ?: TODO("Not Found")
        val user = User(credDto.user.id, credDto.user.name)
        return Credential(
            user,
            loginName,
            PasswordHash(credDto.passwordHash),
        )
    }
}
