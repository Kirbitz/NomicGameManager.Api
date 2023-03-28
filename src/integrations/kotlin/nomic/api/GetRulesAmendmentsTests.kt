package nomic.api

import nomic.api.models.ResponseFormat
import nomic.api.models.RulesAmendmentsApiModel
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class GetRulesAmendmentsTests(@Autowired val client: TestRestTemplate) : BaseEndToEndTest() {
    private val request = createRequest<Any>()

    @Test
    fun `Found Rule And Amendment Data`() {
        val entity = client.exchange<ResponseFormat<List<RulesAmendmentsApiModel>>>("/api/rules_amendments/collect/1", HttpMethod.GET, request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)

        Assertions.assertThat(entity.body?.success).isTrue
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body?.data?.size).isGreaterThan(0)
        Assertions.assertThat(entity.body?.data).anyMatch { it.amendments.size > 0 }
    }

    @Test
    fun `Found Game With Rules That Has No Amendments`() {
        val entity = client.exchange<ResponseFormat<List<RulesAmendmentsApiModel>>>("/api/rules_amendments/collect/1", HttpMethod.GET, request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)

        Assertions.assertThat(entity.body?.success).isTrue
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body?.data).anyMatch { it.amendments.size == 0 }
    }

    @Test
    fun `Found Game With No Rules and Amendments`() {
        val entity = client.exchange<ResponseFormat<List<RulesAmendmentsApiModel>>>("/api/rules_amendments/collect/2", HttpMethod.GET, request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)

        Assertions.assertThat(entity.body?.success).isTrue
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body?.data?.size).isEqualTo(0)
    }

    @Test
    fun `Found Game With Rules Activity Equals False and Amendments Activity Equals True`() {
        val entity = client.exchange<ResponseFormat<List<RulesAmendmentsApiModel>>>("/api/rules_amendments/collect/3", HttpMethod.GET, request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)

        Assertions.assertThat(entity.body?.success).isTrue
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body?.data?.size).isEqualTo(0)
    }

    @Test
    fun `Found Game With Rules and Multiple Amendments`() {
        val entity = client.exchange<ResponseFormat<List<RulesAmendmentsApiModel>>>("/api/rules_amendments/collect/1", HttpMethod.GET, request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)

        Assertions.assertThat(entity.body?.success).isTrue
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body?.data!![2].amendments.size).isGreaterThan(1)
    }

    @Test
    fun `Found Game With Rules and Amendment Activity Equals False`() {
        val entity = client.exchange<ResponseFormat<List<RulesAmendmentsApiModel>>>("/api/rules_amendments/collect/1", HttpMethod.GET, request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)

        Assertions.assertThat(entity.body?.success).isTrue
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body?.data!![3].amendments.size).isEqualTo(0)
    }

    @Test
    fun `Bad Game ID`() {
        val entity = client.exchange<ResponseFormat<String>>("/api/rules_amendments/collect/apple", HttpMethod.GET, request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)

        Assertions.assertThat(entity.body?.success).isFalse
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.BAD_REQUEST)

        Assertions.assertThat(entity.body?.data.toString()).contains("Please enter a valid GameId!")
    }
}
