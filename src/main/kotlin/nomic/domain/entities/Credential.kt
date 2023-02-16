package nomic.domain.entities

import com.fasterxml.jackson.annotation.JsonValue

class Credential(val user: User, var loginName: LoginName, var passwordHash: PasswordHash) : Entity {
    override val id: Int
        get() = user.id

}

@JvmInline
value class LoginName(@JsonValue val rawName: String) {
    init {
        // This regex validates that the login name contains only alphanumeric characters or dashes or underscores.
        val regex = Regex("^[a-zA-Z0-9\\-\\_]{1,45}\$")
        if (!regex.matches(rawName)) {
            throw InvalidLoginNameException(rawName)
        }
    }
}

@JvmInline
value class PasswordHash(val rawHash: String) {
    init {
        // This regex validates that the string is an Argon2 hash encoded with the modular PHC format.
        val regex = Regex("^\\\$argon2(i?d?)\\\$v=(\\d+)\\\$m=(\\d+)\\\$t=(\\d+)\\\$p=(\\d+)\\\$([a-zA-Z\\-=\\/\\\\+]+)\\\$([a-zA-Z\\-=\\/\\\\+]+)\$")
        if (!regex.matches(rawHash)) {
            TODO("Add proper exception")
        }
    }
}
