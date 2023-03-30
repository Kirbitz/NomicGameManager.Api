package nomic.api

import nomic.api.models.ResponseFormat
import nomic.domain.auth.ITokenRegistry
import nomic.domain.entities.EndUser
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class RepealAmendmentsTests(@Autowired val client: TestRestTemplate, @Autowired tokenRegistry: ITokenRegistry) : BaseEndToEndTest(tokenRegistry) {
    private val request = createRequest<Any>(user = EndUser(2, "Master Tester"))

    @Test
    fun `Successfully Repealed an Amendment`() {
        val entity = client.exchange<ResponseFormat<String>>("/api/rules_amendments/repeal_amendment/8", HttpMethod.GET, request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)

        Assertions.assertThat(entity.body?.success).isTrue
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body?.data.toString()).contains("Amendment Repealed")
    }

    @Test
    fun `Bad ID`() {
        val entity = client.exchange<ResponseFormat<String>>("/api/rules_amendments/repeal_amendment/p", HttpMethod.GET, request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)

        Assertions.assertThat(entity.body?.success).isFalse
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(entity.body?.data.toString()).contains("Please enter a valid amendId")
    }

    @Test
    fun `Amendment ID not found`() {
        val entity = client.exchange<ResponseFormat<String>>("/api/rules_amendments/repeal_amendment/1224339", HttpMethod.GET, request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)

        Assertions.assertThat(entity.body?.success).isFalse
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.NOT_FOUND)
        Assertions.assertThat(entity.body?.data.toString()).contains("The entity with id 1224339 was not found on the database.")
    }
}
