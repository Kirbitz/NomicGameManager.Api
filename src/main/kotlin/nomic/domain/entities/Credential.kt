package nomic.domain.entities

class Credential(val user: User, var username: Username, var passwordHash: PasswordHash) {
}

@JvmInline
value class PasswordHash(val rawHash: CharSequence) {
    init {
        val regex = Regex("^\\\$argon2(i?d?)\\\$v=(\\d+)\\\$m=(\\d+)\\\$t=(\\d+)\\\$p=(\\d+)\\\$([a-zA-Z\\-=\\/\\\\+]+)\\\$([a-zA-Z\\-=\\/\\\\+]+)\$")
        if (!regex.matches(rawHash)) {
            TODO("Add proper exception")
        }
    }
}