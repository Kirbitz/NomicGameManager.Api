package nomic.game.manager.data.repositories

import nomic.game.manager.domain.entities.User
import nomic.game.manager.domain.entities.Username

interface UserRepository {
    fun create(name: Username) : User

    fun getUserById(id: Int) : User
    fun findUserByName(name: Username) : User

    fun changeUsername(newName: Username) : Unit

    // get games that user is hosting?
    // get games that user is part of?
}