package nomic.api

import nomic.domain.auth.TokenRegistry
import nomic.domain.entities.RulesAmendmentsModel
import nomic.domain.entities.User
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class RulesAmendmentsTests(
    @Autowired val client: TestRestTemplate,
    @Autowired tokenRegistry: TokenRegistry
) : BaseEndToEndTest(tokenRegistry) {

    private val request = createRequest<Any>(user = User(1, "Foo Bar Jr."))

    @Test
    fun `Found Rule And Amendment Data`() {
        val entity = client.exchange<String>("/api/rules_amendments/1", HttpMethod.GET, request)
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body).contains("MyRule1").contains("MyAmendment1")
    }

    @Test
    fun `Found Game With a Rule That Has No Amendments`() {
        val entity = client.exchange<List<RulesAmendmentsModel>>("/api/rules_amendments/1", HttpMethod.GET, request)
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body).anyMatch({ it.amendments!!.size == 0 })
    }

    @Test
    fun `Found Game With No Rules or Amendments`() {
        val entity = client.exchange<List<RulesAmendmentsModel>>("/api/rules_amendments/2", HttpMethod.GET, request)
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body?.size).isEqualTo(0)
    }

    // @Test
    fun `Found Game With Rules and Multiple Amendments`() {
        val entity = client.exchange<List<RulesAmendmentsModel>>("/api/rules_amendments/1", HttpMethod.GET, request)
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body).anyMatch({ it.amendments!!.size > 1 })
    }

    @Test
    fun `Bad Game ID`() {
        val entity = client.exchange<String>("/api/rules_amendments/apple", HttpMethod.GET, request)
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(entity.body).contains("Please enter a valid GameId!")
    }
}
