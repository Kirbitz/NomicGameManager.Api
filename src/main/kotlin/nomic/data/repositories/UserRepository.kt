package nomic.data.repositories

import nomic.domain.entities.User
import nomic.domain.entities.Username
import org.springframework.stereotype.Component

interface UserRepository {
    fun create(name: Username) : User

    fun getUserById(id: Int) : User
    fun findUserByName(name: Username) : User

    fun changeUsername(newName: Username) : Unit

    // get games that user is hosting?
    // get games that user is part of?
}

@Component
class UserRepositoryImpl : UserRepository {
    override fun create(name: Username): User {
        TODO("Not yet implemented")
    }

    override fun getUserById(id: Int): User {
        TODO("Not yet implemented")
    }

    override fun findUserByName(name: Username): User {
        TODO("Not yet implemented")
    }

    override fun changeUsername(newName: Username) {
        TODO("Not yet implemented")
    }

}