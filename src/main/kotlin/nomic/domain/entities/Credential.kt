package nomic.domain.entities

import com.fasterxml.jackson.annotation.JsonValue

/**
 * This domain entity represent's a user's credentials
 *
 * @property[user] The user whose credential this object represents
 * @property[loginName] The unique [LoginName] which the user must use to login
 * @property[passwordHash] The hash of the user's password which the user must use to login
 */
class Credential(val user: User, var loginName: LoginName, var passwordHash: PasswordHash) : IEntity {
    override val id: Int
        get() = user.id
}

/**
 * This domain object represents a login name. Business rules specify that it must be unique and
 * that it contains only alphanumeric characters, dashes, or underscores.
 *
 * @throws InvalidLoginNameException When a login name is passed in containing invalid characters
 * @property[rawName] The raw string of the login name
 */
@JvmInline
value class LoginName(@JsonValue val rawName: String) {
    init {
        if (!canParse(rawName)) {
            throw InvalidLoginNameException(rawName)
        }
    }

    companion object {
        // This regex validates that the login name contains only alphanumeric characters or dashes or underscores.
        private val regex = Regex("^[a-zA-Z0-9\\-\\_]{1,45}\$")

        fun canParse(rawName: String?): Boolean {
            if (rawName == null) return false
            return regex.matches(rawName)
        }
    }
}

/**
 * This domain object represents a password hashed with Argon2 and stored in the modular PHC format.
 *
 * @throws InvalidPasswordHashException When a string is passed which is not a Argon2 hash in modular PHC format.
 * @property[rawHash] The raw string of the hash
 */
@JvmInline
value class PasswordHash(val rawHash: String) {
    init {
        // This regex validates that the string is an Argon2 hash encoded with the modular PHC format.
        val regex = Regex("^\\\$argon2(i?d?)\\\$v=(\\d+)\\\$m=(\\d+),t=(\\d+),p=(\\d+)\\\$([a-z0-9A-Z\\-=\\/\\\\+]+)\\\$([a-zA-Z0-9\\-=\\/\\\\+]+)\$")
        if (!regex.matches(rawHash)) {

            throw InvalidPasswordHashException()
        }
    }
}
