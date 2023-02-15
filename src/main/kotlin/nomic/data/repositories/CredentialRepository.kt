package nomic.data.repositories

import nomic.domain.entities.Credential
import nomic.domain.entities.PasswordHash
import nomic.domain.entities.User
import nomic.domain.entities.Username
import org.springframework.stereotype.Component


interface CredentialRepository {
    fun create(user: User, passwordHash: PasswordHash) : Credential
    fun changePassword(credential: Credential, newHashedPassword: PasswordHash) : Unit
    fun getByUser(user: User) : Credential
    fun getByName(username: Username) : Credential
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