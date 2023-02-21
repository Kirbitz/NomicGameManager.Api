package nomic.data.repositories

import nomic.data.dtos.CredentialDTO
import nomic.data.dtos.UserDTO
import nomic.data.dtos.credentials
import nomic.domain.entities.Credential
import nomic.domain.entities.LoginName
import nomic.domain.entities.PasswordHash
import nomic.domain.entities.User
import org.junit.jupiter.api.Assertions
import org.ktorm.database.Database
import org.ktorm.entity.add
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder

class CredentialRepositoryImplTest {

    private val db: Database
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
            User(2, "Alcibiades"),
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

        db = mock<Database>
        {
            on { it.credentials.add(testCredsDto1) }
            on { it.credentials.add(testCredsDto2) }
        }
    }

    // @Test
    fun create() {
        val repo = CredentialRepositoryImpl(db)

        repo.create(User(0, "Cincinnatus"), LoginName("SimpleFarmer"), password)
        val expectedDto = CredentialDTO {
            this.user = UserDTO {
                this.id = 0
                this.name = "Cincinnatus"
            }

            this.loginName = "SimpleFarmer"
            this.passwordHash = password.rawHash
        }

        verify(db).credentials.add(expectedDto)
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
