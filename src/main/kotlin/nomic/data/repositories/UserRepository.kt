package nomic.data.repositories

import nomic.domain.entities.User
import nomic.domain.entities.LoginName
import org.springframework.stereotype.Component

interface UserRepository {
    fun create(name: LoginName) : User

    fun getUserById(id: Int) : User
    fun findUserByName(name: LoginName) : User

    fun changeUsername(newName: LoginName) : Unit

    // get games that user is hosting?
    // get games that user is part of?
}

@Component
class UserRepositoryImpl : UserRepository {
    override fun create(name: LoginName): User {
        TODO("Not yet implemented")
    }

    override fun getUserById(id: Int): User {
        TODO("Not yet implemented")
    }

    override fun findUserByName(name: LoginName): User {
        TODO("Not yet implemented")
    }

    override fun changeUsername(newName: LoginName) {
        TODO("Not yet implemented")
    }
}