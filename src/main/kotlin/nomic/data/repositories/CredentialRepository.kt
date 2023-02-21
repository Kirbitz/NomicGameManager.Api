package nomic.data.repositories

import nomic.data.EntityNotFoundException
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
import java.util.Optional

/**
 * This interface specifies the repository pattern for credential entities.
 *
 * @see[nomic.domain.entities.Credential]
 */
interface CredentialRepository : Repository<Credential> {
    /**
     * Creates a new credential entity and persists to the data layer.
     *
     * @see[nomic.domain.entities.LoginName]
     * @see[nomic.domain.entities.PasswordHash]
     *
     * @param[user] The user entity whose credentials are being created
     * @param[loginName] The login name component of the credentials
     * @param[passwordHash] The hash of the password component of the credentials
     * @return The new [nomic.domain.entities.Credential][Credential] domain entity
     */
    fun create(user: User, loginName: LoginName, passwordHash: PasswordHash): Credential

    /**
     * Retrieves the credentials for the provided user entity
     *
     * @param[user] The user with the desired credentials
     * @return An [java.util.Optional][Optional] wrapping the credentials if successful, an empty optional otherwise
     */
    fun getByUser(user: User): Optional<Credential> = getById(user.id)

    /**
     * Retrieves the credentials with the provided login name component
     *
     * @param[loginName] The login name component of the desired credentials
     * @return An [java.util.Optional][Optional] wrapping the credentials if successful, an empty optional otherwise
     */
    fun getByName(loginName: LoginName): Optional<Credential>
}

@Component
class CredentialRepositoryImpl(private val db: Database) : CredentialRepository {

    override fun create(user: User, loginName: LoginName, passwordHash: PasswordHash): Credential {
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
        val userDto = db.users.find { it.id eq id } ?: return Optional.empty()
        val credDto = db.credentials.find { it.userId eq id } ?: return Optional.empty()

        return Optional.of(
            Credential(
                User(id, userDto.name),
                LoginName(credDto.loginName),
                PasswordHash(credDto.passwordHash)
            )
        )
    }

    override fun delete(entity: Credential) {
        db.credentials.find { it.userId eq entity.id }?.delete()
    }

    override fun getByName(loginName: LoginName): Optional<Credential> {
        val credDto = db.credentials.find { it.loginName eq loginName.rawName } ?: return Optional.empty()
        val user = User(credDto.user.id, credDto.user.name)
        return Optional.of(
            Credential(
                user,
                loginName,
                PasswordHash(credDto.passwordHash)
            )
        )
    }
}
