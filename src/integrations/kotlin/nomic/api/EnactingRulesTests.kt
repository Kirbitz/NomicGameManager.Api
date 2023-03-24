package nomic.api
import nomic.api.models.ResponseFormat
import nomic.domain.entities.RulesModel
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
class EnactingRulesTests(@Autowired val client: TestRestTemplate) : BaseEndToEndTest() {

    private val rule = RulesModel(77, 50, "Title", "Description", false, 6)
    private val request = createRequest(rule)

    @Test
    fun `Create Rule in an existing game`() {
        val entity = client.exchange<ResponseFormat<String>>("/api/rules_amendments/enactRule", HttpMethod.POST, request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.CREATED)

        Assertions.assertThat(entity.body?.success).isTrue
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.CREATED)
        Assertions.assertThat(entity.body?.data.toString()).contains("Rule Created")
    }

    private val rule1 = RulesModel(72, 51, "Title2", "", false, 6)
    private val request1 = createRequest(rule1)

    @Test
    fun `Create Rule with no description`() {
        val entity = client.exchange<ResponseFormat<String>>("/api/rules_amendments/enactRule", HttpMethod.POST, request1)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.CREATED)

        Assertions.assertThat(entity.body?.success).isTrue
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.CREATED)
        Assertions.assertThat(entity.body?.data.toString()).contains("Rule Created")
    }

    private val rule2 = RulesModel(73, 52, "", "Description", false, 6)
    private val request2 = createRequest(rule2)

    @Test
    fun `Create Rule with no Title`() {
        val entity = client.exchange<ResponseFormat<String>>("/api/rules_amendments/enactRule", HttpMethod.POST, request2)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.CREATED)

        Assertions.assertThat(entity.body?.success).isTrue
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.CREATED)
        Assertions.assertThat(entity.body?.data.toString()).contains("Rule Created")
    }

    private val rule3 = RulesModel(74, 54, ":::", "Description", false, 6)
    private val request3 = createRequest(rule3)

    @Test
    fun `Create Rule with illegal characters in Title`() {
        val entity = client.exchange<ResponseFormat<String>>("/api/rules_amendments/enactRule", HttpMethod.POST, request3)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)

        Assertions.assertThat(entity.body?.success).isFalse
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(entity.body?.data.toString()).contains("Has Special Characters")
    }

    private val rule4 = RulesModel(75, 56, "Title", "Descri:::::ption", false, 6)
    private val request4 = createRequest(rule4)

    @Test
    fun `Create Rule with illegal characters in Description`() {
        val entity = client.exchange<ResponseFormat<String>>("/api/rules_amendments/enactRule", HttpMethod.POST, request4)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)

        Assertions.assertThat(entity.body?.success).isFalse
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(entity.body?.data.toString()).contains("Has Special Characters")
    }

    private val rule5 = RulesModel(76, 57, "Title", "Description", false, 5)
    private val request5 = createRequest(rule5)

    @Test
    fun `Create rule in a game that does not exist`() {
        val entity = client.exchange<ResponseFormat<String>>("/api/rules_amendments/enactRule", HttpMethod.POST, request5)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)

        Assertions.assertThat(entity.body?.success).isFalse
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        Assertions.assertThat(entity.body?.data.toString()).contains("Internal Server Error")
    }
}
