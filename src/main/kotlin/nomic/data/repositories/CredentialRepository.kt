package nomic.data.repositories

import nomic.domain.entities.Credential
import nomic.domain.entities.PasswordHash
import nomic.domain.entities.User
import nomic.domain.entities.LoginName
import org.springframework.stereotype.Component

interface Repository<TEntity> {
    fun update(entity: TEntity)
}

interface CredentialRepository : Repository<Credential> {
    fun create(user: User, loginName: LoginName, passwordHash: PasswordHash) : Credential
    fun getByUser(user: User) : Credential
    fun getByName(loginName: LoginName) : Credential
}

// TODO: Implement Credentials Repository once the database is setup
@Component
class CredentialRepositoryImpl : CredentialRepository{
    override fun create(user: User, passwordHash: PasswordHash): Credential {
        TODO("Not yet implemented")
    }

    override fun changePassword(credential: Credential, newHashedPassword: PasswordHash) {
        TODO("Not yet implemented")
    }

    override fun getByUser(user: User): Credential {
        TODO("Not yet implemented")
    }

    override fun getByName(username: Username): Credential {
        TODO("Not yet implemented")
    }

}