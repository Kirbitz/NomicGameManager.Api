package nomic.data.repositories.implementations

import nomic.data.dtos.CredentialDTO
import nomic.data.dtos.UserDTO
import nomic.data.dtos.credentials
import nomic.domain.entities.Credential
import nomic.domain.entities.LoginName
import nomic.domain.entities.PasswordHash
import nomic.domain.entities.User
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import org.ktorm.entity.find
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
class CredentialRepositoryTest(@Autowired private val db: Database) {
    private val password: PasswordHash

    private val testCreds1: Credential
    private val testCredsDto1: CredentialDTO

    private val testCreds2: Credential
    private val testCredsDto2: CredentialDTO

    init {
        val encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()
        password = PasswordHash(encoder.encode("password"))

        testCreds1 = Credential(
            User(0, "Cincinnatus"),
            LoginName("SimpleFarmer"),
            password
        )

        testCredsDto1 = CredentialDTO {
            this.user = UserDTO {
                this.id = testCreds1.user.id
                this.name = testCreds1.user.name
            }

            this.loginName = testCreds1.loginName.rawName
            this.passwordHash = testCreds1.passwordHash.rawHash
        }

        testCreds2 = Credential(
            User(3, "Alcibiades"),
            LoginName("StrongDoWhatTheyCan"),
            password
        )

        testCredsDto2 = CredentialDTO {
            this.user = UserDTO {
                this.id = testCreds2.user.id
                this.name = testCreds2.user.name
            }

            this.loginName = testCreds2.loginName.rawName
            this.passwordHash = testCreds2.passwordHash.rawHash
        }
    }

    @Test
    fun create() {
        val repo = CredentialRepository(db)

        val creds = repo.create(testCreds1.user, testCreds1.loginName, testCreds1.passwordHash)

        Assertions.assertThat(creds).usingRecursiveComparison().isEqualTo(testCreds1)
        Assertions.assertThat(db.credentials.find { it.userId eq testCredsDto1.user.id }).usingRecursiveComparison().isEqualTo(testCredsDto1)
    }

    // @Test
    fun update() {
        Assertions.fail<String>("")
    }

    // @Test
    fun getById() {
        Assertions.fail<String>("")
    }

    // @Test
    fun delete() {
        Assertions.fail<String>("")
    }

    // @Test
    fun getByName() {
        Assertions.fail<String>("")
    }
}
