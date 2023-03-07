package nomic.data.repositories.implementations

import nomic.data.dtos.CredentialDTO
import nomic.data.dtos.UserDTO
import nomic.data.dtos.credentials
import nomic.domain.entities.Credential
import nomic.domain.entities.LoginName
import nomic.domain.entities.PasswordHash
import nomic.domain.entities.User
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.ktorm.database.Database
import org.ktorm.dsl.eq
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

    private val passwordHasher = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()

    init {
        password = PasswordHash(passwordHasher.encode("password"))

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

    private fun hashPassword(password: String): PasswordHash {
        return PasswordHash(passwordHasher.encode(password))
    }

    @Test
    @Order(-1)
    fun test_create() {
        val repo = CredentialRepository(db)

        val creds1 = repo.create(testCreds1.user, testCreds1.loginName, testCreds1.passwordHash)
        val creds2 = repo.create(testCreds2.user, testCreds2.loginName, testCreds2.passwordHash)

        val dbCreds1 = db.credentials.find { it.userId eq testCredsDto1.user.id }
        val dbCreds2 = db.credentials.find { it.userId eq testCredsDto2.user.id }

        Assertions.assertThat(creds1).usingRecursiveComparison().isEqualTo(testCreds1)
        Assertions.assertThat(dbCreds1).usingRecursiveComparison().isEqualTo(testCredsDto1)

        Assertions.assertThat(creds2).usingRecursiveComparison().isEqualTo(testCreds2)
        Assertions.assertThat(dbCreds2).usingRecursiveComparison().isEqualTo(testCredsDto2)
    }

    @Test
    @Order(0)
    fun test_update_succeeds_username() {
        val repo = CredentialRepository(db)
        val login1 = LoginName("SupermanTheProgrammer")
        val login2 = LoginName("Darth_Vader")

        testCreds1.loginName = login1
        repo.update(testCreds1)

        testCreds2.loginName = login2
        repo.update(testCreds2)

        val testCredsResult1 = db.credentials.find { it.userId eq testCreds1.id }!!
        val testCredsResult2 = db.credentials.find { it.userId eq testCreds2.id }!!

        Assertions.assertThat(testCredsResult1.loginName).isEqualTo(login1.rawName)
        Assertions.assertThat(testCredsResult2.loginName).isEqualTo(login2.rawName)

        Assertions.assertThat(testCreds1.loginName).isEqualTo(login1.rawName)
        Assertions.assertThat(testCreds2.loginName).isEqualTo(login2.rawName)
    }

    @Test
    @Order(0)
    fun test_update_password() {
        val repo = CredentialRepository(db)
        val password1 = hashPassword("aBC*er2iauerhggkjrfbkj")
        val password2 = hashPassword("alpha_zeta_gamma123748159841365y")

        testCreds1.passwordHash = password1
        repo.update(testCreds1)

        testCreds2.passwordHash = password2
        repo.update(testCreds2)

        val testCredsResult1 = db.credentials.find { it.userId eq testCreds1.id }!!
        val testCredsResult2 = db.credentials.find { it.userId eq testCreds2.id }!!

        Assertions.assertThat(testCredsResult1.passwordHash).isEqualTo(password1.rawHash)
        Assertions.assertThat(testCredsResult2.passwordHash).isEqualTo(password2.rawHash)

        Assertions.assertThat(testCreds1.passwordHash).isEqualTo(password1.rawHash)
        Assertions.assertThat(testCreds2.passwordHash).isEqualTo(password2.rawHash)
    }

    @Test
    @Order(0)
    fun test_update_usernameAndPassword() {
        val repo = CredentialRepository(db)

        val login = LoginName("JoeSmith")
        val password = hashPassword("qwertyasdf")

        testCreds1.loginName = login
        testCreds1.passwordHash = password
        repo.update(testCreds1)

        val testCredsResult = db.credentials.find { it.userId eq testCreds1.id }!!

        Assertions.assertThat(testCredsResult.passwordHash).isEqualTo(password.rawHash)
        Assertions.assertThat(testCredsResult.loginName).isEqualTo(login.rawName)

        Assertions.assertThat(testCreds1.passwordHash).isEqualTo(password.rawHash)
        Assertions.assertThat(testCreds1.loginName).isEqualTo(login.rawName)
    }

    @Test
    @Order(0)
    fun test_update_invalidCred() {
        val repo = CredentialRepository(db)

        val testCred = Credential(User(23, "Gandalf"), LoginName("GrayWizard"), hashPassword("Istari"))

        Assertions.assertThatThrownBy { repo.update(testCred) }
    }

    @Test
    @Order(0)
    fun test_getById_badId_emptyOptional() {
        val repo = CredentialRepository(db)

        val cred1 = repo.getById(-100)
        val cred2 = repo.getById(100)

        Assertions.assertThat(cred1).isEmpty
        Assertions.assertThat(cred2).isEmpty
    }

    @Test
    @Order(0)
    fun test_getById_goodId() {
        val repo = CredentialRepository(db)

        val cred1 = repo.getById(testCreds1.id)
        val cred2 = repo.getById(testCreds2.id)

        Assertions.assertThat(cred1).isPresent
        Assertions.assertThat(cred2).isPresent

        Assertions.assertThat(cred1.get().user.name).isEqualTo(testCreds1.user.name)
        Assertions.assertThat(cred2.get().user.name).isEqualTo(testCreds2.user.name)
    }

    @Test
    @Order(0)
    fun test_getByName_badLoginName_emptyOptional() {
        val repo = CredentialRepository(db)

        val cred1 = repo.getByName(LoginName("Supercalifragilisticexpialidocious"))
        val cred2 = repo.getByName(LoginName("GeorgeWashington32"))

        Assertions.assertThat(cred1).isEmpty
        Assertions.assertThat(cred2).isEmpty
    }

    @Test
    @Order(0)
    fun test_getByName_goodLoginName() {
        val repo = CredentialRepository(db)

        val cred1 = repo.getByName(testCreds1.loginName)
        val cred2 = repo.getByName(testCreds2.loginName)

        Assertions.assertThat(cred1).isPresent
        Assertions.assertThat(cred2).isPresent

        Assertions.assertThat(cred1.get().id).isEqualTo(testCreds1.id)
        Assertions.assertThat(cred2.get().id).isEqualTo(testCreds2.id)
    }

    @Test
    @Order(1)
    fun test_delete_goodId() {
        val repo = CredentialRepository(db)

        repo.delete(testCreds1)
        repo.delete(testCreds2)

        val dbCreds1 = db.credentials.find { it.userId eq testCredsDto1.user.id }
        val dbCreds2 = db.credentials.find { it.userId eq testCredsDto2.user.id }

        Assertions.assertThat(dbCreds1).isNull()
        Assertions.assertThat(dbCreds2).isNull()
    }

    @Test
    @Order(1)
    fun test_delete_badId() {
        val repo = CredentialRepository(db)

        val creds = Credential(
            User(1234, "Foo bar"),
            LoginName("FooBar145"),
            hashPassword("pass")
        )

        repo.delete(creds)

        val dbCreds = db.credentials.find { it.userId eq creds.user.id }

        Assertions.assertThat(dbCreds).isNull()
    }
}
