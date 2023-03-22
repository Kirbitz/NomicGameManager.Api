package nomic.integration.api

import nomic.domain.entities.RepealRuleResponse
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class RepealingRulesTests(@Autowired val client: TestRestTemplate) : BaseEndToEndTest() {
    private val request = createRequest<Any>()

    @Test
    fun `Successfully Repealed a Rule`() {
        val entity = client.exchange<RepealRuleResponse>("/api/rules_amendments/repeal_rule/6", HttpMethod.GET, request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body.toString()).contains("Updated Successfully")
    }

    @Test
    fun `Bad ID`() {
        val entity = client.exchange<String>("/api/rules_amendments/repeal_rule/p", HttpMethod.GET, request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(entity.body.toString()).contains("Please enter a valid ruleId!")
    }

    @Test
    fun `Rule ID not found`() {
        val entity = client.exchange<String>("/api/rules_amendments/repeal_rule/1224339", HttpMethod.GET, request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        Assertions.assertThat(entity.body.toString()).contains("The entity with id 1224339 was not found on the database.")
    }
}
