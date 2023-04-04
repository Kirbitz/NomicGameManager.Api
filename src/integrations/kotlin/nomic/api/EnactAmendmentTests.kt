package nomic.api
import nomic.api.models.ResponseFormat
import nomic.domain.auth.ITokenRegistry
import nomic.domain.entities.AmendmentInputModel
import nomic.domain.entities.EndUser
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class EnactAmendmentTests(
    @Autowired val client: TestRestTemplate,
    @Autowired tokenRegistry: ITokenRegistry
) : BaseEndToEndTest(tokenRegistry) {

    private val amend = AmendmentInputModel(2, 8, 11, "amend", "text")
    private val request = createRequest(amend, user = EndUser(2, "Master Tester"))

    @Test
    fun `Add amendment to existing rule`() {
        val entity = client.exchange<ResponseFormat<String>>("/api/rules_amendments/enactAmendment", HttpMethod.POST, request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.CREATED)
        Assertions.assertThat(entity.body?.success).isTrue
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.CREATED)
        Assertions.assertThat(entity.body?.data.toString()).contains("Amendment Created")
    }
    private val amend4 = AmendmentInputModel(2, 9, 12, "amend", "")
    private val request4 = createRequest(amend4, user = EndUser(2, "Master Tester"))

    @Test
    fun `Add amendment with null description`() {
        val entity = client.exchange<ResponseFormat<String>>("/api/rules_amendments/enactAmendment", HttpMethod.POST, request4)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.CREATED)
        Assertions.assertThat(entity.body?.success).isTrue
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.CREATED)
        Assertions.assertThat(entity.body?.data.toString()).contains("Amendment Created")
    }

    private val amend1 = AmendmentInputModel(2, 8, 11, ":-;", "text")
    private val request1 = createRequest(amend1, user = EndUser(2, "Master Tester"))

    @Test
    fun `add amendment with illegal characters in title`() {
        val entity = client.exchange<ResponseFormat<String>>("/api/rules_amendments/enactAmendment", HttpMethod.POST, request1)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(entity.body?.success).isFalse
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(entity.body?.data.toString()).contains("Has Special Characters")
    }

    private val amend2 = AmendmentInputModel(2, 8, 11, "Title", "te-;xt")
    private val request2 = createRequest(amend2, user = EndUser(2, "Master Tester"))

    @Test
    fun `add amendment with illegal characters in description`() {
        val entity = client.exchange<ResponseFormat<String>>("/api/rules_amendments/enactAmendment", HttpMethod.POST, request2)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(entity.body?.success).isFalse
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(entity.body?.data.toString()).contains("Has Special Characters")
    }

    private val amend3 = AmendmentInputModel(-1, 8, 11, "Title", "text")
    private val request3 = createRequest(amend3, user = EndUser(2, "Master Tester"))

    @Test
    fun `add amendment to rule the does not exist`() {
        val entity = client.exchange<ResponseFormat<String>>("/api/rules_amendments/enactAmendment", HttpMethod.POST, request3)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        Assertions.assertThat(entity.body?.success).isFalse
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.NOT_FOUND)
        Assertions.assertThat(entity.body?.data.toString()).contains("not found")
    }
}
