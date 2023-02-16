package nomic.domain.entities

import org.assertj.core.api.Assertions
import org.assertj.core.api.Condition
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder

class PasswordHashTest
{

    @Test
    fun test_passwordHashes_createDomainObject() {
        val encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()

        val pass1 = encoder.encode("password")
        val pass2 = encoder.encode("Super_alpha012\$#")
        val pass3 = encoder.encode("C#>>>>>>Kotlin")

        Assertions.assertThat(PasswordHash(pass1)).has(Condition({ it.rawHash.equals(pass1)}, "pass1"))
        Assertions.assertThat(PasswordHash(pass2)).has(Condition({ it.rawHash.equals(pass2)}, "pass2"))
        Assertions.assertThat(PasswordHash(pass3)).has(Condition({ it.rawHash.equals(pass3)}, "pass3"))
    }

    @Test
    fun test_passwords_throwException() {
        val encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()

        Assertions.assertThatThrownBy { PasswordHash("hash") }
        Assertions.assertThatThrownBy { PasswordHash("abcdefg") }
        Assertions.assertThatThrownBy { PasswordHash("\$argon2id\$v=2") }
        Assertions.assertThatThrownBy { PasswordHash("super_complicated_password") }
        Assertions.assertThatThrownBy { PasswordHash("alpha_doo_dades") }
    }
}