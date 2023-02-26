package nomic.api

import com.google.gson.Gson
import nomic.domain.entities.RulesAmendmentsModel
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class RulesAmendmentsTests(@Autowired val client: TestRestTemplate) {
    val gson = Gson()
    @Test
    fun `Found Rule And Amendment Data`() {
        val entity = client.getForEntity<String>("/api/rules_amendments/1")
        val result = gson.fromJson(entity.body, Array<RulesAmendmentsModel>::class.java).toList()

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(result.size).isGreaterThan(0)
        Assertions.assertThat(result[0].amendments?.size).isGreaterThan(0)
    }

    @Test
    fun `Found Game With Rules That Has No Amendments`() {
        val entity = client.getForEntity<String>("/api/rules_amendments/1")
        val result = gson.fromJson(entity.body, Array<RulesAmendmentsModel>::class.java).toList()

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(result[1].amendments?.size).isEqualTo(0)
    }

    @Test
    fun `Found Game With No Rules and Amendments`() {
        val entity = client.getForEntity<String>("/api/rules_amendments/2")
        val result = gson.fromJson(entity.body, Array<RulesAmendmentsModel>::class.java).toList()

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(result.size).isEqualTo(0)
    }

    @Test
    fun `Found Game With Rules Activity Equals False and Amendments Activity Equals True`() {
        val entity = client.getForEntity<String>("/api/rules_amendments/3")
        val result = gson.fromJson(entity.body, Array<RulesAmendmentsModel>::class.java).toList()

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(result.size).isEqualTo(0)
    }

    @Test
    fun `Found Game With Rules and Multiple Amendments`() {
        val entity = client.getForEntity<String>("/api/rules_amendments/1")
        val result = gson.fromJson(entity.body, Array<RulesAmendmentsModel>::class.java).toList()

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(result[2].amendments?.size).isGreaterThan(1)
    }

    @Test
    fun `Found Game With Rules and Amendment Activity Equals False`() {
        val entity = client.getForEntity<String>("/api/rules_amendments/1")
        val result = gson.fromJson(entity.body, Array<RulesAmendmentsModel>::class.java).toList()

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(result[3].amendments?.size).isEqualTo(0)
    }

    @Test
    fun `Bad Game ID`() {
        val entity = client.getForEntity<String>("/api/rules_amendments/apple")
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(entity.body).contains("Please enter a valid GameId!")
    }
}
