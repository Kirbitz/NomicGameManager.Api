package nomic.data.repositories

import nomic.domain.entities.Credential
import nomic.domain.entities.PasswordHash
import nomic.domain.entities.User
import nomic.domain.entities.Username


interface CredentialRepository {
    fun create(user: User, passwordHash: PasswordHash) : Credential
    fun changePassword(credential: Credential, newHashedPassword: PasswordHash) : Unit
    fun getByUser(user: User) : Credential
    fun getByName(username: Username) : Credential
}


// TODO: Implement Credentials Repository once the database is setup