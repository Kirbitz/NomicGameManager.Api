package nomic.data.repositories.implementations

import nomic.data.EntityNotFoundException
import nomic.data.dtos.CredentialDTO
import nomic.data.dtos.credentials
import nomic.data.dtos.users
import nomic.data.repositories.ICredentialRepository
import nomic.domain.entities.Credential
import nomic.domain.entities.EndUser
import nomic.domain.entities.LoginName
import nomic.domain.entities.PasswordHash
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.springframework.stereotype.Repository
import java.util.Optional

/**
 * This implementation of [nomic.data.repositories.ICredentialRepository][ICredentialRepository] uses a
 * KTorm [org.ktorm.database.Database][Database] as the data access layer.
 *
 * @see[nomic.data.repositories.ICredentialRepository]
 * @see[org.ktorm.database.Database]
 * @param[db] A connected instance of [org.ktorm.database.Database][Database] to use as the database
 */
@Repository
class CredentialRepository(private val db: Database) : ICredentialRepository {

    override fun create(user: EndUser, loginName: LoginName, passwordHash: PasswordHash): Credential {
        var userDto = db.users.find { it.id eq user.id } ?: throw EntityNotFoundException(user.id)

        val credential = CredentialDTO {
            this.user = userDto
            this.loginName = loginName.rawName
            this.passwordHash = passwordHash.rawHash
        }

        db.credentials.add(credential)
        return Credential(user, loginName, passwordHash)
    }

    override fun update(entity: Credential) {
        val credDto = db.credentials.find { it.userId eq entity.user.id } ?: throw EntityNotFoundException(entity.id)

        credDto.passwordHash = entity.passwordHash.rawHash
        credDto.loginName = entity.loginName.rawName

        credDto.flushChanges()
    }

    override fun getById(id: Int): Optional<Credential> {
        val credDto = db.credentials.find { it.userId eq id } ?: return Optional.empty()

        return Optional.of(
            Credential(
                EndUser(id, credDto.user.name),
                LoginName(credDto.loginName),
                PasswordHash(credDto.passwordHash)
            )
        )
    }

    override fun delete(entity: Credential): Boolean {
        val credDto = db.credentials.find { it.userId eq entity.id } ?: return false
        return credDto.delete() > 0
    }

    override fun getByName(loginName: LoginName): Optional<Credential> {
        val credDto = db.credentials.find { it.loginName eq loginName.rawName } ?: return Optional.empty()
        val user = EndUser(credDto.user.id, credDto.user.name)
        return Optional.of(
            Credential(
                user,
                loginName,
                PasswordHash(credDto.passwordHash)
            )
        )
    }
}
