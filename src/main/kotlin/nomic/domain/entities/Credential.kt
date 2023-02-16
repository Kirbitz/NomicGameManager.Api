package nomic.domain.entities

import com.fasterxml.jackson.annotation.JsonValue

class Credential(val user: User, var loginName: LoginName, var passwordHash: PasswordHash)

@JvmInline
value class LoginName(@JsonValue val rawName: String) {
    init {
        TODO(
            "Discuss and add business logic regarding valid usernames " +
                "(Probably alphanumeric with dashes and underlines only)",
        )
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
