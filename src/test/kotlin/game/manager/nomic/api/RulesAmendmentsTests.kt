package game.manager.nomic.api

import game.manager.nomic.api.config.DatabaseConfig
import game.manager.nomic.api.config.NomicConfigProperties
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Spy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class RulesAmendmentsTests(@Autowired val client: TestRestTemplate) {
    private final val nomicConfig = NomicConfigProperties()
    @Spy
    val mock: DatabaseConfig = DatabaseConfig(nomicConfig)

    @Test
    fun `Bad Game ID`() {
        val entity = client.getForEntity<String>("/api/rules_amendments/apple")
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(entity.body).contains("gameId was not a valid integer")
    }

    @Test
    fun `Internal Server Error`() {
        Mockito.`when`(mock.connectDB()).thenThrow(Exception("Internal Server Error"))
//        val nomicConfig = NomicConfigProperties()
//        val mock = spyk<Database>()
//        every { mock["connectDB"] } throws Exception("Internal Server Error")
        println(mock.connectDB())

        val entity = client.getForEntity<String>("/api/rules_amendments/123")
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        Assertions.assertThat(entity.body).contains("Internal server error")
    }
}