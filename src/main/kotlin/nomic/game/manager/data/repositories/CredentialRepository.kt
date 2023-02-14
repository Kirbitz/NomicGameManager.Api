package nomic.game.manager.data.repositories

import nomic.game.manager.domain.entities.Credential
import nomic.game.manager.domain.entities.PasswordHash
import nomic.game.manager.domain.entities.User
import nomic.game.manager.domain.entities.Username


interface CredentialRepository {
    fun create(user: User, passwordHash: PasswordHash) : Credential
    fun changePassword(credential: Credential, newHashedPassword: PasswordHash) : Unit
    fun getByUser(user: User) : Credential
    fun getByName(username: Username) : Credential
}


// TODO: Implement Credentials Repository once the database is setup