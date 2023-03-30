package nomic.data.repositories.implementations

import nomic.data.dtos.UserDTO
import nomic.data.dtos.users
import nomic.domain.entities.EndUser
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

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
class UserRepositoryTest(@Autowired private val db: Database) {

    private val testUser1: EndUser
    private val testUserDto1: UserDTO

    private val testUser2: EndUser
    private val testUserDto2: UserDTO

    private val existingUser = EndUser(10, "Agamemnon")

    init {
        testUser1 = EndUser(57, "Marcus Aurelius")
        testUserDto1 = UserDTO {
            this.id = testUser1.id
            this.name = testUser1.name
        }

        testUser2 = EndUser(58, "Socrates")
        testUserDto2 = UserDTO {
            this.id = testUser2.id
            this.name = testUser2.name
        }
    }

    @Test
    @Order(1)
    fun test_create() {
        val repo = UserRepository(db)

        val user1 = repo.create(testUser1.name)
        val user2 = repo.create(testUser2.name)

        val dbUser1 = db.users.find { it.id eq user1.id }
        val dbUser2 = db.users.find { it.id eq user2.id }

        Assertions.assertThat(user1.name).isEqualTo(testUser1.name)
        Assertions.assertThat(dbUser1?.name).isEqualTo(testUserDto1.name)

        Assertions.assertThat(user2.name).isEqualTo(testUser2.name)
        Assertions.assertThat(dbUser2?.name).isEqualTo(testUserDto2.name)
    }

    @Order(2)
    @Test
    fun test_getById_goodId() {
        val repo = UserRepository(db)

        val dbUser = repo.getById(existingUser.id)

        Assertions.assertThat(dbUser).isPresent
        Assertions.assertThat(dbUser.get().id).isEqualTo(existingUser.id)
        Assertions.assertThat(dbUser.get().name).isEqualTo(existingUser.name)
    }

    @Order(2)
    @Test
    fun test_getById_badId_emptyOptional() {
        val repo = UserRepository(db)

        val user = repo.getById(-100)

        Assertions.assertThat(user).isEmpty
    }

    @Order(2)
    @Test
    fun test_findByName_goodName() {
        val repo = UserRepository(db)

        val dbUser1 = repo.findUserByName(existingUser.name)
        val dbUser2 = repo.findUserByName(testUser1.name)

        Assertions.assertThat(dbUser1).isPresent
        Assertions.assertThat(dbUser1.get().id).isEqualTo(existingUser.id)
        Assertions.assertThat(dbUser1.get().name).isEqualTo(existingUser.name)

        Assertions.assertThat(dbUser2).isPresent
        Assertions.assertThat(dbUser2.get().name).isEqualTo(testUser1.name)
    }

    @Order(2)
    @Test
    fun test_findByName_badName_emptyOptional() {
        val repo = UserRepository(db)

        val user = repo.findUserByName("Zeus")

        Assertions.assertThat(user).isEmpty
    }

    @Order(3)
    @Test
    fun test_update_name() {
        val repo = UserRepository(db)

        val actualTestUser2 = repo.findUserByName(testUser2.name).get()
        actualTestUser2.name = "Plato"

        existingUser.name = "Menelaus"

        repo.update(actualTestUser2)
        repo.update(existingUser)

        val dbTestUser2 = db.users.find { it.id eq actualTestUser2.id }!!
        val dbExistingUser = db.users.find { it.id eq existingUser.id }!!

        Assertions.assertThat(dbTestUser2.name).isEqualTo(actualTestUser2.name)
        Assertions.assertThat(existingUser.name).isEqualTo(dbExistingUser.name)
    }

    @Order(3)
    @Test
    fun test_update_invalidUser() {
        val repo = UserRepository(db)

        // The entity in the database that was created with testUser1
        // will not share the same id as testUser1, due to primary key increments and such.
        testUser1.name = "Commodus"

        Assertions.assertThatThrownBy { repo.update(testUser1) }
    }

    @Order(4)
    @Test
    fun test_delete_badUser() {
        val repo = UserRepository(db)

        Assertions.assertThat(repo.delete(testUser1)).isFalse
        Assertions.assertThat(repo.delete(testUser2)).isFalse

        val dbTestUser1 = db.users.find { it.id eq testUser1.id }
        val dbTestUser2 = db.users.find { it.id eq testUser2.id }

        Assertions.assertThat(dbTestUser1).isNull()
        Assertions.assertThat(dbTestUser2).isNull()
    }

    @Order(5)
    @Test
    fun test_delete_goodUser() {
        val repo = UserRepository(db)

        val actualTestUser1 = repo.findUserByName(testUser1.name).get()

        Assertions.assertThat(repo.delete(actualTestUser1)).isTrue
        Assertions.assertThat(repo.delete(existingUser)).isTrue

        val dbTestUser1 = db.users.find { it.id eq actualTestUser1.id }
        val dbExistingUser = db.users.find { it.id eq existingUser.id }

        Assertions.assertThat(dbTestUser1).isNull()
        Assertions.assertThat(dbExistingUser).isNull()
    }
}
