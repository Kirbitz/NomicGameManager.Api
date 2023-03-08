package nomic.data.repositories

import nomic.domain.entities.Credential
import nomic.domain.entities.LoginName
import nomic.domain.entities.PasswordHash
import nomic.domain.entities.User
import java.util.Optional

/**
 * This interface specifies the repository pattern for credential entities.
 *
 * @see[nomic.domain.entities.Credential]
 */
interface ICredentialRepository : IRepository<Credential> {
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
