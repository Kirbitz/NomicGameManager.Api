package nomic.data.repositories.implementations

import nomic.data.EntityNotFoundException
import nomic.data.dtos.CredentialDTO
import nomic.data.dtos.UserDTO
import nomic.data.dtos.credentials
import nomic.domain.entities.Credential
import nomic.domain.entities.EndUser
import nomic.domain.entities.LoginName
import nomic.domain.entities.PasswordHash
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.find
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
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
            EndUser(5, "Cincinnatus"),
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
            EndUser(3, "Alcibiades"),
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
    @Order(1)
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
    @Order(1)
    fun test_create_badUser() {
        val repo = CredentialRepository(db)

        Assertions.assertThatThrownBy {
            repo.create(EndUser(1234, "Dante"), LoginName("Alighieri"), password)
        }.isInstanceOf(EntityNotFoundException::class.java)
    }

    @Test
    @Order(2)
    fun test_getById_badId_emptyOptional() {
        val repo = CredentialRepository(db)

        val cred1 = repo.getById(-100)
        val cred2 = repo.getById(100)

        Assertions.assertThat(cred1).isEmpty
        Assertions.assertThat(cred2).isEmpty
    }

    @Test
    @Order(2)
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
    @Order(2)
    fun test_getByName_badLoginName_emptyOptional() {
        val repo = CredentialRepository(db)

        val cred1 = repo.getByName(LoginName("Supercalifragilisticexpialidocious"))
        val cred2 = repo.getByName(LoginName("GeorgeWashington32"))

        Assertions.assertThat(cred1).isEmpty
        Assertions.assertThat(cred2).isEmpty
    }

    @Test
    @Order(2)
    fun test_getByUser_invalidUser_emptyOptional() {
        val repo = CredentialRepository(db)

        val cred = repo.getByUser(EndUser(1234, "Dante"))

        Assertions.assertThat(cred).isEmpty
    }

    @Test
    @Order(2)
    fun test_getByUser_validUser() {
        val repo = CredentialRepository(db)

        val cred1 = repo.getByUser(testCreds1.user)
        val cred2 = repo.getByUser(testCreds2.user)

        Assertions.assertThat(cred1).isPresent
        Assertions.assertThat(cred2).isPresent

        Assertions.assertThat(cred1.get().id).isEqualTo(testCreds1.id)
        Assertions.assertThat(cred2.get().id).isEqualTo(testCreds2.id)
    }

    @Test
    @Order(2)
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
    @Order(3)
    fun test_update_username() {
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

        Assertions.assertThat(testCreds1.loginName).isEqualTo(login1)
        Assertions.assertThat(testCreds2.loginName).isEqualTo(login2)
    }

    @Test
    @Order(3)
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

        Assertions.assertThat(testCreds1.passwordHash).isEqualTo(password1)
        Assertions.assertThat(testCreds2.passwordHash).isEqualTo(password2)
    }

    @Test
    @Order(3)
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

        Assertions.assertThat(testCreds1.passwordHash).isEqualTo(password)
        Assertions.assertThat(testCreds1.loginName).isEqualTo(login)
    }

    @Test
    @Order(3)
    fun test_update_invalidCred() {
        val repo = CredentialRepository(db)

        val testCred = Credential(EndUser(23, "Gandalf"), LoginName("GrayWizard"), hashPassword("Istari"))

        Assertions.assertThatThrownBy { repo.update(testCred) }
    }

    @Test
    @Order(4)
    fun test_delete_goodId() {
        val repo = CredentialRepository(db)

        Assertions.assertThat(repo.delete(testCreds1)).isTrue
        Assertions.assertThat(repo.delete(testCreds2)).isTrue

        val dbCreds1 = db.credentials.find { it.userId eq testCredsDto1.user.id }
        val dbCreds2 = db.credentials.find { it.userId eq testCredsDto2.user.id }

        Assertions.assertThat(dbCreds1).isNull()
        Assertions.assertThat(dbCreds2).isNull()
    }

    @Test
    @Order(4)
    fun test_delete_badId() {
        val repo = CredentialRepository(db)

        val creds = Credential(
            EndUser(1234, "Foo bar"),
            LoginName("FooBar145"),
            hashPassword("pass")
        )

        Assertions.assertThat(repo.delete(creds)).isFalse

        val dbCreds = db.credentials.find { it.userId eq creds.user.id }

        Assertions.assertThat(dbCreds).isNull()
    }
}
