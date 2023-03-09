package nomic.data.repositories.implementations

import nomic.data.dtos.UserDTO
import nomic.data.dtos.users
import nomic.domain.entities.User
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

    private val testUser1: User
    private val testUserDto1: UserDTO

    private val testUser2: User
    private val testUserDto2: UserDTO

    init {
        testUser1 = User(57, "Marcus Aurelius")
        testUserDto1 = UserDTO {
            this.id = testUser1.id
            this.name = testUser1.name
        }

        testUser2 = User(58, "Socrates")
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
}
