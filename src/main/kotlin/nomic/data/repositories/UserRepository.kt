package nomic.data.repositories

import nomic.domain.entities.User
import nomic.domain.entities.Username

interface UserRepository {
    fun create(name: Username) : User

    fun getUserById(id: Int) : User
    fun findUserByName(name: Username) : User

    fun changeUsername(newName: Username) : Unit

    // get games that user is hosting?
    // get games that user is part of?
}